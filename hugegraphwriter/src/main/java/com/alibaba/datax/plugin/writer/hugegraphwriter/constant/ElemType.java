package com.alibaba.datax.plugin.writer.hugegraphwriter.constant;

public enum ElemType {
    VERTEX("vertex"),
    EDGE("edge");

    String name;
    ElemType(String name) { this.name = name; }

    public String string(){ return this.name; }
}
