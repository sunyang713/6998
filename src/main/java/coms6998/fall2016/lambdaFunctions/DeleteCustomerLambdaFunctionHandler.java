package coms6998.fall2016.lambdaFunctions;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import coms6998.fall2016.managers.DBManager;
import coms6998.fall2016.managers.DynamoDBManager;
import coms6998.fall2016.managers.ValidationManager;
import coms6998.fall2016.models.Customer;
import coms6998.fall2016.models.DBReturnCode;
import coms6998.fall2016.models.ErrorPayload;

public class DeleteCustomerLambdaFunctionHandler implements RequestHandler<Customer, String> {
	
	private DBManager dbManager = new DynamoDBManager();
	
    @Override
    public String handleRequest(Customer customer, Context context) {
        context.getLogger().log("Deleting customer with email: " + customer.getEmail());
        if(customer.getEmail() == null || customer.getEmail().isEmpty()) {
        	ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), "No email provided");
        	throw new RuntimeException(errorPayload.toString());
        }
        ValidationManager validationManager = new ValidationManager(context);
        if(validationManager.isValidEmail(customer.getEmail())) {
	        DBReturnCode rc = dbManager.deleteCustomer(customer);
	        if(rc.equals(DBReturnCode.Success)) {
	        	return "{\"message\":\"Customer with email: " + customer.getEmail() + " deleted.\"}";
	        } else if (rc.equals(DBReturnCode.NotFound)){
	        	ErrorPayload errorPayload = new ErrorPayload("NotFound", 404, context.getAwsRequestId(), "No customer with the provided email found: " + customer.getEmail());
	        	throw new RuntimeException(errorPayload.toString());
	        } else {
	        	ErrorPayload errorPayload = new ErrorPayload("Error", 500, context.getAwsRequestId(), "Unknown Internal Error");
	        	throw new RuntimeException(errorPayload.toString());
	        }
        } else
        {
        	ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), "Invalid email provided: " + customer.getEmail());
        	throw new RuntimeException(errorPayload.toString());
        }
    }

}
