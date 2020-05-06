package builder;

import com.alibaba.datax.common.element.LongColumn;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.element.StringColumn;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.core.transport.record.DefaultRecord;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.EdgeBuilder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.SchemaBuilder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.util.HugegraphLogger;
import com.baidu.hugegraph.structure.graph.Edge;
import com.baidu.hugegraph.structure.schema.VertexLabel;
import configs.SchemaConfig;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EdgeBuilderTest {

    Configuration config = Configuration.from(SchemaConfig.jsonConfig_2);
    Logger log = HugegraphLogger.get(EdgeBuilderTest.class);

    @Test
    public void EdgeBuilderTest() throws Exception {
        System.out.println("\n\nEdgeBuilderTest 1...");
        EdgeBuilder eBuilder = new EdgeBuilder(config);
        Record record = new DefaultRecord();
        record.addColumn(new LongColumn(4396));
        record.addColumn(new StringColumn("batina!"));
        record.addColumn(new LongColumn(6934));
        Edge e = eBuilder.build(record);
        log.info(e.toString());
        assert (long) e.sourceId() == 4396 && (long) e.targetId() == 6934;
    }


    @Test
    public void edgeBuilderBuildVertexIdTest(){
        System.out.println("\n\nEdgeBuilder Build Vertex Id with Primary Key Id strategy ...");

        //craate vertex schema
        Configuration vertexConfig = Configuration.from(SchemaConfig.jsonConfig_1);
        SchemaBuilder schemaBuilder = new SchemaBuilder(vertexConfig);
        schemaBuilder.createSchemas();

        EdgeBuilder builder = new EdgeBuilder(config);
        List<Object> obj = Arrays.asList(1001, "test1");
        VertexLabel srcLabel = builder.getSourceLabel();
        String id1 = builder.buildVertexId(srcLabel, obj);
        log.info("get SourceLabel:{}, labelId:{}", srcLabel.toString(), srcLabel.id());
        // real id should be {vertex label id}:2Fe!test1
        assert id1.substring(1).equals(":2Fe!test1"): "expected id = [1]:2Fe!test1 but get " + id1;
    }
}
