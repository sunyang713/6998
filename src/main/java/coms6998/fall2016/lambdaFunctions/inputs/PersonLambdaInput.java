package coms6998.fall2016.lambdaFunctions.inputs;

import coms6998.fall2016.models.Comment;
import coms6998.fall2016.models.Person;

public class PersonLambdaInput {
	
	private String operation;
	
	private Person person;
	
	private Person follow;
	
	private Person friend;
	
	private Comment comment;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Person getFollow() {
		return follow;
	}

	public void setFollow(Person follow) {
		this.follow = follow;
	}

	public Person getFriend() {
		return friend;
	}

	public void setFriend(Person friend) {
		this.friend = friend;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}
}
