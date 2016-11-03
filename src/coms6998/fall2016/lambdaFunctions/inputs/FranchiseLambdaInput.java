package coms6998.fall2016.lambdaFunctions.inputs;

import coms6998.fall2016.models.Comment;
import coms6998.fall2016.models.Franchise;

public class FranchiseLambdaInput {
	
	private String operation;
	
	private Franchise franchise;
	
	private Comment comment;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Franchise getFranchise() {
		return franchise;
	}

	public void setFranchise(Franchise franchise) {
		this.franchise = franchise;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}
}
