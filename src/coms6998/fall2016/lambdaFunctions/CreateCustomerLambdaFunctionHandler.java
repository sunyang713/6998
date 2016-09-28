package coms6998.fall2016.lambdaFunctions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.managers.DBManager;
import coms6998.fall2016.managers.DynamoDBManager;
import coms6998.fall2016.managers.ValidationManager;
import coms6998.fall2016.models.Customer;
import coms6998.fall2016.models.DBReturnCode;
import coms6998.fall2016.models.ErrorPayload;

public class CreateCustomerLambdaFunctionHandler implements RequestHandler<Customer, String> {
	
	private DBManager dbManager = new DynamoDBManager();

    @Override
    public String handleRequest(Customer input, Context context) {
        context.getLogger().log("Input: " + input);
        ValidationManager vm = new ValidationManager(context);
        if(vm.isValidCustomer(input)){
	        DBReturnCode rc = dbManager.create(input);
	        if(rc.equals(DBReturnCode.Success)) {
	        	//return customer
	        	return "{\"message\":\"Customer with email: " + input.getEmail() + " created.\"}";
	        } else if (rc.equals(DBReturnCode.AlreadyExists)){
	        	ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), "Customer with provided email already exists: " + input.getEmail() + ".");
	        	throw new RuntimeException(errorPayload.toString());
	        } else {
	        	ErrorPayload errorPayload = new ErrorPayload("Error", 500, context.getAwsRequestId(), "Unknown Internal Error");
	        	throw new RuntimeException(errorPayload.toString());
	        }
        } else {
        	ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), "Invalid input provided: " + input.toString());
        	throw new RuntimeException(errorPayload.toString());
        }
    }

}

