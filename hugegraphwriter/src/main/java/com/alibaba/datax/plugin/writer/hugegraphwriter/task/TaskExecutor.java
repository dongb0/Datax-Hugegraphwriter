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


    public TaskExecutor(int threads, ElemBuilder builder, TaskPluginCollector collector){
        this.batchService = Executors.newFixedThreadPool(threads);
        this.builder = builder;
        this.collector = collector;
    }

    public void submitBatch(List<Record> records, ElemType type){
        List<GraphElement> elemRecords = new ArrayList<>(records.size());

        batchInsertTask = new BatchInsertTask(elemRecords, type);
        try {
            for(Record record : records){
                elemRecords.add(this.builder.build(record));
            }
            CompletableFuture.runAsync(batchInsertTask, batchService).get();
        } catch (Exception e){
            e.printStackTrace();
            for(Record record : records){
                try {
                    submitOne(record, type);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    collector.collectDirtyRecord(record, ex);
                }
            }
        }
    }

    public void submitOne(Record r, ElemType type) throws Exception{
        insertTask = new InsertTask(builder.build(r), type);
        try{
            CompletableFuture.runAsync(insertTask, batchService).get();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void shutdown(){
        batchService.shutdown();
    }

}
