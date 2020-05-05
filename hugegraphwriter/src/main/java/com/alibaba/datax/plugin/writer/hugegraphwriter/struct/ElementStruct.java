package com.alibaba.datax.plugin.writer.hugegraphwriter.struct;

import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.CardinalityType;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.PropertyType;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.ValueType;
import com.alibaba.datax.plugin.writer.hugegraphwriter.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementStruct {
    protected String label = null;

    public List<Pair<String, ColumnsConfHolder>> getColumnsProperties() {
        return columnsProperties;
    }

    public void setColumnsProperties(List<Pair<String, ColumnsConfHolder>> columnsProperties) {
        this.columnsProperties = columnsProperties;
    }

    protected List<Pair<String, ColumnsConfHolder>> columnsProperties = new ArrayList<>();

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static class ColumnsConfHolder{
        public String value;
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
