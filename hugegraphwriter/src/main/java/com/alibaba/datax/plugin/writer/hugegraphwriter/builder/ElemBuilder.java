package com.alibaba.datax.plugin.writer.hugegraphwriter.builder;

import com.alibaba.datax.common.element.Column;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.*;
import com.alibaba.datax.plugin.writer.hugegraphwriter.struct.EdgeStruct;
import com.alibaba.datax.plugin.writer.hugegraphwriter.struct.ElementStruct;
import com.alibaba.datax.plugin.writer.hugegraphwriter.struct.VertexStruct;
import com.alibaba.datax.plugin.writer.hugegraphwriter.util.HugegraphLogger;
import com.baidu.hugegraph.structure.GraphElement;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ElemBuilder <GE extends GraphElement, ES extends ElementStruct >{

    Logger log = HugegraphLogger.get(ElemBuilder.class);
    protected ES struct;

    public abstract GE build(Record record);

    protected int parseColumnIndex(String name){

        String valStr = (String) struct.getProperties().get(name).value;
        return Integer.parseInt(valStr.substring(1, valStr.length() - 1));
    }

    protected Object parseColumnType(ElementStruct.ColumnsConfHolder cch, Column col){
        switch (cch.valueType){
            case INT:
            case LONG:
                return col.asLong();
            case FLOAT:
            case DOUBLE:
                return col.asDouble();
            case STRING:
                return col.asString();
            case BOOLEAN:
                return col.asBoolean();
        }
        return null;
    }

    protected ES createStruct(Configuration config){
        ES struct = null;

        ElemType elemType = ElemType.valueOf(
                config.getString(Key.ELEMENT_TYPE).toUpperCase());
        if(elemType == ElemType.VERTEX){
            struct = (ES)new VertexStruct();
            ((VertexStruct) struct).setIdStrategy(
                    IdStrategy.valueOf(config.getString(Key.ID_STRATEGY)));
        }
        else{
            struct = (ES)new EdgeStruct();
            ((EdgeStruct) struct).setSrcLabel(
                    config.getString(Key.SOURCE_LABEL));
            ((EdgeStruct) struct).setDstLabel(
                    config.getString(Key.DESTINATION_LABEL));

        }
        struct.setLabel(config.getString(Key.LABEL));

        // TODO parse index might support prefix later
        Map<String, ElementStruct.ColumnsConfHolder> properties = new HashMap<>();
        List<Configuration> columnConfigs = config.getListConfiguration(Key.COLUMN);
        for(Configuration conf: columnConfigs) {
            ElementStruct.ColumnsConfHolder col = new ElementStruct.ColumnsConfHolder();
            col.value = conf.getString(Key.COLUMN_VALUE);
            col.valueType = ValueType.valueOf(conf.getString(Key.COLUMN_VALUE_TYPE).toUpperCase());
            col.cardinalityType = CardinalityType.valueOf(conf.getString(Key.COLUMN_CARDINALITY, "single").toUpperCase());
            col.propertyType = PropertyType.valueOf(conf.getString(Key.COLUMN_PROPERTY_TYPE));
            col.nullable = conf.getBool(Key.COLUMN_NULLABLE, false);
            col.indexing = conf.getBool(Key.COLUMN_INDEXING, true);
            properties.put(conf.getString(Key.COLUMN_NAME), col);
        }
        struct.setProperties(properties);

        return struct;
    }




}
