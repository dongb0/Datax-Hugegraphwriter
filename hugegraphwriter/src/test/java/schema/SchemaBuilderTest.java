package schema;

import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.writer.hugegraphwriter.client.ClientHolder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.ElemType;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.Key;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.SchemaBuilder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.util.HugegraphLogger;
import com.baidu.hugegraph.client.RestClient;
import com.baidu.hugegraph.driver.SchemaManager;
import com.baidu.hugegraph.rest.RestResult;
import com.baidu.hugegraph.structure.schema.EdgeLabel;
import com.baidu.hugegraph.structure.schema.PropertyKey;
import com.baidu.hugegraph.structure.schema.VertexLabel;
import configs.SchemaConfig;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.List;

public class SchemaBuilderTest {
    private Configuration config = Configuration.from(SchemaConfig.jsonConfig_1);
    private SchemaBuilder sb = new SchemaBuilder(ClientHolder.getClient(config), "hugegraph", config);
    Logger log = HugegraphLogger.get(SchemaBuilder.class);

    /* TODO
         list all property, vertex, edge. index schema that will be created
         and delete all test schema afterward
     */

    @Test
    public void schemaManagerTest(){
        RestClient client = ClientHolder.getClient(config);
        SchemaManager schema = new SchemaManager(client, "hugegraph");
        RestResult res = client.get("graphs/hugegraph/schema/propertykeys");
        Assert.assertEquals(res.status(), 200);

        List<VertexLabel>  vList = schema.getVertexLabels();
        log.info(vList.toString());

        for(VertexLabel v : vList){
            schema.removeVertexLabel(v.name());
        }
        vList = schema.getVertexLabels();
        assert vList.isEmpty();
    }

    @Test
    public void createPropertySchemaTest(){
        Configuration conf = config.getConfiguration("column[0]");
        Assert.assertEquals(conf.getString("name"), "id");

        ElemType eType = ElemType.valueOf(config.getString(Key.ELEMENT_TYPE).toUpperCase());
        Assert.assertEquals(eType, ElemType.VERTEX);
        PropertyKey pk = sb.createPropertySchema(conf, eType);

        log.info("Test Create property key: {}", pk.toString());
        Assert.assertEquals(pk.name(), "id");
    }

//    TODO
//    @Test
//    public void createPropertySchemasTest(){
//        SchemaBuilder sb = new SchemaBuilder(ClientHolder.getClient(config), "hugegraph", config);
//
//
//    }

    @Test
    public void createVertexSchemaTest(){
        sb.createPropertySchemas();
        VertexLabel vl = sb.createVertexSchema();
        log.info("Test Create Vertex schema: {}", vl);
        assert vl != null;
    }

    @Test
    public void createEdgeSchemaTest(){
        Configuration conf = Configuration.from(SchemaConfig.jsonConfig_2);
        SchemaBuilder edge_sb = new SchemaBuilder(conf);

        edge_sb.createPropertySchemas();
        EdgeLabel el = edge_sb.createEdgeSchema();
        log.info("Test Create Edge schema: {}", el);
        assert el != null;
    }

    @Test
    public void createSchemaTest(){
        Configuration conf = Configuration.from(SchemaConfig.jsonConfig_1);
        log.info("{}", config.toString());
        SchemaBuilder sb = new SchemaBuilder(conf);
        sb.createSchemas();
    }


}
