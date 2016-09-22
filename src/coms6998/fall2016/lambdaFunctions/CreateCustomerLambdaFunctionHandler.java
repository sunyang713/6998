package coms6998.fall2016.lambdaFunctions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.managers.DynamoDBManager;
import coms6998.fall2016.managers.ValidationManager;
import coms6998.fall2016.models.Customer;
import coms6998.fall2016.models.Response;

public class CreateCustomerLambdaFunctionHandler implements RequestHandler<Customer, String> {

    @Override
    public String handleRequest(Customer input, Context context) {
        context.getLogger().log("Input: " + input);
        ValidationManager vm = new ValidationManager(context);
        
        boolean success = vm.isValidCustomer(input) && (new DynamoDBManager()).create(input);

        if(success) {
        	//return customer
        	return "{\"message\":\"Customer with email: " + input.getEmail() + " created.\"}";
        } else {
        	//throw runtime exception error w error model object
        	return "{\"message\":\"Failed to create Customer with email: " + input.getEmail() + ".\"}";
        }
    }

}

