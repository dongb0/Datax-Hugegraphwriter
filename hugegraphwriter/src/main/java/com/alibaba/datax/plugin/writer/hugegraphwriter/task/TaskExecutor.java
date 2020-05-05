package com.alibaba.datax.plugin.writer.hugegraphwriter.task;

import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.plugin.TaskPluginCollector;
import com.alibaba.datax.plugin.writer.hugegraphwriter.builder.ElemBuilder;
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
    ElemBuilder builder;
    TaskPluginCollector collector;
    Logger log = LoggerFactory.getLogger(this.getClass());

//    public TaskExecutor(int threads){
//        batchService = Executors.newFixedThreadPool(threads);
//    }

    public TaskExecutor(int threads, ElemBuilder builder){
        this.batchService = Executors.newFixedThreadPool(threads);
        this.builder = builder;
    }

    public List<Record> submitBatch(List<Record> records, ElemType type){
        List<Record> errorRecords = null;
        List<GraphElement> elemRecords = new ArrayList<>(records.size());
        for(Record record : records)
            elemRecords.add(this.builder.build(record));

        batchInsertTask = new BatchInsertTask(elemRecords, type);
        try {
            CompletableFuture.runAsync(batchInsertTask, batchService).get();
        } catch (Exception e){
            e.printStackTrace();
            errorRecords = new ArrayList<>();
            for(Record record : records){
                Record retElem = submitOne(record, type);
                if(retElem != null)
                    errorRecords.add(retElem);
            }
            log.warn("Submit batch fail. Failed record(s) size:{}\n{}", errorRecords.size(), errorRecords);
        }
        return errorRecords;
    }

    public Record submitOne(Record r, ElemType type){
        Record elem = null;
        insertTask = new InsertTask(builder.build(r), type);
        try{
            CompletableFuture.runAsync(insertTask, batchService).get();
        }catch (Exception e){
            e.printStackTrace();
            elem = r;
        }
        return elem;
    }

    public void shutdown(){
        batchService.shutdown();
    }

}
