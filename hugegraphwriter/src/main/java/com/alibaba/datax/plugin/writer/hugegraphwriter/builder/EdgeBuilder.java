package com.alibaba.datax.plugin.writer.hugegraphwriter.builder;

import com.alibaba.datax.common.element.Column;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.PropertyType;
import com.alibaba.datax.plugin.writer.hugegraphwriter.struct.EdgeStruct;
import com.alibaba.datax.plugin.writer.hugegraphwriter.struct.ElementStruct;
import com.baidu.hugegraph.structure.graph.Edge;

import java.util.Map;

public class EdgeBuilder extends ElemBuilder<Edge, EdgeStruct> {

    public EdgeBuilder(Configuration config){
        this.struct = createStruct(config);
    }

    @Override
    public Edge build(Record record) {
        Edge edge = new Edge(struct.getLabel());

        edge.sourceLabel(struct.getSrcLabel());
        edge.targetLabel(struct.getDstLabel());

        for(Map.Entry<String, ElementStruct.ColumnsConfHolder> prop : struct.getProperties().entrySet()){
            PropertyType pType = prop.getValue().propertyType;
            //TODO optimize parseColumnIndex
            int index = parseColumnIndex(prop.getKey());
            Column col = record.getColumn(index);


            if(col == null && prop.getValue().nullable == false){
                // throws Column {i} nullable=false, but get null column
            }
            if(col != null && col.getByteSize() != 0){
                switch(pType){
                    case srcId:
                        edge.sourceId(parseColumnType(prop.getValue(), col));
                        break;
                    case dstId:
                        edge.targetId(parseColumnType(prop.getValue(), col));
                        break;
                    case edgeProperty:
                        edge.property(prop.getKey(), parseColumnType(prop.getValue(), col));
                        break;
                    default:
                        // throws Exception
                }
            }
        }
        if(edge.sourceId() == null || edge.targetId() == null){
            // throw Must set sourceId and targetId
        }
        return edge;
    }
}
