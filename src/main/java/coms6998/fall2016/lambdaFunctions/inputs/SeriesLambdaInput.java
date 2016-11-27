package coms6998.fall2016.lambdaFunctions.inputs;

import coms6998.fall2016.models.Comment;
import coms6998.fall2016.models.Series;

public class SeriesLambdaInput {

	private String operation;
	
	private Series series;
	
	private Comment comment;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Series getSeries() {
		return series;
	}

	public void setSeries(Series series) {
		this.series = series;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}
}
