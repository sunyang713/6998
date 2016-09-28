package coms6998.fall2016.lambdaFunctions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.managers.DBManager;
import coms6998.fall2016.managers.DynamoDBManager;
import coms6998.fall2016.models.Address;
import coms6998.fall2016.models.DBReturnCode;
import coms6998.fall2016.models.ErrorPayload;

public class GetAddressLambdaFunctionHandler implements RequestHandler<Address, Address> {

	private DBManager dbManager = new DynamoDBManager();
	
	@Override
	public Address handleRequest(Address address, Context context) {
		context.getLogger().log("Getting address with UUID: " + address.getUuid());
		if(address.getUuid()== null || address.getUuid().isEmpty()) {
			ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), "No UUID provided");
			throw new RuntimeException(errorPayload.toString());
		}
		DBReturnCode rc = dbManager.getAdd(address);
		Address newAddress = (new DynamoDBManager()).getAddress(address.getUuid());
		if(rc.equals(DBReturnCode.Success) && !newAddress.isDeleted()) {
			System.out.printf("Address UUID: %s, city: %s, street: %s, number: %s, zipCode: %s.", newAddress.getUuid(), newAddress.getCity(), 
					newAddress.getStreet(), newAddress.getNumber(), newAddress.getZipCode());
			return newAddress;
		} else if(rc.equals(DBReturnCode.NotFound) || newAddress.isDeleted()){
			ErrorPayload errorPayload = new ErrorPayload("NotFound", 404, context.getAwsRequestId(), "No address with the provided UUID found: " + address.getUuid());
			throw new RuntimeException(errorPayload.toString());
		} else {
			ErrorPayload errorPayload = new ErrorPayload("Error", 500, context.getAwsRequestId(), "Unknown Internal Error");
			throw new RuntimeException(errorPayload.toString());
		}
	}

}
