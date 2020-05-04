package client;

import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.writer.hugegraphwriter.client.ClientHolder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.util.HugegraphLogger;
import com.baidu.hugegraph.client.RestClient;
import com.baidu.hugegraph.rest.RestResult;
import configs.SchemaConfig;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

public class ClientHolderTest {
    public static Configuration testConfig = Configuration.from(SchemaConfig.jsonConfig_1);

    private Logger log = HugegraphLogger.get(ClientHolderTest.class);

    @Test
    public void clientHolder(){
        log.info("\n\nClientHolder Test 1...");

        Configuration config = testConfig.clone();
        log.info("reading config :\n{}", config.toJSON());

        ClientHolder.close();
        RestClient client = ClientHolder.getClient(config);
        int port = config.getInt("port");
        String ip = config.getString("host");
        String url = "http://" +  ip + ":" + port;

        Assert.assertEquals(url, "http://localhost:8080");

        String path = "versions";
        log.info("Request to {}/{}", url, path);

        Client wsClient = ClientBuilder.newClient();
        Response res1 = wsClient.target(url).path(path).request().get();
        log.info("wsClient request to {}, get response status {}", url, res1.getStatus());

        RestResult res = client.get(path);
        log.info(res.toString());
        Assert.assertEquals(res.status(), 200);

        res = client.get("graphs/hugegraph/schema/propertykeys");
        log.info(res.toString());
        Assert.assertEquals(res.status(), 200);

        wsClient.close();
    }

}
