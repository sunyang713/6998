package coms6998.fall2016.lambdaFunctions;

import java.io.IOException;
import java.util.Optional;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.managers.DBManager;
import coms6998.fall2016.managers.DynamoDBManager;
import coms6998.fall2016.managers.ValidationManager;
import coms6998.fall2016.models.Address;
import coms6998.fall2016.models.DBReturnCode;
import coms6998.fall2016.models.ErrorPayload;


public class CreateAddressLambdaFunctionHandler implements RequestHandler<Address, String> {
	private DBManager dbManager = new DynamoDBManager();
	
	@Override
	 public String handleRequest(Address input, Context context) {
		context.getLogger().log("Input: " + input);
		ValidationManager vm = new ValidationManager(context);
		//the try-catch block should catch any validation errors that occurred during call to smartystreets api
		  try { 
			  //validation makes GET call to smartystreets API
			  //it will fill the primary key dbBarcode if it is valid
			if (vm.isValidAddress(input)) {
			        DBReturnCode rc = dbManager.create(input);
			        if(rc.equals(DBReturnCode.Success)) {
			        	return "{\"message\":\"Address with Delivery Barcode: " + input.getDPBarcode() + " created.\"}";
			        } else if (rc.equals(DBReturnCode.AlreadyExists)){
			        	ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), "Address with Delivery Barcode already exists: " + input.getDPBarcode() + ".");
			        	throw new RuntimeException(errorPayload.toString());
			        } else {
			        	ErrorPayload errorPayload = new ErrorPayload("Error", 500, context.getAwsRequestId(), "Unknown Internal Error");
			        	throw new RuntimeException(errorPayload.toString());
			        }
			 } else {
			    	ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), "Invalid input provided: " + input.toString());
			    	throw new RuntimeException(errorPayload.toString());
			 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//if smartystreets returned an error
		ErrorPayload errorPayload = new ErrorPayload("Error", 500, context.getAwsRequestId(), "Unknown Internal Error");
	throw new RuntimeException(errorPayload.toString()); 
	 }
}
