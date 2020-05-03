package task;

import com.alibaba.datax.common.element.LongColumn;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.element.StringColumn;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.core.transport.record.DefaultRecord;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.EdgeBuilder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.ElemBuilder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.SchemaBuilder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.VertexBuilder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.client.ClientHolder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.ElemType;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.Key;
import com.alibaba.datax.plugin.writer.hugegraphwriter.task.TaskExecutor;
import com.alibaba.datax.plugin.writer.hugegraphwriter.util.HugegraphLogger;
import com.baidu.hugegraph.driver.GraphManager;
import com.baidu.hugegraph.structure.GraphElement;
import com.baidu.hugegraph.structure.graph.Vertex;
import configs.SchemaConfig;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskTest {

    Logger log = HugegraphLogger.get(TaskTest.class);

    private  <T extends ElemBuilder> void submitVertex(int num, T builder){
        int max_buffer = 48;
        TaskExecutor taskExecutor = new TaskExecutor(2);
        List<GraphElement> records = new ArrayList<>(max_buffer);
        ElemType type = ElemType.VERTEX;
        for(int i = 0 ; i < num; i++){
            if(i % 10000 == 0)
                log.info("{} {} added.", i, type.string());
            Record r = new DefaultRecord();
            r.addColumn(new LongColumn(i));
            r.addColumn(new StringColumn("task-test-" + type.string() + i));
            records.add(builder.build(r));
            if(records.size() >= max_buffer){
                taskExecutor.submitBatch(records, ElemType.VERTEX);
                records = new ArrayList<>(max_buffer);
            }
        }
    }

    @Test
    public void batchSubmitVertexTest(){
        int num = 5 * 10000;
        Configuration config = Configuration.from(SchemaConfig.jsonConfig_TaskTestV);
        SchemaBuilder taskSB = new SchemaBuilder(config);
        VertexBuilder vb = new VertexBuilder(config);
        taskSB.createSchemas();

        StopWatch timer = new StopWatch();
        timer.start();
        submitVertex(num, vb);
        timer.stop();
        log.info("Insert {} vertices, run time: {}s", num, timer.getTime()/1000);
    }

    public void submitEdge(int num, EdgeBuilder builder){
        int max_buffer = 48;
        TaskExecutor taskExecutor = new TaskExecutor(2);
        List<GraphElement> records = new ArrayList<>(max_buffer);
        ElemType type = ElemType.EDGE;
        Random random = new Random();
        for(int i = 0 ; i < num; i++){
            if(i % 10000 == 0)
                log.info("{} {} added.", i, type.string());
            Record r = new DefaultRecord();
            r.addColumn(new LongColumn(Math.abs(random.nextInt() % num)));
            r.addColumn(new StringColumn("task-test-" + type.string() + i));
            r.addColumn(new LongColumn(Math.abs(random.nextInt() % num)));
            records.add(builder.build(r));
            if(records.size() >= max_buffer){
                taskExecutor.submitBatch(records, ElemType.EDGE);
                log.debug(records.get(0).toString());
                records = new ArrayList<>(max_buffer);
            }
        }
    }

    @Test
    public void batchSubmitEdgeTest(){
//        batchSubmitVertexTest();

        int num = 2*10000;
        Configuration config = Configuration.from(SchemaConfig.jsonConfig_TaskTestE);
        SchemaBuilder taskSB = new SchemaBuilder(config);
        EdgeBuilder eb = new EdgeBuilder(config);
        taskSB.createSchemas();

        StopWatch timer = new StopWatch();
        timer.start();
        submitEdge(num, eb);
        timer.stop();
        log.info("Insert {} Edges, run time: {}s", num, timer.getTime()/1000);
    }
}
