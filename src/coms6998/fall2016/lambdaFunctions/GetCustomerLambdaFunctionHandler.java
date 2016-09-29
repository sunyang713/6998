package coms6998.fall2016.lambdaFunctions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.managers.DBManager;
import coms6998.fall2016.managers.DynamoDBManager;
import coms6998.fall2016.managers.ValidationManager;
import coms6998.fall2016.models.Customer;
import coms6998.fall2016.models.DBReturnCode;
import coms6998.fall2016.models.ErrorPayload;

public class GetCustomerLambdaFunctionHandler implements RequestHandler<Customer, Customer> {
	
	private DBManager dbManager = new DynamoDBManager();
	
	@Override
	public Customer handleRequest(Customer customer, Context context) {
		context.getLogger().log("Getting customer with email: " + customer.getEmail());
		ValidationManager vm = new ValidationManager(context);
		if (vm.isValidEmail(customer.getEmail())){
			DBReturnCode rc = dbManager.getCus(customer);
			Customer newCustomer = (new DynamoDBManager()).getCustomer(customer.getEmail());
			if (rc.equals(DBReturnCode.Success) && !newCustomer.isDeleted()) {
					System.out.printf("Customer email: %s, firstname: %s, lastname: %s, phoneNum: %s, address: %s.", newCustomer.getEmail(),
	        			newCustomer.getFirstName(), newCustomer.getLastName(), newCustomer.getPhoneNumber(), newCustomer.getAddressRef());
					return newCustomer;
			} else if (rc.equals(DBReturnCode.NotFound) || newCustomer.isDeleted()){
				ErrorPayload errorPayload = new ErrorPayload("NotFound", 404, context.getAwsRequestId(), "No customer with the provided email found: " + customer.getEmail());
				throw new RuntimeException(errorPayload.toString());
			} else {
				ErrorPayload errorPayload = new ErrorPayload("Error", 500, context.getAwsRequestId(), "Unknown Internal Error");
				throw new RuntimeException(errorPayload.toString());
			}
		} else {
			ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), "Invalid email provided: " + customer.toString());
			throw new RuntimeException(errorPayload.toString());
		}
	}
}
