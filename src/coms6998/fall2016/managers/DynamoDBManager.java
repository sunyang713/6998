package coms6998.fall2016.managers;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import coms6998.fall2016.models.Customer;

public class DynamoDBManager {
	
	private static final String CUSTOMER_TABLE = "customer";
	
	private AmazonDynamoDBClient dynamoDBClient;
	private DynamoDB dynamoDB;
	private DynamoDBMapper mapper;
	private Table customerTable;
	private Table addressTable;
	
	public DynamoDBManager() {
		dynamoDBClient = new AmazonDynamoDBClient();
		dynamoDBClient.setRegion(Region.getRegion(Regions.US_EAST_1));
		mapper = new DynamoDBMapper(dynamoDBClient);
	    dynamoDB = new DynamoDB(dynamoDBClient);
	    customerTable = dynamoDB.getTable(CUSTOMER_TABLE);
	}
	
	public boolean deleteCustomerUsingMapper(Customer emailKey){
		Customer customerToDelete = mapper.load(emailKey);
		if(customerToDelete != null) {
			customerToDelete.setDeleted(true);
			mapper.save(customerToDelete);
			return true;
		}
		return false;
	}
	
	public boolean deleteCustomer(String email) {
		try {

            Map<String, String> expressionAttributeNames = new HashMap<String, String>();
            expressionAttributeNames.put("#na", "isDeleted");

            UpdateItemSpec updateItemSpec = new UpdateItemSpec()
            .withPrimaryKey("email", email)
            .withUpdateExpression("set #na = :val1")
            .withNameMap(new NameMap()
                .with("#na", "isDeleted"))
            .withValueMap(new ValueMap()
                .withBoolean(":val1", true))
            .withReturnValues(ReturnValue.ALL_NEW);

            UpdateItemOutcome outcome =  customerTable.updateItem(updateItemSpec);

            // Check the response.
            System.out.println("Printing item after adding new attribute...");
            System.out.println(outcome.getItem().toJSONPretty());           

        }   catch (Exception e) {
            System.err.println("Failed to add new attribute in " + CUSTOMER_TABLE);
            System.err.println(e.getMessage());
            return false;
        }        
		return true;
	}

}
