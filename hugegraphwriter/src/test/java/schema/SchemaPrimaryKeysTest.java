package schema;

import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.SchemaBuilder;
import configs.SchemaConfig;
import org.junit.Test;

public class SchemaPrimaryKeysTest {

    @Test
    public void createPrimaryKeysVertexTest(){
        Configuration config = Configuration.from(SchemaConfig.jsonConfig_Schema_PrimaryKeysV);
        SchemaBuilder vSchemaBuilder = new SchemaBuilder(config);
        vSchemaBuilder.createSchemas();
    }
}
