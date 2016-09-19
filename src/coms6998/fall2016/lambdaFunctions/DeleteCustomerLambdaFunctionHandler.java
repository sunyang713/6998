package coms6998.fall2016.lambdaFunctions;


import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.models.Customer;

public class DeleteCustomerLambdaFunctionHandler implements RequestHandler<Customer, String> {

    static AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(new BasicAWSCredentials("AKIAIYM2CT2BA3SAR3TA", "z2eGDpVsW0B/b5N5eoi/FNqp+NqqOr6s06vmvNlR"));
	
    @Override
    public String handleRequest(Customer input, Context context) {
        context.getLogger().log("Input is very much: " + input);
//        dynamoDBClient.setEndpoint("https://dynamodb.us-west-2.amazonaws.com");
//        DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);
//        Table customerTable = dynamoDB.getTable("customer");
//        context.getLogger().log("Customer Table Description: " + customerTable.getTableName());
//
//        Item item = new Item().withPrimaryKey("email", "test@gmail.com");
//        
//        customerTable.putItem(item);
       
       
        return "{\"message\":\"Hello from delete function " + input.getFirstName() + "!, how are you?\"}";
    }

}
