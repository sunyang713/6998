package coms6998.fall2016.lambdaFunctions;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import coms6998.fall2016.managers.DBManager;
import coms6998.fall2016.managers.DynamoDBManager;
import coms6998.fall2016.models.Address;
import coms6998.fall2016.models.DBReturnCode;
import coms6998.fall2016.models.ErrorPayload;

public class DeleteAddressLambdaFunctionHandler implements RequestHandler<Address, String> {
	
	private DBManager dbManager = new DynamoDBManager();
	
    @Override
    public String handleRequest(Address address, Context context) {
        context.getLogger().log("Deleting address with DPBarcode: " + address.getDPBarcode());
        if(address.getDPBarcode()== null || address.getDPBarcode().isEmpty()) {
        	ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), "No DPBarcode provided");
        	throw new RuntimeException(errorPayload.toString());
        }
        DBReturnCode rc = dbManager.deleteAddress(address);
        if(rc.equals(DBReturnCode.Success)) {
        	return "{\"message\":\"Address with DPBarcode: " + address.getDPBarcode() + " deleted.\"}";
        } else if(rc.equals(DBReturnCode.NotFound)){
        	ErrorPayload errorPayload = new ErrorPayload("NotFound", 404, context.getAwsRequestId(), "No address with the provided DPBarcode found: " + address.getDPBarcode());
        	throw new RuntimeException(errorPayload.toString());
        } else {
        	ErrorPayload errorPayload = new ErrorPayload("Error", 500, context.getAwsRequestId(), "Unknown Internal Error");
        	throw new RuntimeException(errorPayload.toString());
        }
    }

}
