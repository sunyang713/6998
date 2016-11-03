package coms6998.fall2016.models.contentcatalog;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Franchises")
public class FranchiseModel extends ContentModel {
	private String propertyRef;

	@DynamoDBAttribute
	public String getPropertyRef() {
		return propertyRef;
	}

	public void setPropertyRef(String propertyRef) {
		this.propertyRef = propertyRef;
	}

	@Override
	public ContentModel updateFromNew(ContentModel newContent) {
		super.updateFromNew(newContent);
		String newPropertyRef = ((FranchiseModel) newContent).propertyRef;
		this.propertyRef = newPropertyRef == null ? this.propertyRef : newPropertyRef;
		return this;
	}

}
