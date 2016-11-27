package coms6998.fall2016.managers;

import java.util.Date;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;

import coms6998.fall2016.models.contentcatalog.ContentModel;

public class AdminDynamoDBDAO<T extends ContentModel> implements AdminDAO<T> {

	private AmazonDynamoDBClient dynamoDBClient;
	private DynamoDBMapper mapper;
	private Class<T> modelType; // should be not null

	public AdminDynamoDBDAO(Class<T> modelType) {
		dynamoDBClient = new AmazonDynamoDBClient();
		dynamoDBClient.setRegion(Region.getRegion(Regions.US_EAST_1));
		mapper = new DynamoDBMapper(dynamoDBClient);
		this.modelType = modelType;
	}

	public Class<T> getModelType() {
		return modelType;
	}

	public void setModelType(Class<T> modelType) {
		this.modelType = modelType;
	}

	/**
	 * Creates the entity only if it does not exist.
	 */
	@Override
	public DAOResponse<T> create(T entity) {

		entity.setCreatedOnTimestamp(new Date());
		entity.setUpdatedOnTimestamp(new Date());
		entity.setIsDeleted(false);
		
		System.out.println(entity.getId());

		try {
			mapper.save(entity);
			return new DAOResponse<T>(true, "Successfully created", entity);
		} catch (ConditionalCheckFailedException e) {
			return new DAOResponse<T>(false, "Already exists", entity);
		}
	}

	@Override
	public DAOResponse<T> read(String key) {
		T entity = mapper.load(modelType, key);
		if (entity == null) {
			return new DAOResponse<T>(false, key + " not found", null);
		} else {
			return new DAOResponse<T>(true, "Succesfully read", entity);
		}
	}

	// should test read (sum1 else delets,) then save. should die?
	@Override
	public DAOResponse<T> update(String key, T newEntity) {
		DAOResponse<T> readResponse = read(key);
		if (!readResponse.succeeded()) {
			return readResponse;
		}
		T entity = readResponse.getPayload();
		entity.updateFromNew(newEntity);
		try {
			mapper.save(entity);
			return new DAOResponse<T>(true, "Successfully updated", entity);
		} catch (ConditionalCheckFailedException e) {
			return new DAOResponse<T>(false, "Unable to update at this time", newEntity);
		}
	}

	@Override
	public DAOResponse<T> delete(String key) {
		DAOResponse<T> readResponse = read(key);
		if (!readResponse.succeeded()) {
			return readResponse;
		}
		
		T entity = readResponse.getPayload();
		try {
			mapper.delete(entity);
			return new DAOResponse<T>(true, "Successfully deleted", entity);
		} catch (ConditionalCheckFailedException e) {
			return new DAOResponse<T>(false, "Unable to delete at this time", entity);
		}
	}

}
