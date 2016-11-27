package coms6998.fall2016.data.models.comment;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonProperty;

import coms6998.fall2016.data.models.BaseModel;
import coms6998.fall2016.data.models.ReferenceAttribute;
import coms6998.fall2016.data.validation.CreateValidation;
import coms6998.fall2016.data.validation.UpdateValidation;
import coms6998.fall2016.models.Customer;

/**
 * Comment (ID, user, content instance, comment)
 * 
 * @author Jonathan
 *
 */
@DynamoDBTable(tableName = "Comments")
public class CommentModel extends BaseModel {

	private String userId;
	private String contentInstanceUuid;
	private String comment;

	@Override
	@DynamoDBHashKey(attributeName = "id")
	@JsonProperty("id")
	@NotNull(groups = CreateValidation.class, message = "[id] must be provided")
	@Null(groups = UpdateValidation.class, message = "[id] cannot be updated")
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "[id] must be alphanumeric, underscore & hyphens only.")
	public String getKey() {
		return super.getKey();
	}

	@DynamoDBAttribute
	@NotNull(groups = CreateValidation.class, message = "[userId] must be provided")
	@ReferenceAttribute(Customer.class)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@DynamoDBAttribute
	@NotNull(groups = CreateValidation.class, message = "[contentInstanceUuid] must be provided")
	@Pattern(regexp = "/^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$/", message = "[contentInstanceUuid] must be a valid UUID.")
	public String getContentInstanceUuid() {
		return contentInstanceUuid;
	}

	public void setContentInstanceUuid(String contentInstanceUuid) {
		this.contentInstanceUuid = contentInstanceUuid;
	}

	@DynamoDBAttribute
	@NotNull(groups = CreateValidation.class, message = "[comment] must be provided")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
