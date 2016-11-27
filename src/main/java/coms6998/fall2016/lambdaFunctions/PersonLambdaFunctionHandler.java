package coms6998.fall2016.lambdaFunctions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.lambdaFunctions.inputs.PersonLambdaInput;
import coms6998.fall2016.managers.GraphDBManager;
import coms6998.fall2016.managers.Neo4jDBManager;
import coms6998.fall2016.models.Comment;
import coms6998.fall2016.models.DBReturnCode;
import coms6998.fall2016.models.ErrorPayload;
import coms6998.fall2016.models.Person;

public class PersonLambdaFunctionHandler implements RequestHandler<PersonLambdaInput, Object>{

	private GraphDBManager graphDBManager = new Neo4jDBManager();
	
	@Override
	public Object handleRequest(PersonLambdaInput input, Context context) {
		if(input.getOperation().equals("create")){
			graphDBManager.addPerson(input.getPerson());
			return input.getPerson();
		} else if(input.getOperation().equals("get")){
			Person person = graphDBManager.getPerson(input.getPerson().getId());
			if(person != null)
				return person.cloneRemoveRecursiveRelationship();
			else {
				ErrorPayload errorPayload = new ErrorPayload("NotFound", 404, context.getAwsRequestId(), "No Person with provided ID found: " + input.getPerson().getId());
				throw new RuntimeException(errorPayload.toString());
			}
		} else if(input.getOperation().equals("update")){
			graphDBManager.updatePerson(input.getPerson());
			return input.getPerson();
		} else if(input.getOperation().equals("delete")){
			graphDBManager.deletePerson(input.getPerson());
			return "success";
		} else if(input.getOperation().equals("addFriend")) {
			graphDBManager.addFriend(input.getPerson(), input.getFriend());
			return input.getPerson();
		} else if(input.getOperation().equals("getFriends")) {
			Person person = graphDBManager.getPerson(input.getPerson().getId());
			return person.cloneRemoveRecursiveRelationship().getFriends();
		}  else if(input.getOperation().equals("addFollows")) {
			graphDBManager.addFollows(input.getPerson(), input.getFollow());
			return input.getPerson();
		} else if(input.getOperation().equals("getFollows")) {
			Person person = graphDBManager.getPerson(input.getPerson().getId());
			return person.cloneRemoveRecursiveRelationship().getFollows();
		} else if(input.getOperation().equals("getFollowedBy")) {
			Person person = graphDBManager.getPerson(input.getPerson().getId());
			return person.cloneRemoveRecursiveRelationship().getFollowedBy();
		} else if(input.getOperation().equals("addComment")) {
			graphDBManager.addComment(input.getPerson(), input.getComment());
			return input.getComment();
		} else if(input.getOperation().equals("getComments")) {
			Person person = graphDBManager.getPerson(input.getPerson().getId());
			return person.getComments();
		} else if(input.getOperation().equals("deleteComment")) {
			graphDBManager.deleteComment(input.getComment());
			return "success";
		} else if(input.getOperation().equals("updateComment")) {
			graphDBManager.updateComment(input.getComment());
			return "success";
		} else {
			ErrorPayload errorPayload = new ErrorPayload("BadRequest", 422, context.getAwsRequestId(), "Invalid operation provided: " + input.getOperation());
			throw new RuntimeException(errorPayload.toString());
		}
	}
}
