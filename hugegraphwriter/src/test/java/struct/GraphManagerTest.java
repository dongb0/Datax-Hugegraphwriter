package struct;

import com.alibaba.datax.common.element.LongColumn;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.element.StringColumn;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.core.transport.record.DefaultRecord;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.EdgeBuilder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.SchemaBuilder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.VertexBuilder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.client.ClientHolder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.util.HugegraphLogger;
import com.baidu.hugegraph.client.RestClient;
import com.baidu.hugegraph.driver.GraphManager;
import com.baidu.hugegraph.structure.graph.Edge;
import com.baidu.hugegraph.structure.graph.Vertex;
import configs.SchemaConfig;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

public class GraphManagerTest {

    Logger log  = HugegraphLogger.get(GraphManagerTest.class);
    Configuration vertexConfig = Configuration.from(SchemaConfig.jsonConfig_1);
    Configuration edgeConfig = Configuration.from(SchemaConfig.jsonConfig_2);
    RestClient client = ClientHolder.getClient("localhost", 8080, "hugegraph");
    GraphManager gm = new GraphManager(client, "hugegraph");

    SchemaBuilder vertex_sb = new SchemaBuilder(vertexConfig);
    SchemaBuilder edge_sb = new SchemaBuilder(edgeConfig);

    public void insertVertexTest(){
        vertex_sb.createPropertySchemas();
        vertex_sb.createVertexSchema();

        Vertex v = new Vertex("InsertTestV_1");
        v.id(-776);
        v.property("name", "657hula");
        Vertex resV = gm.addVertex(v);
        log.info(resV.toString());

        v = new Vertex("InsertTestV_1");
        v.id(-63214);
        resV = gm.addVertex(v);
        log.info(resV.toString());

        v = new Vertex("InsertTestV_1");
        v.id(-214);
        resV = gm.addVertex(v);
        log.info(resV.toString());
    }

    @Test
    public void insertEdgeTest(){
        System.out.println("\n\nInsert V/E Test 1");

        insertVertexTest();
        edge_sb.createPropertySchemas();
        edge_sb.createEdgeSchema();
        Edge e = new Edge("InsertTestE_1");
        e.sourceId(-63214); e.sourceLabel("InsertTestV_1");
        e.targetId(-214); e.targetLabel("InsertTestV_1");
        log.info("building edge: {}", e.toString());
        Edge resE = gm.addEdge(e);
        log.info("return Edge: {}",resE.toString());
        assert resE.id() != null;

        e.sourceId(-776); e.sourceLabel("InsertTestV_1");
        e.targetId(-214); e.targetLabel("InsertTestV_1");
        e.property("name", "hanasei");
        resE = gm.addEdge(e);
        log.info("return Edge: {}",resE.toString());
        assert resE.id() != null;
    }

    @Test
    public void insertVertexTest2(){
        vertex_sb.createPropertySchemas();
        vertex_sb.createVertexSchema();

        Record r1 = new DefaultRecord(), r2 = new DefaultRecord();
        r1.addColumn(new LongColumn(-1001));
        r1.addColumn(new StringColumn("user001"));

        r2.addColumn(new LongColumn(-1002));
        r2.addColumn(new StringColumn("user002"));

        VertexBuilder vb = new VertexBuilder(vertexConfig);
        Vertex v1 = vb.build(r1), v2 = vb.build(r2);
        log.info("v1.id.class={}", v1.id().getClass());
        assert v1.id().getClass() == Long.class;

        log.info("v1: {}, v2: {}", v1.toString(), v2.toString());
        List<Vertex> retVertice = gm.addVertices(Arrays.asList(v1, v2));
        //id changed from Long to String ?

        assert Integer.parseInt((String)retVertice.get(0).id()) == -1001;
        assert Integer.parseInt((String)retVertice.get(1).id()) == -1002;
    }

    @Test
    public void insertEdgeTest2(){
        System.out.println("\n\nInsert V/E Test 2");

        insertVertexTest2();

        edge_sb.createPropertySchemas();
        edge_sb.createEdgeSchema();

        Record r1 = new DefaultRecord(), r2 = new DefaultRecord();

        r1.addColumn(new LongColumn(-1001));
        r1.addColumn(new StringColumn("Glincy"));
        r1.addColumn(new LongColumn(-1002));

        r2.addColumn(new LongColumn(-1001));
        r2.setColumn(2, new LongColumn(-1001));

        EdgeBuilder eb = new EdgeBuilder(edgeConfig);
        Edge e1 = eb.build(r1), e2 = eb.build(r2);
        log.info("e1: {}, e2: {}", e1.toString(), e2.toString());

        List<Edge> retEdges = gm.addEdges(Arrays.asList(e1,e2));
        assert (long)retEdges.get(0).sourceId() == -1001;
        assert (long)retEdges.get(1).sourceId() == -1001;
    }
}
