package builder;

import com.alibaba.datax.common.element.LongColumn;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.element.StringColumn;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.core.transport.record.DefaultRecord;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.EdgeBuilder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.util.HugegraphLogger;
import com.baidu.hugegraph.structure.graph.Edge;
import configs.SchemaConfig;
import org.junit.Test;
import org.slf4j.Logger;

public class EdgeBuilderTest {

    Configuration config = Configuration.from(SchemaConfig.jsonConfig_2);
    Logger log = HugegraphLogger.get(EdgeBuilderTest.class);

    @Test
    public void EdgeBuilderTest(){
        log.info("\n\nEdgeBuilderTest 1...");
        EdgeBuilder eBuilder = new EdgeBuilder(config);
        Record record = new DefaultRecord();
        record.addColumn(new LongColumn(4396));
        record.addColumn(new StringColumn("batina!"));
        record.addColumn(new LongColumn(6934));
        Edge e = eBuilder.build(record);
        log.info(e.toString());
        assert (long) e.sourceId() == 4396 && (long) e.targetId() == 6934;
    }
}
