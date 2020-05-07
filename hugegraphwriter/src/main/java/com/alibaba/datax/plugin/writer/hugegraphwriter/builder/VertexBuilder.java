package com.alibaba.datax.plugin.writer.hugegraphwriter.builder;

import com.alibaba.datax.common.element.Column;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.PropertyType;
import com.alibaba.datax.plugin.writer.hugegraphwriter.struct.ElementStruct.ColumnsConfHolder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.struct.VertexStruct;
import com.alibaba.datax.plugin.writer.hugegraphwriter.util.Pair;
import com.baidu.hugegraph.structure.graph.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class VertexBuilder extends ElemBuilder<Vertex, VertexStruct> {

    public VertexBuilder(Configuration config) {
        this.struct = createStruct(config);
    }

    @Override
    public Vertex build(Record record) throws Exception{
        // currently implement customize Id strategy only, which means only 1 column is ID
        // TODO implement primaryKey id strategy

        Vertex vertex = new Vertex(struct.getLabel());
        List<Pair<String, ColumnsConfHolder>> properties = struct.getColumnsProperties();
        for (Pair<String, ColumnsConfHolder> pair: properties) {
            String propName = pair.getKey();
            ColumnsConfHolder cch = pair.getValue();

            int index = getMappingColumnIndex(cch);
            Column col = record.getColumn(index);
            if (col == null && cch.nullable == false) {
                throw new Exception("Column[" + propName + "] nullable = false, but get null column.");
            }
            if (col != null && col.getByteSize() != 0) {
                PropertyType pType = cch.propertyType;
                switch (pType) {
                    case vertexProperty:
                    case vertexPrimaryProperty:
                        vertex.property(propName, parseColumnType(cch, col));
                        break;
                    case vertexId:
                        vertex.id(parseColumnType(cch, col));
                        break;
                    default:
                        throw new Exception("Not support Property Type " + pType);
                }
            }
        }
        return vertex;
    }

}
