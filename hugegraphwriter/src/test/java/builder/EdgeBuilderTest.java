package builder;

import com.alibaba.datax.common.element.DateColumn;
import com.alibaba.datax.common.element.LongColumn;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.element.StringColumn;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.core.transport.record.DefaultRecord;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.EdgeBuilder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.SchemaBuilder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.client.ClientHolder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.Key;
import com.alibaba.datax.plugin.writer.hugegraphwriter.util.HugegraphLogger;
import com.baidu.hugegraph.driver.GraphManager;
import com.baidu.hugegraph.structure.graph.Edge;
import com.baidu.hugegraph.structure.schema.VertexLabel;
import configs.SchemaConfig;
import org.junit.Test;
import org.slf4j.Logger;

import javax.ws.rs.client.ClientBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EdgeBuilderTest {

    Logger log = HugegraphLogger.get(EdgeBuilderTest.class);

    @Test
    public void EdgeBuilderTest() throws Exception {
        System.out.println("\n\nEdgeBuilderTest 1...");
        Configuration config = Configuration.from(SchemaConfig.jsonConfig_2);
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

        Configuration config = Configuration.from(SchemaConfig.jsonConfig_2);
        EdgeBuilder builder = new EdgeBuilder(config);
        List<Object> obj = Arrays.asList(1001, "test1");
        VertexLabel srcLabel = builder.getSourceLabel();
        String id1 = builder.buildVertexId(srcLabel, obj);
        log.info("get SourceLabel:{}, labelId:{}", srcLabel.toString(), srcLabel.id());
        // real id should be {vertex label id}:2Fe!test1
        assert id1.substring(1).equals(":2Fe!test1"): "expected id = [1]:2Fe!test1 but get " + id1;
    }


    @Test
    public void edgeBuilder_BuildPrimaryKeysEdgeTest() throws Exception {
        Configuration config = Configuration.from(SchemaConfig.jsonConfig_Schema_PrimaryKeysE);
        SchemaBuilder schemaBuilder = new SchemaBuilder(config);
        schemaBuilder.createSchemas();

        GraphManager gm = new GraphManager(ClientHolder.getClient(config), config.getString(Key.GRAPH));
//        Edge e1 = new Edge("schema_testE");
        Record r = new DefaultRecord();
        r.addColumn(new LongColumn(1001));
        r.addColumn(new StringColumn("test1"));
        r.addColumn(new LongColumn(1001));
        r.addColumn(new StringColumn("test1"));
        r.addColumn(new DateColumn(new Date()));

        EdgeBuilder builder = new EdgeBuilder(config);
        Edge e1 = builder.build(r);
        log.info("EdgeBuilder Build Edge:{}", e1.toString());

        String buildVertexId = (String)e1.targetId();
        assert buildVertexId.substring(1).equals(":2Fe!test1"): "expected get vertex id [{}]:2Fe!test1 but get " + buildVertexId;

        gm.addEdge(e1);
    }
}
