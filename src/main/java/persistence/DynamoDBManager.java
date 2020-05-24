package persistence;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.*;
import event.Event;
import event.Execution;
import event.Order;
import graph.TransactionGraph;

import java.util.ArrayList;
import java.util.List;

public class DynamoDBManager {



    // Take the TransactionGraph and persist the individual events based on type by navigating each object in the graph - implement an iterator in the graph to help with the traversal .

    // rebuild the transaction graph from the persisted data

    //
    // query order by orderId
    // query orders by parentorderid
    // query orders by listId
    // query list slices by listid

    public static AmazonDynamoDB getHandle()
    {
        AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
                .build();

        return ddb;

    }

    final DynamoDBMapper mapper ;


    public DynamoDBManager() {
        final AmazonDynamoDB ddb = getHandle();
        mapper = new DynamoDBMapper(ddb);

    }

    private void deleteTable(String tableName)
    {

        final AmazonDynamoDB ddb = getHandle();


        DeleteTableResult result = ddb.deleteTable(tableName);

        System.out.println(result);

    }

    //query execution by executionId


    public void putExecution(Execution execution)
    {
        ExecutionRecord record = new ExecutionRecord(execution);
        mapper.save(record);
    }

    public Execution getExecution(String execId)
    {
        ExecutionRecord executionRecord = mapper.load(ExecutionRecord.class , execId);
        return Execution.createExecution(executionRecord);
    }

    public void cleanAllRecords(Class<?> clazz)
    {
    /*    final AmazonDynamoDB ddb = getHandle();
        DynamoDBMapper mapper = new DynamoDBMapper(ddb);*/

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        var scanResult = mapper.scan(clazz, scanExpression);

        scanResult.stream().forEach(r-> {
            mapper.delete(r);
            System.out.println("Deleted " + r);
        });
    }


    public void putOrder(Order order)
    {

    }



    public void createOrderTable()
    {
        ArrayList<AttributeDefinition> attributeDefinitions= new ArrayList<>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("orderId").withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("parentOrderId").withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("listId").withAttributeType("S"));
        //   attributeDefinitions.add(new AttributeDefinition().withAttributeName("price").withAttributeType("N"));

        ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
        tableKeySchema.add(new KeySchemaElement().withAttributeName("orderId").withKeyType(KeyType.HASH));  //Partition key



        ArrayList<String> nonKeyAttributes = new ArrayList<String>();
        nonKeyAttributes.add("symbol");
        // nonKeyAttributes.add("execId");
        nonKeyAttributes.add("price");
        nonKeyAttributes.add("orderType");
        nonKeyAttributes.add("qty");
        nonKeyAttributes.add("listId");

        ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<>();
        indexKeySchema.add(new KeySchemaElement().withAttributeName("parentOrderId").withKeyType(KeyType.HASH));


        IndexHolder holder = new IndexHolder();
        holder.setIndexKeySchema(indexKeySchema);
        holder.setIndexName("OrderIndex");

        ArrayList<KeySchemaElement> indexKeySchema1 = new ArrayList<>();
        indexKeySchema1.add(new KeySchemaElement().withAttributeName("listId").withKeyType(KeyType.HASH));


        IndexHolder holder1 = new IndexHolder();
        holder1.setIndexKeySchema(indexKeySchema1);
        holder1.setIndexName("ListIndex");




