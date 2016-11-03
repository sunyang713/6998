package coms6998.fall2016.models.contentcatalog;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Series")
public class SeriesModel extends ContentModel {
	private String franchiseRef;

	@DynamoDBAttribute
	public String getFranchiseRef() {
		return franchiseRef;
	}

	public void setFranchiseRef(String franchiseRef) {
		this.franchiseRef = franchiseRef;
	}

	@Override
	public ContentModel updateFromNew(ContentModel newContent) {
		super.updateFromNew(newContent);
		String newFranchiseRef = ((SeriesModel) newContent).franchiseRef;
		this.franchiseRef = newFranchiseRef == null ? this.franchiseRef : newFranchiseRef;
		return this;
	}

}
