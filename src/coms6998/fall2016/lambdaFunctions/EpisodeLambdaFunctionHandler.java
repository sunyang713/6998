package coms6998.fall2016.lambdaFunctions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.lambdaFunctions.inputs.EpisodeLambdaInput;
import coms6998.fall2016.managers.GraphDBManager;
import coms6998.fall2016.managers.Neo4jDBManager;
import coms6998.fall2016.models.ErrorPayload;

public class EpisodeLambdaFunctionHandler implements RequestHandler<EpisodeLambdaInput, Object> {
	
	private GraphDBManager graphDBManager = new Neo4jDBManager();
	
	@Override
	public Object handleRequest(EpisodeLambdaInput input, Context context) {
		if(input.getOperation().equals("create")) {
			graphDBManager.addEpisode(input.getEpisode());
			return input.getEpisode();
		}else if (input.getOperation().equals("get")) {
			return graphDBManager.getEpisode(input.getEpisode().getId());
		}else if (input.getOperation().equals("update")) {
			return graphDBManager.updateEpisode(input.getEpisode());
		}else if (input.getOperation().equals("delete")) {
			return graphDBManager.deleteEpisode(input.getEpisode());
		}else if (input.getOperation().equals("addComment")) {
			return graphDBManager.addComments(input.getComment(), input.getEpisode());
		}else if (input.getOperation().equals("getComments")) {
			return graphDBManager.getComments(input.getEpisode());
		}else {
			ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), "Invalid operation provided: " + input.getOperation());
			throw new RuntimeException(errorPayload.toString());
		}
	}

}
