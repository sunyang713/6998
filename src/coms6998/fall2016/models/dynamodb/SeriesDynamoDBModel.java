package coms6998.fall2016.models.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonProperty;

@DynamoDBTable(tableName = "Series")
public class SeriesDynamoDBModel extends ContentDynamoDBModel {
	@JsonProperty(value = "FranchiseRef") private String franchiseRef;

	@Override
	@DynamoDBAttribute(attributeName = "FranchiseRef")
	public String getRef() {
		return franchiseRef;
	}

	@Override
	public void setRef(String franchiseRef) {
		this.franchiseRef = franchiseRef;
	}

	@Override
	public ContentDynamoDBModel updateFromNew(ContentDynamoDBModel newContent) {
		super.updateFromNew(newContent);
		String newFranchiseRef = ((SeriesDynamoDBModel) newContent).franchiseRef;
		this.franchiseRef = newFranchiseRef == null ? this.franchiseRef : newFranchiseRef;
		return this;
	}

}
