package configs;

public class SchemaConfig {
    public static String jsonConfig_1 = "{\n" +
            "                        \"host\": \"localhost\",\n" +
            "                        \"port\": 8080,\n" +
            "                        \"graph\": \"hugegraph\",\n" +
            "                        \"label\": \"InsertTestV_1\",\n" +
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
            "                        \"host\": \"localhost\",\n" +
            "                        \"port\": 8080,\n" +
            "                        \"graph\": \"hugegraph\",\n" +
            "                        \"label\": \"InsertTestE_1\",\n" +
            "                        \"elementType\": \"EDGE\",\n" +
            "                        \"srcLabel\": \"InsertTestV_1\",\n" +
            "                        \"dstLabel\": \"InsertTestV_1\",\n" +
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
            "                        \"host\": \"localhost\",\n" +
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
            "                        \"host\": \"localhost\",\n" +
            "                        \"port\": 8080,\n" +
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

    public static String jsonConfig_Schema_PrimaryKeysV = "{\n" +
            "                        \"host\": \"localhost\",\n" +
            "                        \"port\": 8080,\n" +
            "                        \"graph\": \"hugegraph\",\n" +
            "                        \"label\": \"schema_testV\",\n" +
            "                        \"elementType\": \"VERTEX\",\n" +
            "                        \"idStrategy\": \"PRIMARY_KEY\",\n" +
            "                        \"column\": [\n" +
            "                            {\n" +
            "                                \"name\": \"id\",\n" +
            "                                \"value\": \"{0}\",\n" +
            "                                \"valueType\": \"int\",\n" +
            "                                \"cardinality\": \"single\",\n" +
            "                                \"propertyType\": \"vertexPrimaryProperty\",\n" +
            "                                \"indexing\": true\n" +
            "                            },\n" +
            "                            {\n" +
            "                                \"name\": \"name\",\n" +
            "                                \"value\": \"{1}\",\n" +
            "                                \"valueType\": \"string\",\n" +
            "                                \"cardinality\": \"single\",\n" +
            "                                \"propertyType\": \"vertexPrimaryProperty\",\n" +
            "                                \"nullable\": false\n" +
            "                            },\n" +
            "                            {\n" +
            "                                \"name\": \"age\",\n" +
            "                                \"value\": \"{2}\",\n" +
            "                                \"valueType\": \"int\",\n" +
            "                                \"cardinality\": \"single\",\n" +
            "                                \"propertyType\": \"vertexProperty\",\n" +
            "                                \"nullable\": true\n" +
            "                            }\n" +
            "                        ]\n" +
            "                    }";

    public static String jsonConfig_Schema_PrimaryKeysE= "{\n" +
            "                        \"host\": \"localhost\",\n" +
            "                        \"port\": 8080,\n" +
            "                        \"graph\": \"hugegraph\",\n" +
            "                        \"label\": \"schema_testE\",\n" +
            "                        \"elementType\": \"EDGE\",\n" +
            "                        \"srcLabel\": \"schema_testV\",\n" +
            "                        \"dstLabel\": \"schema_testV\",\n" +
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

}
