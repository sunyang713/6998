package coms6998.fall2016.lambdaFunctions;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.managers.DBManager;
import coms6998.fall2016.managers.DynamoDBManager;
import coms6998.fall2016.managers.ValidationManager;
import coms6998.fall2016.models.Customer;
import coms6998.fall2016.models.DBReturnCode;
import coms6998.fall2016.models.ErrorPayload;


public class UpdateCustomerLambdaFunctionHandler implements RequestHandler<Customer, CustomerLambdaResponse> {

	private DBManager dynamoDBManager = new DynamoDBManager();

	@Override
    public CustomerLambdaResponse handleRequest(Customer customerInput, Context context) {
    	LambdaLogger logger = context.getLogger();
        ValidationManager vm = new ValidationManager(context);
    	String message;
    	
    	// Validate email (must exist)
		if (!vm.isValidEmail(customerInput.getEmail())) {
    		message = "Invalid email provided: " + customerInput.toString();
        	logger.log(message);
        	ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), message);
        	throw new RuntimeException(errorPayload.toString());
		}
		// Validate phone number if exists
    	if (customerInput.getPhoneNumber() != null  && !customerInput.getPhoneNumber().equals("")) {
    		if (!vm.isValidPhoneNumber(customerInput.getPhoneNumber())) {
	    		message = "Invalid phone provided: " + customerInput.toString();
	        	logger.log(message);
	        	ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), message);
	        	throw new RuntimeException(errorPayload.toString());
    		}    		
    	}
    	// Validate first name if exists - letters only.
    	if (customerInput.getFirstName() != null  && !customerInput.getFirstName().equals("")) {
    		if (!vm.isLettersOnly(customerInput.getFirstName())) {
	    		message = "Invalid firstName provided: " + customerInput.toString();
	        	logger.log(message);
	        	ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), message);
	        	throw new RuntimeException(errorPayload.toString());
    		}    		
    	}
    	// Validate last name if exists - letters only.
    	if (customerInput.getLastName() != null  && !customerInput.getLastName().equals("")) {
    		if (!vm.isLettersOnly(customerInput.getLastName())) {
	    		message = "Invalid last name provided: " + customerInput.toString();
	        	logger.log(message);
	        	ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), message);
	        	throw new RuntimeException(errorPayload.toString());
    		}
    	}
    	
        logger.log("Updating customer with email: " + customerInput.getEmail());
    	DBReturnCode returnCode = dynamoDBManager.updateCustomer(customerInput);
    	
    	if(returnCode.equals(DBReturnCode.Success)) {
    		message = "Successfully updated customer: " + customerInput.getEmail();
            logger.log(message);
            return new CustomerLambdaResponse(message);
    	} else if(returnCode.equals(DBReturnCode.NotFound)) {
    		message = "Unable to find customer with email: " + customerInput.getEmail();
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
	
	public boolean throwInvalidInputException(Customer customerInput, Context context) {
		String message = "Invalid input provided: " + customerInput.toString();
    	LambdaLogger logger = context.getLogger();
    	logger.log(message);
    	ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), message);
    	throw new RuntimeException(errorPayload.toString());

	}

}


