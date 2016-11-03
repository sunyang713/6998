package coms6998.fall2016.lambdaFunctions.inputs;

import coms6998.fall2016.models.Comment;
import coms6998.fall2016.models.Property;

public class PropertyLambdaInput {

	private String operation;
	
	private Property property;
	
	private Comment comment;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}
}
