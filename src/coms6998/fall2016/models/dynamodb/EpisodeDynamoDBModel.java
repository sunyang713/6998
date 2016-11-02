package coms6998.fall2016.models.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonProperty;

@DynamoDBTable(tableName = "Episodes")
public class EpisodeDynamoDBModel extends ContentDynamoDBModel {
	@JsonProperty(value = "SeriesRef") private String seriesRef;

	@Override
	@DynamoDBAttribute(attributeName = "SeriesRef")
	public String getRef() {
		return seriesRef;
	}

	@Override
	public void setRef(String seriesRef) {
		this.seriesRef = seriesRef;
	}
	
	@Override
	public ContentDynamoDBModel updateFromNew(ContentDynamoDBModel newContent) {
		super.updateFromNew(newContent);
		String newSeriesRef = ((EpisodeDynamoDBModel) newContent).seriesRef;
		this.seriesRef = newSeriesRef == null ? this.seriesRef : newSeriesRef;
		return this;
	}

}
