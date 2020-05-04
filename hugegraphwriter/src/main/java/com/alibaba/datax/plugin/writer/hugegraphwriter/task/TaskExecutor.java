package com.alibaba.datax.plugin.writer.hugegraphwriter.task;

import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.ElemType;
import com.baidu.hugegraph.structure.GraphElement;
import com.baidu.hugegraph.structure.graph.Edge;
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

    /**
     * return error records if failure occurred. Otherwise return null.
     * @param records   List of V/E to be submitted.
     * @param type      V/E
     * @return
     */
    public List<? extends GraphElement> submitBatch(List<? extends GraphElement> records, ElemType type){
        List<GraphElement> errorRecords = new ArrayList<>();
        batchInsertTask = new BatchInsertTask(records, type);
        try {
            CompletableFuture.runAsync(batchInsertTask, batchService).get();
        } catch (Exception e){
            log.warn("Submit batch fail. batch[0]:{}", records.get(0).toString());
            for(GraphElement elem : records){
                GraphElement retElem = submitOne(elem, type);
                if(retElem != null)
                    errorRecords.add(retElem);
            }
        }
        return  null;
    }

    public GraphElement submitOne(GraphElement element, ElemType type){
        insertTask = new InsertTask(element, type);
        try{
            CompletableFuture.runAsync(insertTask, batchService).get();
        }catch (Exception e){
            return element;
        }
        return null;
    }

    public void shutdown(){
        batchService.shutdown();
    }

}
