package com.alibaba.datax.plugin.writer.hugegraphwriter.builder;

import com.alibaba.datax.common.element.Column;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.element.StringColumn;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.PropertyType;
import com.alibaba.datax.plugin.writer.hugegraphwriter.struct.ElementStruct.ColumnsConfHolder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.struct.VertexStruct;
import com.baidu.hugegraph.structure.graph.Vertex;


import java.util.Map;


public class VertexBuilder extends ElemBuilder<Vertex, VertexStruct> {

    public VertexBuilder(Configuration config){
        this.struct = createStruct(config);
    }

    @Override
    public Vertex build(Record record) {
        // currently implement customize Id strategy only, which means only 1 column is ID
        // TODO implement primaryKey id strategy

        Vertex vertex = new Vertex(struct.getLabel());
        Map<String, ColumnsConfHolder> properties = this.struct.getProperties();
        for(Map.Entry<String, ColumnsConfHolder> prop : properties.entrySet()){

            int index = parseColumnIndex(prop.getKey());
            Column col = record.getColumn(index);
            PropertyType pType = prop.getValue().propertyType;

            if(col == null && prop.getValue().nullable == false){
                // throws Column {i} nullable=false, but get null column
            }
            if(col != null && col.getByteSize() != 0){
                switch (pType){
                    case vertexProperty:
                        //extra search in Map<Str, Col>?  TODO  optimize
                        vertex.property(prop.getKey(), parseColumnType(prop.getValue(), col));
                        break;
                    case vertexId:
                        vertex.id(parseColumnType(prop.getValue(), col));
                        break;
                }
            }
        }
        return vertex;
    }

}
