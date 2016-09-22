package coms6998.fall2016.lambdaFunctions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
//import com.google.gson.Gson;

import coms6998.fall2016.managers.DynamoDBManager;
import coms6998.fall2016.models.Customer;
import coms6998.fall2016.models.Response;

public class CreateCustomerLambdaFunctionHandler implements RequestHandler<Customer, String> {

    @Override
    public String handleRequest(Customer input, Context context) {
        context.getLogger().log("Input: " + input);
        
        //Gson myGson = new Gson();
        
        //Response obj = null;
        
        boolean success = (new DynamoDBManager()).createCustomer(input);

        if(success) {
        	return "{\"message\":\"Customer with email: " + input.getEmail() + " created.\"}";
        } else {
        	return "{\"message\":\"Failed to create Customer with email: " + input.getEmail() + ".\"}";
        }
    }

}

