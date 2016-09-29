package coms6998.fall2016.lambdaFunctions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.managers.DBManager;
import coms6998.fall2016.managers.DynamoDBManager;
import coms6998.fall2016.managers.ValidationManager;
import coms6998.fall2016.models.Address;
import coms6998.fall2016.models.DBReturnCode;
import coms6998.fall2016.models.ErrorPayload;

public class UpdateAddressLambdaFunctionHandler implements RequestHandler<Address, CustomerLambdaResponse> {

	private DBManager dynamoDBManager = new DynamoDBManager();

	/**
	 * Kind of sketchy, because in the addressInput, the UUID will be the desired one, but the
	 * data will will be the desired update which results in a different UUID.
	 */
	@Override
    public CustomerLambdaResponse handleRequest(Address addressInput, Context context) {
    	LambdaLogger logger = context.getLogger();
        ValidationManager vm = new ValidationManager(context);
    	String message;

    	// Validate city if exists - letters only
    	if (addressInput.getCity() != null && !addressInput.getCity().equals("")) {
    		if (!vm.isLettersOnly(addressInput.getCity())) {
    			message = "Invalid city provided: " + addressInput.toString();
    			logger.log(message);
	        	ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), message);
	        	throw new RuntimeException(errorPayload.toString());	
    		}
    	}
    	// Validate state if exists - letters only.
    	if (addressInput.getState() != null && !addressInput.getState().equals("")) {
    		if (!vm.isLettersOnly(addressInput.getState())) {
    			message = "Invalid state provided: " + addressInput.toString();
    			logger.log(message);
	        	ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), message);
	        	throw new RuntimeException(errorPayload.toString());	
    		}
    	}
    	// Validate zipcode if exists
    	if (addressInput.getZipCode() != null && !addressInput.getZipCode().equals("")) {
    		if (!vm.isValidZipCode(addressInput.getZipCode())) {
    			message = "Invalid zipcode provided: " + addressInput.toString();
    			logger.log(message);
	        	ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), message);
	        	throw new RuntimeException(errorPayload.toString());	
    		}
    	}

        logger.log("Updating address with uuid: " + addressInput.getUuid());
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
	}
	
}


