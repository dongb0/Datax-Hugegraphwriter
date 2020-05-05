package com.alibaba.datax.plugin.writer.hugegraphwriter;

import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.plugin.RecordReceiver;
import com.alibaba.datax.common.plugin.TaskPluginCollector;
import com.alibaba.datax.common.spi.Writer;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.EdgeBuilder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.ElemBuilder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.VertexBuilder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.client.ClientHolder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.SchemaBuilder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.ElemType;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.Key;
import com.alibaba.datax.plugin.writer.hugegraphwriter.task.TaskExecutor;
import com.baidu.hugegraph.structure.GraphElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HugegraphWriter extends Writer {

    private static final Logger log = LoggerFactory.getLogger(HugegraphWriter.class);

    public static class Job extends Writer.Job {

        private Configuration jobConfig = null;
        private SchemaBuilder schema = null;

        @Override
        public void init() {
            this.jobConfig = getPluginJobConf();
            log.info("Job plugin config: {}", jobConfig.toJSON());
        }

        @Override
        public void prepare() {
            super.prepare();

            this.schema = new SchemaBuilder(this.jobConfig);
            schema.createSchemas();
        }

        @Override
        public List<Configuration> split(int mandatoryNumber) {
            log.info("Hugegraph Split begin...");
            List<Configuration> configList = new ArrayList<>();
            for (int i = 0; i < mandatoryNumber; i++) {
                configList.add(this.jobConfig.clone());
            }
            log.info("Hugegraph Split end...");
            return configList;
        }

        @Override
        public void destroy() {
            ClientHolder.close();
        }
    }

    public static class Task extends Writer.Task {

        ElemBuilder elemBuilder = null;
        Configuration taskConfig = null;
        ElemType type = null;
        TaskExecutor taskExecutor = null;
        TaskPluginCollector collector = null;

        @Override
        public void init() {
            log.info("hugegraph Task init...");
            taskConfig = getPluginJobConf();
            type = ElemType.valueOf(taskConfig.getString(Key.ELEMENT_TYPE));
            switch (type) {
                case VERTEX:
                    elemBuilder = new VertexBuilder(taskConfig);
                    break;
                case EDGE:
                    elemBuilder = new EdgeBuilder(taskConfig);
                    break;
            }
            taskExecutor = new TaskExecutor(1, elemBuilder);
            collector = getTaskPluginCollector();
            log.info("ElemBuilder Type: {}", elemBuilder.getClass());
        }

        @Override
        public void prepare() {
            super.prepare();
        }

        @Override
        public void startWrite(RecordReceiver lineReceiver) {
            log.info("HugeGraph start write...");
            Record r = null;
            final int batchSize = 32;
            List<Record> records = new ArrayList<>(batchSize);
            List<Record> errors = new ArrayList<>();

            while ((r = lineReceiver.getFromReader()) != null) {
                records.add(r);
                if (records.size() >= batchSize) {
                    List<Record> err = taskExecutor.submitBatch(records, type);
                    records = new ArrayList<>(batchSize);
                    if (err != null) {
                        errors.addAll(err);
                    }
                }
            }
            if (!records.isEmpty()) {
                List<Record> err = taskExecutor.submitBatch(records, type);
                if (err != null) {
                    errors.addAll(err);
                }
            }

            log.info("Dirty record size:{}, [{}]", errors.size(), errors);
            for (Record err : errors) {
                collector.collectDirtyRecord(err, new Exception("Element writer failed!"));
            }
            log.info("HugeGraph complete write...");
        }

        @Override
        public void post() {
            super.post();
        }

        @Override
        public void destroy() {
            taskExecutor.shutdown();
        }
    }
}
