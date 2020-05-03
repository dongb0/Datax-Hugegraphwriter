package com.alibaba.datax.plugin.writer.hugegraphwriter.task;

import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.ElemType;
import com.baidu.hugegraph.structure.GraphElement;
import org.h2.command.dml.Insert;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskExecutor {

    ExecutorService batchService;
    BatchInsertTask batchInsertTask;
    InsertTask insertTask;

    public TaskExecutor(int threads){
        batchService = Executors.newFixedThreadPool(threads);
    }

    public void submitBatch(List<? extends GraphElement> records, ElemType type){

        batchInsertTask = new BatchInsertTask(records, type);
        try {
            CompletableFuture.runAsync(batchInsertTask, batchService).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //TODO add failed record
    public void submitOne(GraphElement element, ElemType type){
        insertTask = new InsertTask(element, type);

        try{
            CompletableFuture.runAsync(insertTask, batchService).get();
        }catch (Exception e){

            e.printStackTrace();
        }

    }

    public void shutdown(){
        batchService.shutdown();
    }

}
