package com.alibaba.datax.plugin.writer.hugegraphwriter.struct;

import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.IdStrategy;

public class VertexStruct extends  ElementStruct{
    private IdStrategy idStrategy = IdStrategy.CUSTOMIZE;

    public IdStrategy getIdStrategy() {
        return idStrategy;
    }

    public void setIdStrategy(IdStrategy idStrategy) {
        this.idStrategy = idStrategy;
    }

    @Override
    public String toString() {
        return "VertexStruct{" +
                "idStrategy=" + idStrategy +
                ", label='" + label + '\'' +
                ", columnsProperties=" + columnsProperties.toString() +
                '}';
    }
}
