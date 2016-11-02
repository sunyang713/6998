package coms6998.fall2016.managers;

import java.util.Date;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;

import coms6998.fall2016.models.dynamodb.ContentDynamoDBModel;

public class AdminDynamoDBDAO<T extends ContentDynamoDBModel> implements AdminDAO<T> {

	private AmazonDynamoDBClient dynamoDBClient;
	private DynamoDBMapper mapper;
	private Class<T> modelType; // should be not null

	public AdminDynamoDBDAO() {
		dynamoDBClient = new AmazonDynamoDBClient();
		dynamoDBClient.setRegion(Region.getRegion(Regions.US_EAST_1));
		mapper = new DynamoDBMapper(dynamoDBClient);
	}

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
	public boolean create(T entity) throws DAOException {

		entity.setCreatedOnTimestamp(new Date());
		entity.setUpdatedOnTimestamp(new Date());
		entity.setIsDeleted(false);

		try {
			mapper.save(entity);
		} catch (ConditionalCheckFailedException e) {
			throw new DAOException(entity.getId() + " already exists.");
		}
		return true;
	}

	@Override
	public T read(String key) throws DAOException {
		T entity = mapper.load(modelType, key);
		if (entity == null) {
			throw new DAOException(key + " not found.");
		}
		return entity;
	}

	@Override
	public boolean update(String key, T newEntity) throws DAOException {
		T entity = read(key);
		entity.updateFromNew(newEntity);
		mapper.save(entity);
		return true;
	}

	@Override
	public boolean delete(String key) throws DAOException {
		mapper.delete(read(key));
		return true;
	}

}
