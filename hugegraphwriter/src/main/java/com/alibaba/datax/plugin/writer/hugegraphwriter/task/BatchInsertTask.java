package com.alibaba.datax.plugin.writer.hugegraphwriter.task;

import com.alibaba.datax.plugin.writer.hugegraphwriter.client.ClientHolder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.ElemType;
import com.alibaba.datax.plugin.writer.hugegraphwriter.util.HugegraphLogger;
import com.baidu.hugegraph.HugeGraph;
import com.baidu.hugegraph.driver.GraphManager;
import com.baidu.hugegraph.structure.GraphElement;
import com.baidu.hugegraph.structure.graph.Edge;
import com.baidu.hugegraph.structure.graph.Vertex;
import org.slf4j.Logger;

import java.util.List;

public class BatchInsertTask implements Runnable {

    List<? extends GraphElement> records;
    ElemType type;
    GraphManager graphManager;
    Logger log = HugegraphLogger.get(BatchInsertTask.class);

    BatchInsertTask(List<? extends GraphElement> records, ElemType type){
        this.records = records;
        this.type = type;
        this.graphManager = ClientHolder.getGraphManager();
    }

    @Override
    public void run() {
        if(type == ElemType.VERTEX){
            graphManager.addVertices((List<Vertex>) records);
        }
        else{
            graphManager.addEdges((List<Edge>) records);
        }
    }
}
