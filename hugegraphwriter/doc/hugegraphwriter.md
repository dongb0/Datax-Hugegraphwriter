        {
          "name": "hugegraphwriter",
          "parameter": {
            "host": "139.196.100.210",
            "port": 12345,
            "graph": "name  //hugegraph, mygraph, etc.",
            "label": "test-label  //element label",
            "elementType": "EDGE/VERTEX",
            "srcLabel": "test-srcLabel-     //must contain value if elementType is EDGE, support read from reader?",
            "dstLabel": "test-dstLabel-     ",
            "allowDuplicateEdge": false
            "writeMode": "INSERT, UPDATE(what to do when same id appears, append? rewrite?), SKIP       1)insert",
        
            "idStrategy": "1)AUTO, 2)PRIMARY_KEY (user specify properties to concatenate as primary key, then AT LEAST ONE id in column is required), 3)CUSTOMIZE",
        
            "column": [
              {
                "name": "id  //property name",
                "value": "{0},{1}..., only string type are allowed to concatenate, e.g.,prefix-{0}-suffix",
                "valueType": "int, long, float, double, string, boolean",
                "cardinality": "single list set  //default single",
                "propertyType": "vertexId, vertexProperty, edgeProperty, srcId, dstId // if not set, fill in V/E property according to elementType; At least 1 id column is required(pending",
                "nullable": "true/false  //can not be id, default false",
                "indexing": "true:boolean/false  //TODO //default true, set false will disable global search for specific V/E"
              },
              {
                "name": "age",
                "value": "xx",
                "valueType": "int",
                "propertyType": "vertexProperty",
              }
            ]
          }
        }
        
        
        