        createTable("Order",attributeDefinitions,tableKeySchema,nonKeyAttributes,List.of(holder,holder1));

    }

    public void createOrderListTable()
    {
/*
  String  ;
    String ;

 */
        ArrayList<AttributeDefinition> attributeDefinitions= new ArrayList<>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("listId").withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("parentListId").withAttributeType("S"));

        ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
        tableKeySchema.add(new KeySchemaElement().withAttributeName("listId").withKeyType(KeyType.HASH));  //Partition key

        ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<>();
        indexKeySchema.add(new KeySchemaElement().withAttributeName("parentListId").withKeyType(KeyType.HASH));


        ArrayList<String> nonKeyAttributes = new ArrayList<String>();
        nonKeyAttributes.add("orderids");


        IndexHolder holder = new IndexHolder();
        holder.setIndexKeySchema(indexKeySchema);
        holder.setIndexName("ListIndex");

        createTable("List",attributeDefinitions,tableKeySchema,nonKeyAttributes,List.of(holder));



    }


    public void createExecutionTable()
    {
        ArrayList<AttributeDefinition> attributeDefinitions= new ArrayList<>();
        //    attributeDefinitions.add(new AttributeDefinition().withAttributeName("symbol").withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("execId").withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("orderId").withAttributeType("S"));
        //   attributeDefinitions.add(new AttributeDefinition().withAttributeName("price").withAttributeType("N"));

        ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
        tableKeySchema.add(new KeySchemaElement().withAttributeName("execId").withKeyType(KeyType.HASH));  //Partition key

        ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<>();
        indexKeySchema.add(new KeySchemaElement().withAttributeName("orderId").withKeyType(KeyType.HASH));


        ArrayList<String> nonKeyAttributes = new ArrayList<String>();
        nonKeyAttributes.add("symbol");
        // nonKeyAttributes.add("execId");
        nonKeyAttributes.add("price");

        IndexHolder holder = new IndexHolder();
        holder.setIndexKeySchema(indexKeySchema);
        holder.setIndexName("OrderIndex");

        createTable("Execution",attributeDefinitions,tableKeySchema,nonKeyAttributes,List.of(holder));
    }

    public void persist(TransactionGraph graph) {

        // look at each type of object and create a record of that type and save it in the table .

        graph.nodes.values().stream().forEach(node -> {

            Event event  = node.getEvent();

            if (event instanceof Execution)
            {
                putExecution((Execution)event);
            }

        });
    }



    class IndexHolder
    {
        List<KeySchemaElement> indexKeySchema ;
        String indexName;

        public List<KeySchemaElement> getIndexKeySchema() {
            return indexKeySchema;
        }

        public void setIndexKeySchema(List<KeySchemaElement> indexKeySchema) {
            this.indexKeySchema = indexKeySchema;
        }

        public String getIndexName() {
            return indexName;
        }

        public void setIndexName(String indexName) {
            this.indexName = indexName;
        }
    }


    private void createTable(String tableName,List<AttributeDefinition> attributeDefinitions,
                             List<KeySchemaElement> tableKeySchema,List<String> nonKeyAttributes,
                             List<IndexHolder> indexHolders )
                          //   List<KeySchemaElement> indexKeySchema, String indexName)
    {

        CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName);

        createTableRequest.setAttributeDefinitions(attributeDefinitions);


        createTableRequest.setKeySchema(tableKeySchema);

         //Partition key

        Projection projection = new Projection().withProjectionType(ProjectionType.INCLUDE);


        projection.setNonKeyAttributes(nonKeyAttributes);

        ArrayList<GlobalSecondaryIndex> globalSecondaryIndexes = new ArrayList<GlobalSecondaryIndex>();

        indexHolders.stream().forEach(holder-> {
            String indexName = holder.getIndexName();
            List<KeySchemaElement> indexKeySchema = holder.getIndexKeySchema();

            GlobalSecondaryIndex localSecondaryIndex = new GlobalSecondaryIndex()
                    .withIndexName(indexName).withKeySchema(indexKeySchema).withProvisionedThroughput(new ProvisionedThroughput(
                            new Long(5), new Long(5))).withProjection(projection);

            globalSecondaryIndexes.add(localSecondaryIndex);

        });




        createTableRequest.setGlobalSecondaryIndexes(globalSecondaryIndexes);

        createTableRequest.setProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits((long)5).withWriteCapacityUnits((long)5));


        final AmazonDynamoDB ddb = getHandle();

        try {
            CreateTableResult result = ddb.createTable(createTableRequest);
            System.out.println(result);
        } catch (AmazonServiceException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Done!");
    }




    public static void main(String[] args) {

        DynamoDBManager dbManager = new DynamoDBManager();

        dbManager.cleanAllRecords(ExecutionRecord.class);



      /*  dbManager.deleteTable("Order");
        dbManager.deleteTable("List");
        dbManager.deleteTable("Execution");



        dbManager.createExecutionTable();
        dbManager.createOrderTable();
        dbManager.createOrderListTable();*/
    }

}

/*
 AmazonDynamoDB ddb;


    static AmazonDynamoDB getHandle()
    {
        AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
                .build();

        return ddb;

    }

    DynamoDBMapper mapper;

    static DynamoDB dynamoDB ;


    public IDPDynamoDBPersistenceManager()
    {
        ddb = getHandle();
        mapper = new DynamoDBMapper(ddb);

        dynamoDB  = new DynamoDB(ddb);


    }
 */
