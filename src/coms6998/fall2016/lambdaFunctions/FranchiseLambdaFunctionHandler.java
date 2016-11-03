package coms6998.fall2016.lambdaFunctions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.lambdaFunctions.inputs.FranchiseLambdaInput;
import coms6998.fall2016.managers.GraphDBManager;
import coms6998.fall2016.managers.Neo4jDBManager;
import coms6998.fall2016.models.ErrorPayload;

public class FranchiseLambdaFunctionHandler implements RequestHandler<FranchiseLambdaInput, Object> {
	
	private GraphDBManager graphDBManager = new Neo4jDBManager();
	
	@Override
	public Object handleRequest(FranchiseLambdaInput input, Context context) {
		if(input.getOperation().equals("create")) {
			graphDBManager.addFranchise(input.getFranchise());
			return input.getFranchise();
		}else if (input.getOperation().equals("get")) {
			return graphDBManager.getFranchise(input.getFranchise().getId());
		}else if (input.getOperation().equals("update")) {
			return graphDBManager.updateFranchise(input.getFranchise());
		}else if (input.getOperation().equals("delete")) {
			return graphDBManager.deleteFranchise(input.getFranchise());
		}else if (input.getOperation().equals("addComment")) {
			return graphDBManager.addComments(input.getComment(), input.getFranchise());
		}else if (input.getOperation().equals("getComments")) {
			return graphDBManager.getComments(input.getFranchise());
		}else {
			ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), "Invalid operation provided: " + input.getOperation());
			throw new RuntimeException(errorPayload.toString());
		}
	}

}
