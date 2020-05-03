package com.alibaba.datax.plugin.writer.hugegraphwriter.task;

import com.alibaba.datax.plugin.writer.hugegraphwriter.client.ClientHolder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.ElemType;
import com.baidu.hugegraph.driver.GraphManager;
import com.baidu.hugegraph.structure.GraphElement;
import com.baidu.hugegraph.structure.graph.Edge;
import com.baidu.hugegraph.structure.graph.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class InsertTask implements Runnable{

    private GraphElement element;
    private GraphManager graphManager;
    private ElemType type;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    InsertTask(GraphElement elem, ElemType type){
        this.element = elem;
        this.graphManager = ClientHolder.getGraphManager();
        this.type = type;
    }

    @Override
    public void run() {
        if(type == ElemType.VERTEX){
            graphManager.addVertex((Vertex) element);
        }
        else{
            graphManager.addEdge((Edge) element);
        }
    }
}
