package com.alibaba.datax.plugin.writer.hugegraphwriter.struct;

public class EdgeStruct extends ElementStruct{

    private String srcLabel = null;
    private String dstLabel = null;

    public String getSrcLabel() {
        return srcLabel;
    }

    public void setSrcLabel(String srcLabel) {
        this.srcLabel = srcLabel;
    }

    public String getDstLabel() {
        return dstLabel;
    }

    public void setDstLabel(String dstLabel) {
        this.dstLabel = dstLabel;
    }

    @Override
    public String toString() {
        return "EdgeStruct{" +
                "srcLabel='" + srcLabel + '\'' +
                ", dstLabel='" + dstLabel + '\'' +
                ", label='" + label + '\'' +
                ", columnsProperties=" + columnsProperties +
                '}';
    }
}
