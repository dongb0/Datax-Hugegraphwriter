package com.alibaba.datax.plugin.writer.hugegraphwriter.builder;

import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.writer.hugegraphwriter.client.ClientHolder;
import com.alibaba.datax.plugin.writer.hugegraphwriter.constant.*;
import com.alibaba.datax.plugin.writer.hugegraphwriter.struct.ElementStruct;
import com.alibaba.datax.plugin.writer.hugegraphwriter.util.HugegraphLogger;
import com.baidu.hugegraph.driver.SchemaManager;
import com.baidu.hugegraph.client.RestClient;
import com.baidu.hugegraph.structure.schema.EdgeLabel;
import com.baidu.hugegraph.structure.schema.PropertyKey;
import com.baidu.hugegraph.structure.schema.VertexLabel;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class SchemaBuilder {

    private static Logger log = HugegraphLogger.get(SchemaBuilder.class);
    private SchemaManager schemaManager;
    private Configuration config;

    public SchemaBuilder(RestClient client, String graph, Configuration config){
        this.schemaManager = new SchemaManager(client, graph);
        this.config = config;
    }

    public SchemaBuilder(Configuration config){
        this(ClientHolder.getClient(config), config.getString(Key.GRAPH), config);
    }

    public void createSchemas(){
        createPropertySchemas();
        ElemType type = ElemType.valueOf(this.config.getString(Key.ELEMENT_TYPE));
        switch(type){
            case VERTEX:
                createVertexSchema();
                break;
            case EDGE:
                createEdgeSchema();
                break;
        }
    }

    // TODO: change return type to evaluate function execution
    public void createPropertySchemas(){
        ElemType type = ElemType.valueOf(this.config.getString(Key.ELEMENT_TYPE).toUpperCase());
        List<Configuration> configs = this.config.getListConfiguration("column");
        for(Configuration config : configs){
            createPropertySchema(config, type);
        }
    }

    public PropertyKey createPropertySchema(Configuration config, ElemType type){
        String propertyName = config.getString(Key.COLUMN_NAME);
        ValueType vType = ValueType.valueOf(config.getString(Key.COLUMN_VALUE_TYPE).toUpperCase());

        PropertyKey.Builder builder = schemaManager.propertyKey(propertyName);
        switch(vType){
            case INT:
                builder.asInt(); break;
            case LONG:
                builder.asLong(); break;
            case FLOAT:
                builder.asFloat(); break;
            case DOUBLE:
                builder.asDouble(); break;
            case STRING:
                builder.asText(); break;
            case BOOLEAN:
                builder.asBoolean(); break;
        }
        return builder.ifNotExist().create();
    }


    // TODO: use inner builder,ColumnConfigOlder to replace List<Configuration>
    public VertexLabel createVertexSchema(){
        String elemLabel = this.config.getString(Key.LABEL);
        IdStrategy idStrategy = IdStrategy.valueOf(this.config.getString(Key.ID_STRATEGY, "AUTO"));
        List<Configuration> columns = this.config.getListConfiguration(Key.COLUMN);
        List<String> propName = new ArrayList<>(), nullableProp = new ArrayList<>();
        List<String> primaryKeys = new ArrayList<>();

        for(Configuration col : columns){
            String name = col.getString(Key.COLUMN_NAME);
            PropertyType pType = PropertyType.valueOf(
                    col.getString(Key.COLUMN_PROPERTY_TYPE, "vertexProperty"));
            if(pType == PropertyType.vertexId) {
                primaryKeys.add(name);
            }
            if(pType == PropertyType.vertexProperty){
                propName.add(name);
            }

            Boolean nullable = col.getBool(Key.COLUMN_NULLABLE, false);
            if(nullable){
                nullableProp.add(name);
            }
        }
        VertexLabel.Builder builder = schemaManager.vertexLabel(elemLabel)
                .properties(propName.toArray(new String[propName.size()]))
                .nullableKeys(nullableProp.toArray(new String[nullableProp.size()]));

        // TODO  customizeId && primaryKeys should have id column; primaryKeys AT LEAST one; customize EXACTLY one
        switch(idStrategy){
            case AUTO:
                //not support yet
                break;
            case PRIMARY_KEY:
                // not support yet
                assert primaryKeys.isEmpty() == false;
                builder.primaryKeys(primaryKeys.toArray(new String[primaryKeys.size()]));
                break;
            case CUSTOMIZE:
                // TODO support string Id, Number Id, UUID
                assert primaryKeys.size() == 1: "Customize ID receives only 1 id column" ;
                builder.useCustomizeNumberId();
                break;
        }
        log.info("Create Vertex Schema -> property:{}, nullable:{}, idStrategy:{}", propName, nullableProp, idStrategy);
        return builder.ifNotExist().create();
    }

    // TODO merge createEdgeSchemas() and createVertexSchemas(), extract common part
    public EdgeLabel createEdgeSchema(){
        List<Configuration> columns = this.config.getListConfiguration(Key.COLUMN);
        List<String> propName = new ArrayList<>(), nullableProp = new ArrayList<>();
        List<String> primaryKeys = new ArrayList<>();

        // TODO read from ElemBuilder
        for(Configuration col : columns){
            String name = col.getString(Key.COLUMN_NAME);
            PropertyType pType = PropertyType.valueOf(
                    col.getString(Key.COLUMN_PROPERTY_TYPE, "edgeProperty"));

            if(pType == PropertyType.vertexId){
                primaryKeys.add(name);
            }
            if(pType == PropertyType.edgeProperty){
                propName.add(name);
            }

            Boolean nullable = col.getBool(Key.COLUMN_NULLABLE, false);
            if(nullable){
                nullableProp.add(name);
            }
        }

        String elemLabel = this.config.getString(Key.LABEL);
        String srcLabel = this.config.getString(Key.SOURCE_LABEL);
        String dstLabel = this.config.getString(Key.DESTINATION_LABEL);
        Boolean allowDuplicate = this.config.getBool(Key.EDGE_FREQUENCY, false);

        EdgeLabel.Builder builder = schemaManager.edgeLabel(elemLabel)
                .link(srcLabel, dstLabel)
                .properties(propName.toArray(new String[propName.size()]))
                .nullableKeys(nullableProp.toArray(new String[nullableProp.size()]));

        if(allowDuplicate){
            builder.multiTimes()
                    .sortKeys(primaryKeys.toArray(new String[primaryKeys.size()]));
        }
        log.info("EdgeSchema -> elemLabel: {}, srcLabel: {}, dstLabel: {}, allowDuplicate: {}", elemLabel, srcLabel, dstLabel, allowDuplicate);
        return builder.ifNotExist().create();
    }

    //TODO get***Shema()

    public void removePropertyKeys(){
        List<PropertyKey> propertyKeys = this.schemaManager.getPropertyKeys();
        for(PropertyKey pk: propertyKeys){
            removePropertyKey(pk.name());
        }
    }

    public void removePropertyKey(String name){
        this.schemaManager.removePropertyKey(name);
    }

    public void removePropertyKeyIfExist(String name){
        PropertyKey property = this.schemaManager.getPropertyKey(name);
        if(property.name().equals(name)){
            removePropertyKey(name);
        }
    }

    public void removeVertexSchemas(){
        List<VertexLabel> vertexLabels = this.schemaManager.getVertexLabels();
        for(VertexLabel vl : vertexLabels){
            removeVertexSchema(vl.name());
        }
    }

    public void removeVertexSchema(String name){
        this.schemaManager.removeVertexLabel(name);
    }

}
