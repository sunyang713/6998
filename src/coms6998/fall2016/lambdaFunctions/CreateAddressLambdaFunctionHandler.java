package coms6998.fall2016.lambdaFunctions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.managers.DBManager;
import coms6998.fall2016.managers.DynamoDBManager;
import coms6998.fall2016.managers.ValidationManager;
import coms6998.fall2016.models.Address;
import coms6998.fall2016.models.DBReturnCode;
import coms6998.fall2016.models.ErrorPayload;
import coms6998.fall2016.models.Response;

public class CreateAddressLambdaFunctionHandler implements RequestHandler<Address, String> {
	private DBManager dbManager = new DynamoDBManager();
	
	@Override
	 public String handleRequest(Address input, Context context) {
		context.getLogger().log("Input: " + input);
		ValidationManager vm = new ValidationManager(context);
		  if(vm.isValidAddress(input)){
		        DBReturnCode rc = dbManager.create(input);
		        if(rc.equals(DBReturnCode.Success)) {
		        	return "{\"message\":\"Address with UUID: " + input.getUuid() + " created.\"}";
		        } else if (rc.equals(DBReturnCode.AlreadyExists)){
		        	ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), "Address with UUID already exists: " + input.getUuid() + ".");
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
