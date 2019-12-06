package persistence;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.ArrayList;
import java.util.List;

public class DynamoDBManager {

    // create Execution  , Order and OrderList tables.

    // Execution has secondary key of orderId and primary key of execId .

    // Order has secondary keys of parent orderid and listId

    // list Id has secondary keys of parentListID .

    // Take the TransactionGraph and persist the individual events based on type by navigating each object in the graph - implement an iterator in the graph to help with the traversal .

    // rebuild the transaction graph from the persisted data

    // query execution by executionId
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




    public void createOrderTable()
    {
        ArrayList<AttributeDefinition> attributeDefinitions= new ArrayList<>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("orderId").withAttributeType("S"));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("parentOrderId").withAttributeType("S"));
        //   attributeDefinitions.add(new AttributeDefinition().withAttributeName("price").withAttributeType("N"));

        ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
        tableKeySchema.add(new KeySchemaElement().withAttributeName("orderId").withKeyType(KeyType.HASH));  //Partition key

        ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<>();
        indexKeySchema.add(new KeySchemaElement().withAttributeName("parentOrderId").withKeyType(KeyType.HASH));


        ArrayList<String> nonKeyAttributes = new ArrayList<String>();
        nonKeyAttributes.add("symbol");
        // nonKeyAttributes.add("execId");
        nonKeyAttributes.add("price");
        nonKeyAttributes.add("orderType");
        nonKeyAttributes.add("qty");
        nonKeyAttributes.add("listId");


        createTable("Order",attributeDefinitions,tableKeySchema,nonKeyAttributes,indexKeySchema,"OrderIndex");

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


        createTable("List",attributeDefinitions,tableKeySchema,nonKeyAttributes,indexKeySchema,"ListIndex");



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

        createTable("Execution",attributeDefinitions,tableKeySchema,nonKeyAttributes,indexKeySchema,"OrderIndex");
    }


    private void createTable(String tableName,List<AttributeDefinition> attributeDefinitions,
                             List<KeySchemaElement> tableKeySchema,List<String> nonKeyAttributes,
                             List<KeySchemaElement> indexKeySchema, String indexName)
    {

        CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName);

        createTableRequest.setAttributeDefinitions(attributeDefinitions);


        createTableRequest.setKeySchema(tableKeySchema);

         //Partition key

        Projection projection = new Projection().withProjectionType(ProjectionType.INCLUDE);


        projection.setNonKeyAttributes(nonKeyAttributes);




        GlobalSecondaryIndex localSecondaryIndex = new GlobalSecondaryIndex()
                .withIndexName(indexName).withKeySchema(indexKeySchema).withProvisionedThroughput(new ProvisionedThroughput(
                        new Long(5), new Long(5))).withProjection(projection);

        ArrayList<GlobalSecondaryIndex> globalSecondaryIndexes = new ArrayList<GlobalSecondaryIndex>();
        globalSecondaryIndexes.add(localSecondaryIndex);
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


        //dbManager.deleteTable("Order");
       // dbManager.createExecutionTable();
        //dbManager.createOrderTable();
        dbManager.createOrderListTable();
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
