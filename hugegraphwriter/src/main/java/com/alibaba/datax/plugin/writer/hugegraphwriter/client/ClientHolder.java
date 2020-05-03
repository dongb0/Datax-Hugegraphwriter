package com.alibaba.datax.plugin.writer.hugegraphwriter.client;


import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.Key;
import com.alibaba.datax.plugin.writer.hugegraphwriter.util.HugegraphLogger;
import com.baidu.hugegraph.client.RestClient;
import com.baidu.hugegraph.driver.GraphManager;
import org.slf4j.Logger;

import java.text.MessageFormat;

public class ClientHolder {

    private static Logger log = HugegraphLogger.get(ClientHolder.class);
    private static volatile RestClient client = null;
    private static GraphManager graphManager = null;

    public static RestClient getClient(Configuration config) {
        int port = config.getInt(Key.PORT);
        String host = config.getString(Key.HOST);
        String graph = config.getString(Key.GRAPH);
        return getClient(host, port, graph);
    }

    public static RestClient getClient(String host, int port, String graphName){
        if(client == null){
            synchronized (ClientHolder.class){
                String url = MessageFormat.format("http://{0}:{1}", host, Integer.toString(port));
                log.info("RestClient url: {}, graph name: {}", url, graphName);
                client = new RestClient(url, 30);
                graphManager = new GraphManager(client, graphName);
            }
        }
        return client;
    }

    public static GraphManager getGraphManager() {
        return graphManager;
    }

    public static void setGraphManager(GraphManager graphManager) {
        ClientHolder.graphManager = graphManager;
    }

    public static void close() {
        if(client != null){
            client.close();
            client = null;
        }
    }
}
