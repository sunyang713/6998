package coms6998.fall2016.models.contentcatalog;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Episodes")
public class EpisodeModel extends ContentModel {
	private String seriesRef;

	@DynamoDBAttribute
	public String getSeriesRef() {
		return seriesRef;
	}

	public void setSeriesRef(String seriesRef) {
		this.seriesRef = seriesRef;
	}
	
	@Override
	public ContentModel updateFromNew(ContentModel newContent) {
		super.updateFromNew(newContent);
		String newSeriesRef = ((EpisodeModel) newContent).seriesRef;
		this.seriesRef = newSeriesRef == null ? this.seriesRef : newSeriesRef;
		return this;
	}

}
