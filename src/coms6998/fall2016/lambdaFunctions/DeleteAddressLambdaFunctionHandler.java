package coms6998.fall2016.lambdaFunctions;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.managers.DynamoDBManager;
import coms6998.fall2016.models.Address;
import coms6998.fall2016.models.Customer;

public class DeleteAddressLambdaFunctionHandler implements RequestHandler<Address, String> {
	
    @Override
    public String handleRequest(Address address, Context context) {
        context.getLogger().log("Deleting address with UUID: " + address.getUuid());
        boolean rval = (new DynamoDBManager()).deleteAddress(address);
        if(rval) {
        	return "{\"message\":\"Address with UUID: " + address.getUuid() + " deleted.\"}";
        } else {
        	return "{\"message\":\"Failed to delete address with UUID: " + address.getUuid() + ".\"}";
        }
    }

}
