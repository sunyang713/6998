package coms6998.fall2016.lambdaFunctions;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.managers.DBManager;
import coms6998.fall2016.managers.DynamoDBManager;
import coms6998.fall2016.models.Customer;

public class DeleteCustomerLambdaFunctionHandler implements RequestHandler<Customer, String> {
	
	private DBManager dbManager = new DynamoDBManager();
	
    @Override
    public String handleRequest(Customer customer, Context context) {
        context.getLogger().log("Deleting customer with email: " + customer.getEmail());
        boolean rval = dbManager.deleteCustomer(customer);
        if(rval) {
        	return "{\"message\":\"Customer with email: " + customer.getEmail() + " deleted.\"}";
        } else {
        	return "{\"message\":\"Failed to delete Customer with email: " + customer.getEmail() + ".\"}";
        }
    }

}
