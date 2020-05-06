package schema;

import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.SchemaBuilder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.client.ClientHolder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.util.Pair;
import com.baidu.hugegraph.driver.GraphManager;
import com.baidu.hugegraph.structure.graph.Vertex;
import configs.SchemaConfig;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SchemaPrimaryKeysTest {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    public void createPrimaryKeysVertexTest(){
        Configuration config = Configuration.from(SchemaConfig.jsonConfig_Schema_PrimaryKeysV);
        SchemaBuilder vSchemaBuilder = new SchemaBuilder(config);
        vSchemaBuilder.createSchemas();

        GraphManager gm = ClientHolder.getGraphManager();
        Vertex v1 = new Vertex("schema_testV");
        v1.property("id", 1001);
        v1.property("name", "test1");
        Vertex retV = gm.addVertex(v1);
        log.info("insert Vertex:{}", retV);
        assert retV.property("name").equals("test1"): "expected property name == test1, get " + retV.property("name");
    }

    @Test
    public void createEdgeTest_VertexPrimaryKeys(){

    }
}
