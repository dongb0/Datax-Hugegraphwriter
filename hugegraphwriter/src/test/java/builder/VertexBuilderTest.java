package builder;

import com.alibaba.datax.common.element.LongColumn;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.core.transport.record.DefaultRecord;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.VertexBuilder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.util.HugegraphLogger;
import com.baidu.hugegraph.structure.graph.Vertex;
import configs.SchemaConfig;
import org.junit.Test;
import org.slf4j.Logger;

public class VertexBuilderTest {

    Configuration config = Configuration.from( SchemaConfig.jsonConfig_1);
    Logger log = HugegraphLogger.get(VertexBuilderTest.class);

    @Test
    public void vertexBuilderTest(){
        log.info("\n\nVertexBuilder Test 1...");
        VertexBuilder vbuilder = new VertexBuilder(config);
        Record record = new DefaultRecord();
        record.addColumn(new LongColumn(2048));
        Vertex v = vbuilder.build(record);
        log.info(v.toString());
        assert (int) v.id() == 2048;
    }
}
