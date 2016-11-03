package coms6998.fall2016.lambdaFunctions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.lambdaFunctions.inputs.PersonLambdaInput;
import coms6998.fall2016.managers.GraphDBManager;
import coms6998.fall2016.managers.Neo4jDBManager;
import coms6998.fall2016.models.ErrorPayload;

public class PersonLambdaFunctionHandler implements RequestHandler<PersonLambdaInput, Object>{

	private GraphDBManager graphDBManager = new Neo4jDBManager();
	
	@Override
	public Object handleRequest(PersonLambdaInput input, Context context) {
		if(input.getOperation().equals("create")){
			graphDBManager.addPerson(input.getPerson());
			return input.getPerson();
		} else if(input.getOperation().equals("get")){
			return graphDBManager.getPerson(input.getPerson().getId());
		} else if(input.getOperation().equals("update")){
			graphDBManager.updatePerson(input.getPerson());
			return input.getPerson();
		} else if(input.getOperation().equals("delete")){
			graphDBManager.deletePerson(input.getPerson());
			return "success";
		} else {
			ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), "Invalid operation provided: " + input.getOperation());
			throw new RuntimeException(errorPayload.toString());
		}
	}

}
