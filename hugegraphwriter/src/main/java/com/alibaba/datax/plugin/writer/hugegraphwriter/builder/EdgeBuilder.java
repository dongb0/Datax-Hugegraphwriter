package com.alibaba.datax.plugin.writer.hugegraphwriter.builder;

import com.alibaba.datax.common.element.Column;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.writer.hugegraphwriter.client.ClientHolder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.Key;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.PropertyType;
import com.alibaba.datax.plugin.writer.hugegraphwriter.struct.EdgeStruct;
import com.alibaba.datax.plugin.writer.hugegraphwriter.struct.ElementStruct;
import com.alibaba.datax.plugin.writer.hugegraphwriter.struct.ElementStruct.ColumnsConfHolder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.util.Pair;
import com.baidu.hugegraph.driver.SchemaManager;
import com.baidu.hugegraph.structure.graph.Edge;
import com.baidu.hugegraph.structure.graph.Vertex;
import com.baidu.hugegraph.structure.schema.VertexLabel;
import com.baidu.hugegraph.util.LongEncoding;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EdgeBuilder extends ElemBuilder<Edge, EdgeStruct> {

    private VertexLabel sourceLabel = null;
    private VertexLabel targetLabel = null;
    private SchemaManager schemaManager = null;

    public EdgeBuilder(Configuration config) {
        this.struct = createStruct(config);
        this.schemaManager = new SchemaManager(ClientHolder.getClient(config), config.getString(Key.GRAPH));
    }

    @Override
    public Edge build(Record record) throws Exception{
        Edge edge = new Edge(struct.getLabel());

        edge.sourceLabel(struct.getSrcLabel());
        edge.targetLabel(struct.getDstLabel());

        List<Object> srcPKprops = new ArrayList<>(), dstPKprops = new ArrayList<>();
        List<Pair<String, ColumnsConfHolder>> properties = struct.getColumnsProperties();
        for (Pair<String, ColumnsConfHolder> pair : properties) {
            String propName = pair.getKey();
            ColumnsConfHolder cch = pair.getValue();

            int index = getMappingColumnIndex(cch);
            Column col = record.getColumn(index);

            if (col == null && cch.nullable == false) {
                throw new Exception("Column[" + propName + "] nullable=false, but get null column");
            }
            if (col != null && col.getByteSize() != 0) {
                PropertyType pType = cch.propertyType;
                switch (pType) {
                    //---- Id Strategy: customize id (num)
                    case srcId:
                        edge.sourceId(parseColumnType(cch, col));
                        break;
                    case dstId:
                        edge.targetId(parseColumnType(cch, col));
                        break;
                    //---- Id Strategy: customize id
                    case edgeProperty:
                        edge.property(propName, parseColumnType(cch, col));
                        break;
                    case srcPrimaryProperty:
                        srcPKprops.add(parseColumnType(cch, col));
                        break;
                    case dstPrimaryProperty:
                        dstPKprops.add(parseColumnType(cch, col));
                        break;
                    default:
                        throw new Exception("Not support Property Type " + pType);
                }
            }
        }
        if(!srcPKprops.isEmpty() || !dstPKprops.isEmpty()){
            String srcId = buildVertexId(getSourceLabel(), srcPKprops), dstId = buildVertexId(getTargetLabel(), dstPKprops);
//            log.info("buildSrcId:{}, buildDstId:{}", srcId, dstId);
            edge.sourceId(srcId);
            edge.targetId(dstId);
        }
//        log.info("Display srcPKprops:{}", srcPKprops);
//        log.info("Display dstPKprops:{}", dstPKprops);
        if (edge.sourceId() == null || edge.targetId() == null) {
            throw new Exception("sourceId or targetId not set!");
        }
        return edge;
    }

    public String buildVertexId(VertexLabel vertexLabel,List<Object> kv){
        StringBuilder vertexId = new StringBuilder();
        vertexId.append(vertexLabel.id()).append(":");
        for(Object value : kv){
            String pkValue;
            if(value instanceof Number || value instanceof Date){
                pkValue = LongEncoding.encodeNumber(value);
            }else{
                pkValue = String.valueOf(value);
            }

            //replace ':' or '!' in original property value
            pkValue = StringUtils.replaceEach(pkValue, Key.VERTEX_ID_CHAR, Key.VERTEX_ID_CHAR_REPLACE);
            vertexId.append(pkValue).append("!");
        }
        vertexId.deleteCharAt(vertexId.length() - 1);
        return vertexId.toString();
    }

    public VertexLabel getSourceLabel()
    {
        if(sourceLabel == null){
            String srcLabel = struct.getSrcLabel();
            this.sourceLabel = schemaManager.getVertexLabel(srcLabel);
        }
        return sourceLabel;
    }

    public void setSourceLabel(VertexLabel sourceLabel) {
        this.sourceLabel = sourceLabel;
    }

    public VertexLabel getTargetLabel() {
        if(targetLabel == null){
            String dstLabel = struct.getDstLabel();
            this.targetLabel = schemaManager.getVertexLabel(dstLabel);
        }
        return targetLabel;
    }

    public void setTargetLabel(VertexLabel targetLabel) {
        this.targetLabel = targetLabel;
    }
}
