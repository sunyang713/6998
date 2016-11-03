package coms6998.fall2016.lambdaFunctions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.lambdaFunctions.inputs.SeriesLambdaInput;
import coms6998.fall2016.managers.GraphDBManager;
import coms6998.fall2016.managers.Neo4jDBManager;
import coms6998.fall2016.models.ErrorPayload;

public class SeriesLambdaFunctionHandler implements RequestHandler<SeriesLambdaInput, Object> {

	private GraphDBManager graphDBManager = new Neo4jDBManager();
	
	@Override
	public Object handleRequest(SeriesLambdaInput input, Context context) {
		if(input.getOperation().equals("create")) {
			graphDBManager.addSeries(input.getSeries());
			return input.getSeries();
		}else if (input.getOperation().equals("get")) {
			return graphDBManager.getSeries(input.getSeries().getId());
		}else if (input.getOperation().equals("update")) {
			return graphDBManager.updateSeries(input.getSeries());
		}else if (input.getOperation().equals("delete")) {
			return graphDBManager.deleteSeries(input.getSeries());
		}else if (input.getOperation().equals("addComment")) {
			return graphDBManager.addComments(input.getComment(), input.getSeries());
		}else if (input.getOperation().equals("getComments")) {
			return graphDBManager.getComments(input.getSeries());
		}else {
			ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), "Invalid operation provided: " + input.getOperation());
			throw new RuntimeException(errorPayload.toString());
		}
	}

}
