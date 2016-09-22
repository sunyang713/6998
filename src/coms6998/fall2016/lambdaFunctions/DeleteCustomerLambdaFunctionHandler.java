package coms6998.fall2016.lambdaFunctions;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.managers.DynamoDBManager;

public class DeleteCustomerLambdaFunctionHandler implements RequestHandler<String, String> {
	
    @Override
    public String handleRequest(String email, Context context) {
        context.getLogger().log("Deleting customer with email: " + email);
        boolean rval = (new DynamoDBManager()).deleteCustomerUsingMapper(email);
        if(rval) {
        	return "{\"message\":\"Customer with email: " + email + " deleted.\"}";
        } else {
        	return "{\"message\":\"Failed to delete Customer with email: " + email + ".\"}";
        }
    }

}
