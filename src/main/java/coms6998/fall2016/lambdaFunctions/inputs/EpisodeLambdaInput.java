package coms6998.fall2016.lambdaFunctions.inputs;

import coms6998.fall2016.models.Comment;
import coms6998.fall2016.models.Episode;

public class EpisodeLambdaInput {
	
	private String operation;
	
	private Episode episode;
	
	private Comment comment;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Episode getEpisode() {
		return episode;
	}

	public void setEpisode(Episode episode) {
		this.episode = episode;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}
}

