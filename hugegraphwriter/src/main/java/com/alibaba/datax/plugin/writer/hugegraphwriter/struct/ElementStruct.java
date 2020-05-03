package com.alibaba.datax.plugin.writer.hugegraphwriter.struct;

import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.CardinalityType;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.IdStrategy;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.PropertyType;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.ValueType;

import java.util.HashMap;
import java.util.Map;

public class ElementStruct {
    protected String label = null;
    protected Map<String, ColumnsConfHolder> columnsProperties = new HashMap<>();

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Map<String, ColumnsConfHolder> getProperties() {
        return columnsProperties;
    }

    public void setProperties(Map<String, ColumnsConfHolder> properties) {
        this.columnsProperties = properties;
    }

    public static class ColumnsConfHolder{
        public Object value;
        public ValueType valueType;
        public CardinalityType cardinalityType;
        public PropertyType propertyType;
        public Boolean nullable;
        public Boolean indexing;

        @Override
        public String toString() {
            return "ColumnsConfHolder{" +
                    "value=" + value +
                    ", valueType=" + valueType +
                    ", cardinalityType=" + cardinalityType +
                    ", propertyType=" + propertyType +
                    ", nullable=" + nullable +
                    ", indexing=" + indexing +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ElementStruct{" +
                "label='" + label + '\'' +
                ", columnsProperties=" + columnsProperties.toString() +
                '}';
    }
}
