package com.alibaba.datax.plugin.writer.hugegraphwriter.builder;

import com.alibaba.datax.common.element.Column;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.PropertyType;
import com.alibaba.datax.plugin.writer.hugegraphwriter.struct.EdgeStruct;
import com.alibaba.datax.plugin.writer.hugegraphwriter.struct.ElementStruct;
import com.alibaba.datax.plugin.writer.hugegraphwriter.struct.ElementStruct.ColumnsConfHolder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.util.Pair;
import com.baidu.hugegraph.structure.graph.Edge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EdgeBuilder extends ElemBuilder<Edge, EdgeStruct> {

    public EdgeBuilder(Configuration config) {
        this.struct = createStruct(config);
    }

    @Override
    public Edge build(Record record) throws Exception{
        Edge edge = new Edge(struct.getLabel());

        edge.sourceLabel(struct.getSrcLabel());
        edge.targetLabel(struct.getDstLabel());

        List<Pair<String, Object>> vertexPKName = new ArrayList<>();
        List<Pair<String, ColumnsConfHolder>> properties = struct.getColumnsProperties();
        for (Pair<String, ColumnsConfHolder> pair : properties) {
            String propName = pair.getKey();
            ColumnsConfHolder cch = pair.getValue();

            //TODO optimize parseColumnIndex
            int index = getMappingColumnIndex(cch);
            Column col = record.getColumn(index);

            if (col == null && cch.nullable == false) {
                throw new Exception("Column[" + propName + "] nullable=false, but get null column");
            }
            if (col != null && col.getByteSize() != 0) {
                PropertyType pType = cch.propertyType;
                switch (pType) {
                    case srcId:
                        edge.sourceId(parseColumnType(cch, col));
                        break;
                    case dstId:
                        edge.targetId(parseColumnType(cch, col));
                        break;
                    case edgeProperty:
                        edge.property(propName, parseColumnType(cch, col));
                        break;
                    case vertexPrimaryProperty:
                        vertexPKName.add(new Pair<>(propName, col));
                        break;
                    default:
                        throw new Exception("Not support Property Type " + pType);
                }
            }
        }
        if (edge.sourceId() == null || edge.targetId() == null) {
            throw new Exception("sourceId or targetId not set!");
        }
        if(!vertexPKName.isEmpty()){
            buildVertexId(vertexPKName);
        }
        return edge;
    }

    void buildVertexId(List<Pair<String, Object>> kv){

    }
}
