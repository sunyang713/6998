package coms6998.fall2016.models.contentcatalog;

public enum Resource {
	PROPERTIES (PropertyModel.class),
	FRANCHISES (FranchiseModel.class),
	SERIES (SeriesModel.class),
	EPISODES (EpisodeModel.class);
	
	private final Class<? extends ContentModel> modelType;
	
	Resource(Class<? extends ContentModel> modelType) {
		this.modelType = modelType;
	}

	public <T extends ContentModel> Class<? extends ContentModel> getModelType() {
		return modelType;
	}

}
