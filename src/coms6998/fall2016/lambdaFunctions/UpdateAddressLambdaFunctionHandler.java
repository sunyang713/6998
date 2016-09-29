package coms6998.fall2016.lambdaFunctions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;


import coms6998.fall2016.managers.DynamoDBManager;
import coms6998.fall2016.managers.ValidationManager;
import coms6998.fall2016.models.Address;
import coms6998.fall2016.models.DBReturnCode;
import coms6998.fall2016.models.ErrorPayload;


public class UpdateAddressLambdaFunctionHandler implements RequestHandler<Address, CustomerLambdaResponse> {

	private DynamoDBManager dynamoDBManager = new DynamoDBManager();

	/**
	 * Kind of sketchy, because in the addressInput, the UUID will be the desired one, but the
	 * data will will be the desired update which results in a different UUID.
	 */
	@Override
    public CustomerLambdaResponse handleRequest(Address addressInput, Context context) {
    	LambdaLogger logger = context.getLogger();
        ValidationManager vm = new ValidationManager(context);
    	String message;

    	// Wish we could DRY this with better enums
        if(vm.isValidAddress(addressInput)) {

        	DBReturnCode returnCode = dynamoDBManager.updateAddress(addressInput);
        	
        	if(returnCode.equals(DBReturnCode.Success)) {
        		message = "Successfully updated address id: " + addressInput.getUuid();
                logger.log(message);
                return new CustomerLambdaResponse(message);
        	} else if(returnCode.equals(DBReturnCode.NotFound)) {
        		message = "Unable to find address with id: " + addressInput.getUuid();
                logger.log(message);
	        	ErrorPayload errorPayload = new ErrorPayload("NotFound", 404, context.getAwsRequestId(), message);
	        	throw new RuntimeException(errorPayload.toString());
        	} else {
        		message = "Unknown Internal Error";
                logger.log(message);
	        	ErrorPayload errorPayload = new ErrorPayload("Error", 500, context.getAwsRequestId(), message);
	        	throw new RuntimeException(errorPayload.toString());
        	}
        } else {
    		message = "Invalid input provided: " + addressInput.toString();
            logger.log(message);
        	ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), message);
        	throw new RuntimeException(errorPayload.toString());
        }
	}
	
}


