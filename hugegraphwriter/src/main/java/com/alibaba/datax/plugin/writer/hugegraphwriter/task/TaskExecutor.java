package com.alibaba.datax.plugin.writer.hugegraphwriter.task;

import com.alibaba.datax.plugin.writer.hugegraphwriter.client.ClientHolder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.ElemType;
import com.baidu.hugegraph.driver.GraphManager;
import com.baidu.hugegraph.structure.GraphElement;
import com.baidu.hugegraph.structure.graph.Edge;
import com.baidu.hugegraph.structure.graph.Graph;
import com.baidu.hugegraph.structure.graph.Vertex;
import org.h2.command.dml.Insert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskExecutor {

    ExecutorService batchService;
    BatchInsertTask batchInsertTask;
    InsertTask insertTask;
    Logger log = LoggerFactory.getLogger(this.getClass());

    public TaskExecutor(int threads){
        batchService = Executors.newFixedThreadPool(threads);
    }

    public List<GraphElement> submitBatch(List<? extends GraphElement> records, ElemType type){
        List<GraphElement> errorRecords = null;
        batchInsertTask = new BatchInsertTask(records, type);
        try {
            CompletableFuture.runAsync(batchInsertTask, batchService).get();
        } catch (Exception e){
            e.printStackTrace();
            errorRecords = new ArrayList<>();
            for(GraphElement elem : records){
                GraphElement retElem = submitOne(elem, type);
                if(retElem != null)
                    errorRecords.add(retElem);
            }
            log.warn("Submit batch fail. Failed record(s) size:{}\n{}", errorRecords.size(), errorRecords);
        }
        return errorRecords;
    }

    public GraphElement submitOne(GraphElement element, ElemType type){
        GraphElement elem = null;
        insertTask = new InsertTask(element, type);
        try{
            CompletableFuture.runAsync(insertTask, batchService).get();
        }catch (Exception e){
            e.printStackTrace();
            elem = element;
        }
        return elem;
    }

    public void shutdown(){
        batchService.shutdown();
    }

}
