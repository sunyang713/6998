package coms6998.fall2016.models;

import coms6998.fall2016.models.dynamodb.ContentDynamoDBModel;
import coms6998.fall2016.models.dynamodb.EpisodeDynamoDBModel;
import coms6998.fall2016.models.dynamodb.FranchiseDynamoDBModel;
import coms6998.fall2016.models.dynamodb.PropertyDynamoDBModel;
import coms6998.fall2016.models.dynamodb.SeriesDynamoDBModel;

public enum Resource {
	PROPERTIES (PropertyDynamoDBModel.class),
	FRANCHISES (FranchiseDynamoDBModel.class),
	SERIES (SeriesDynamoDBModel.class),
	EPISODES (EpisodeDynamoDBModel.class);
	
	private final Class<? extends ContentDynamoDBModel> modelType;
	
	Resource(Class<? extends ContentDynamoDBModel> modelType) {
		this.modelType = modelType;
	}

	public <T extends ContentDynamoDBModel> Class<? extends ContentDynamoDBModel> getModelType() {
		return modelType;
	}

}
