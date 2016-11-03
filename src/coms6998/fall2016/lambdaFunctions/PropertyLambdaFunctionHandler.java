package coms6998.fall2016.lambdaFunctions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.lambdaFunctions.inputs.PropertyLambdaInput;
import coms6998.fall2016.managers.GraphDBManager;
import coms6998.fall2016.managers.Neo4jDBManager;
import coms6998.fall2016.models.ErrorPayload;

public class PropertyLambdaFunctionHandler implements RequestHandler<PropertyLambdaInput, Object> {

	private GraphDBManager graphDBManager = new Neo4jDBManager();
	
	@Override
	public Object handleRequest(PropertyLambdaInput input, Context context) {
		if(input.getOperation().equals("create")) {
			graphDBManager.addProperty(input.getProperty());
			return input.getProperty();
		}else if (input.getOperation().equals("get")) {
			return graphDBManager.getProperty(input.getProperty().getId());
		}else if (input.getOperation().equals("update")) {
			return graphDBManager.updateProperty(input.getProperty());
		}else if (input.getOperation().equals("delete")) {
			return graphDBManager.deleteProperty(input.getProperty());
		}else if (input.getOperation().equals("addComment")) {
			return graphDBManager.addComments(input.getComment(), input.getProperty());
		}else if (input.getOperation().equals("getComments")) {
			return graphDBManager.getComments(input.getProperty());
		}else {
			ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), "Invalid operation provided: " + input.getOperation());
			throw new RuntimeException(errorPayload.toString());
		}
	}
}
