package coms6998.fall2016.lambda.comment;

import coms6998.fall2016.data.access.AdminDynamoDBDAO;
import coms6998.fall2016.data.models.comment.CommentModel;
import coms6998.fall2016.lambda.MicroserviceLambda;

public class CommentLambda extends MicroserviceLambda<CommentModel> {

	public CommentLambda() {
		super(new AdminDynamoDBDAO(CommentModel.class), CommentModel.class);
	}

}
