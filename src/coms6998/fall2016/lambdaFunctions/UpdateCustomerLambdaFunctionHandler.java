package coms6998.fall2016.lambdaFunctions;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;


import coms6998.fall2016.managers.DynamoDBManager;
import coms6998.fall2016.managers.ValidationManager;
import coms6998.fall2016.models.Customer;
import coms6998.fall2016.models.DBReturnCode;
import coms6998.fall2016.models.ErrorPayload;


public class UpdateCustomerLambdaFunctionHandler implements RequestHandler<Customer, CustomerLambdaResponse> {

	private DynamoDBManager dynamoDBManager = new DynamoDBManager();

	@Override
    public CustomerLambdaResponse handleRequest(Customer customerInput, Context context) {
    	LambdaLogger logger = context.getLogger();
        ValidationManager vm = new ValidationManager(context);
    	String message;

    	// Wish we could DRY this with better enums
        if(vm.isValidCustomer(customerInput)) {
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
        } else {
    		message = "Invalid input provided: " + customerInput.toString();
            logger.log(message);
        	ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), message);
        	throw new RuntimeException(errorPayload.toString());
        }
	}

}


