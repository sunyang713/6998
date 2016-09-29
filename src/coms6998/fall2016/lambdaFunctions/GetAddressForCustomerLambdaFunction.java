package coms6998.fall2016.lambdaFunctions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.managers.DBManager;
import coms6998.fall2016.managers.DynamoDBManager;
import coms6998.fall2016.models.Address;
import coms6998.fall2016.models.Customer;
import coms6998.fall2016.models.DBReturnCode;
import coms6998.fall2016.models.ErrorPayload;

public class GetAddressForCustomerLambdaFunction implements RequestHandler<Customer, Address> {
	private DBManager dbManager = new DynamoDBManager();
	
	@Override
	 public Address handleRequest(Customer customer, Context context) {
		context.getLogger().log("Input: " + customer);
		DBReturnCode rc = dbManager.getCus(customer);
		
		if (rc.equals(DBReturnCode.Success)){
		 	Customer customerEntry = dbManager.getCustomer(customer.getEmail());
		 	return dbManager.getAddress(customerEntry.getAddressRef());
		} else {
			ErrorPayload errorPayload = new ErrorPayload("NotFound", 404, context.getAwsRequestId(), "Invalid email provided: " + customer.getEmail());
        	throw new RuntimeException(errorPayload.toString());
		}
	}
}
