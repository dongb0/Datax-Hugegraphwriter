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
import com.baidu.hugegraph.structure.graph.Edge;
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

    private void submitVertex(int num, VertexBuilder builder){
        int max_buffer = 16;
        TaskExecutor taskExecutor = new TaskExecutor(2);
        List<GraphElement> records = new ArrayList<>(max_buffer);
        ElemType type = ElemType.VERTEX;
        for(int i = 0 ; i <= num; i++){
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
        if(!records.isEmpty())
            taskExecutor.submitBatch(records, ElemType.VERTEX);
    }

//    @Test
    public void batchSubmitVertexTest(){
        System.out.println("\n\nInsert Batch Vertices Test....");
        int num = 5 * 1000;
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
        int max_buffer = 16;
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
                records = new ArrayList<>(max_buffer);
            }
        }
        if(!records.isEmpty()){
            taskExecutor.submitBatch(records, ElemType.EDGE);
        }
    }

    @Test
    public void batchSubmitEdgeTest(){
        batchSubmitVertexTest();

        System.out.println("\n\nInsert Batch Edges Test...");

        int num = 4 * 20;
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

    @Test
    public void insertEdgeTest(){
        int num = 50;
        Configuration configV = Configuration.from(SchemaConfig.jsonConfig_TaskTestV);
        new SchemaBuilder(configV).createSchemas();
        VertexBuilder vb = new VertexBuilder(configV);

        //init 50+ vertices(id:[0,50]) for edge test later
        submitVertex(num, vb);

        Configuration config = Configuration.from(SchemaConfig.jsonConfig_TaskTestE);
        SchemaBuilder taskSB = new SchemaBuilder(config);
        taskSB.createSchemas();
        EdgeBuilder eb = new EdgeBuilder(config);
        TaskExecutor taskExecutor = new TaskExecutor(2);

        Record r = new DefaultRecord();
        r.addColumn(new LongColumn(1));
        r.addColumn(new StringColumn("task-test-E-failTest-1-success"));
        r.addColumn(new LongColumn(10));

        // success
        GraphElement elem = taskExecutor.submitOne(eb.build(r), ElemType.EDGE);
        log.info("Insert Edge 1 complete, ret:{}", elem);
        assert elem == null: "expected element is null but get " + elem.toString();

        r = new DefaultRecord();
        r.addColumn(new LongColumn(-1));
        r.addColumn(new StringColumn("task-test-E-failTest-2"));
        r.addColumn(new LongColumn(-2));

        //fail
        elem = taskExecutor.submitOne(eb.build(r), ElemType.EDGE);
        log.info("Insert Edge 2 success, ret:{}", elem);
        assert elem != null: "expected element not null";
    }


    @Test
    public void batchSubmitEdgesFailTest(){
        System.out.println("\n\nRunning batchSubmitEdgesFailTest....");

        int num = 50;
        Configuration configV = Configuration.from(SchemaConfig.jsonConfig_TaskTestV);
        new SchemaBuilder(configV).createSchemas();
        VertexBuilder vb = new VertexBuilder(configV);

        //init 50+ vertices for edge test later
        submitVertex(num, vb);

        Configuration config = Configuration.from(SchemaConfig.jsonConfig_TaskTestE);
        SchemaBuilder taskSB = new SchemaBuilder(config);
        taskSB.createSchemas();
        EdgeBuilder eb = new EdgeBuilder(config);
        TaskExecutor taskExecutor = new TaskExecutor(2);

        Record r = new DefaultRecord();
        r.addColumn(new LongColumn(-167));
        r.addColumn(new StringColumn("task-test-E-fail"));
        r.addColumn(new LongColumn(-7788));

        List<Edge> records = new ArrayList<>(16);
        records.add(eb.build(r));

        r = new DefaultRecord();
        r.addColumn(new LongColumn(Math.abs(10)));
        r.addColumn(new StringColumn("task-test-E-10-20"));
        r.addColumn(new LongColumn(Math.abs(20)));
        records.add(eb.build(r));

        r = new DefaultRecord();
        r.addColumn(new LongColumn(Math.abs(20)));
        r.addColumn(new StringColumn("task-test-E-20-30"));
        r.addColumn(new LongColumn(Math.abs(30)));
        records.add(eb.build(r));

        List<?> errorEdges = taskExecutor.submitBatch(records, ElemType.EDGE);
        assert errorEdges.size() == 1: "expected 1 error records, but get " + errorEdges.size();
        log.error("Error records:{}", errorEdges);

        records.remove(0);
        errorEdges = taskExecutor.submitBatch(records, ElemType.EDGE);
        assert errorEdges == null: "expect null!" + errorEdges;
    }
}
