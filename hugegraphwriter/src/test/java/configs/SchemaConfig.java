package configs;

public class SchemaConfig {
    public static String jsonConfig_1 = "{\n" +
            "                        \"host\": \"192.168.192.131\",\n" +
            "                        \"port\": 8080,\n" +
            "                        \"graph\": \"hugegraph\",\n" +
            "                        \"label\": \"user\",\n" +
            "                        \"elementType\": \"VERTEX\",\n" +
            "                        \"idStrategy\": \"CUSTOMIZE\",                    \n" +
            "                        \"column\": [\n" +
            "                            {\n" +
            "                                \"name\": \"id\",\n" +
            "                                \"value\": \"{0}\",\n" +
            "                                \"valueType\": \"int\",\n" +
            "                                \"cardinality\": \"single\",\n" +
            "                                \"propertyType\": \"vertexId\",\n" +
            "                                \"nullable\": false,\n" +
            "                                \"indexing\": true\n" +
            "                            },\n" +
            "                            {\n" +
            "                                \"name\": \"name\",\n" +
            "                                \"value\": \"{1}\",\n" +
            "                                \"valueType\": \"string\",\n" +
            "                                \"cardinality\": \"single\",\n" +
            "                                \"propertyType\": \"vertexProperty\",\n" +
            "                                \"nullable\": true\n" +
            "                            }\n" +
            "                        ]\n" +
            "                    }";

    public static String jsonConfig_2 = "{\n" +
            "                        \"host\": \"192.168.192.131\",\n" +
            "                        \"port\": 8080,\n" +
            "                        \"graph\": \"hugegraph\",\n" +
            "                        \"label\": \"knows\",\n" +
            "                        \"elementType\": \"EDGE\",\n" +
            "                        \"srcLabel\": \"user\",\n" +
            "                        \"dstLabel\": \"user\",\n" +
            "                        \"writeMode\": \"INSERT\",                    \n" +
            "                        \"idStrategy\": \"PRIMARY_KEY\",                    \n" +
            "                        \"column\": [\n" +
            "                            {\n" +
            "                                \"name\": \"src_id\",\n" +
            "                                \"value\": \"{0}\",\n" +
            "                                \"valueType\": \"int\",\n" +
            "                                \"cardinality\": \"single\",\n" +
            "                                \"propertyType\": \"srcId\",\n" +
            "                                \"nullable\": false,\n" +
            "                                \"indexing\": true\n" +
            "                            },\n" +"" +
            "                            {\n" +
            "                               \"name\": \"name\",\n" +
            "                               \"value\": \"{1}\",\n" +
            "                               \"valueType\": \"string\",\n" +
            "                               \"propertyType\": \"edgeProperty\",\n" +
            "                               \"nullable\": true\n" +
            "                            }," +
            "                            {\n" +
            "                               \"name\": \"dst_id\",\n" +
            "                               \"value\": \"{2}\",\n" +
            "                               \"valueType\": \"int\",\n" +
            "                               \"propertyType\": \"dstId\",\n" +
            "                            }" +
            "                        ]\n" +
            "                    }";

    public static String jsonConfig_TaskTestV = "{\n" +
            "                        \"host\": \"192.168.192.131\",\n" +
            "                        \"port\": 8080,\n" +
            "                        \"graph\": \"hugegraph\",\n" +
            "                        \"label\": \"task-test\",\n" +
            "                        \"elementType\": \"VERTEX\",\n" +
            "                        \"idStrategy\": \"CUSTOMIZE\",                    \n" +
            "                        \"column\": [\n" +
            "                            {\n" +
            "                                \"name\": \"id\",\n" +
            "                                \"value\": \"{0}\",\n" +
            "                                \"valueType\": \"int\",\n" +
            "                                \"cardinality\": \"single\",\n" +
            "                                \"propertyType\": \"vertexId\",\n" +
            "                                \"nullable\": false,\n" +
            "                                \"indexing\": true\n" +
            "                            },\n" +
            "                            {\n" +
            "                                \"name\": \"name\",\n" +
            "                                \"value\": \"{1}\",\n" +
            "                                \"valueType\": \"string\",\n" +
            "                                \"cardinality\": \"single\",\n" +
            "                                \"propertyType\": \"vertexProperty\",\n" +
            "                                \"nullable\": true\n" +
            "                            }\n" +
            "                        ]\n" +
            "                    }";

    public static String jsonConfig_TaskTestE = "{\n" +
            "                        \"host\": \"139.196.100.210\",\n" +
            "                        \"port\": 12345,\n" +
            "                        \"graph\": \"hugegraph\",\n" +
            "                        \"label\": \"task-test-E\",\n" +
            "                        \"elementType\": \"EDGE\",\n" +
            "                        \"srcLabel\": \"task-test\",\n" +
            "                        \"dstLabel\": \"task-test\",\n" +
            "                        \"writeMode\": \"INSERT\",                    \n" +
            "                        \"idStrategy\": \"CUSTOMIZE\",                    \n" +
            "                        \"column\": [\n" +
            "                            {\n" +
            "                                \"name\": \"src_id\",\n" +
            "                                \"value\": \"{0}\",\n" +
            "                                \"valueType\": \"int\",\n" +
            "                                \"cardinality\": \"single\",\n" +
            "                                \"propertyType\": \"srcId\",\n" +
            "                                \"nullable\": false,\n" +
            "                                \"indexing\": true\n" +
            "                            },\n" +"" +
            "                            {\n" +
            "                               \"name\": \"name\",\n" +
            "                               \"value\": \"{1}\",\n" +
            "                               \"valueType\": \"string\",\n" +
            "                               \"propertyType\": \"edgeProperty\",\n" +
            "                               \"nullable\": true\n" +
            "                            }," +
            "                            {\n" +
            "                               \"name\": \"dst_id\",\n" +
            "                               \"value\": \"{2}\",\n" +
            "                               \"valueType\": \"int\",\n" +
            "                               \"propertyType\": \"dstId\",\n" +
            "                            }" +
            "                        ]\n" +
            "                    }";
}
