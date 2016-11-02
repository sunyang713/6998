package coms6998.fall2016.models.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonProperty;

@DynamoDBTable(tableName = "Franchises")
public class FranchiseDynamoDBModel extends ContentDynamoDBModel {
	@JsonProperty(value = "PropertyRef") private String propertyRef;

	@Override
	@DynamoDBAttribute(attributeName = "PropertyRef")
	public String getRef() {
		return propertyRef;
	}

	@Override
	public void setRef(String propertyRef) {
		this.propertyRef = propertyRef;
	}

	@Override
	public ContentDynamoDBModel updateFromNew(ContentDynamoDBModel newContent) {
		super.updateFromNew(newContent);
		String newPropertyRef = ((FranchiseDynamoDBModel) newContent).propertyRef;
		this.propertyRef = newPropertyRef == null ? this.propertyRef : newPropertyRef;
		return this;
	}

}
