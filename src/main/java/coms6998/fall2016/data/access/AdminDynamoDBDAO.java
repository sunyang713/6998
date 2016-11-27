package coms6998.fall2016.data.access;

import javax.validation.constraints.NotNull;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;

import coms6998.fall2016.data.models.ModelUtils;

/**
 * DynamoDB Data Access Object
 * 
 * All operations handle known exceptions as follows: when a known exception is
 * thrown like 'ConditionalCheckFailedException', a new DAOResponse object is
 * returned with the corresponding statusCode and error messageas the payload.
 * Unknown unchecked exceptions throw per usual, allowing the exception to
 * bubble up to the client handler (which should typically be API Gateway. This
 * is fine because API Gateway swallows the stack trace and responds with a 50x
 * to its client.
 * 
 * @author Jonathan
 *
 */
public class AdminDynamoDBDAO implements AdminDAO {

	@NotNull
	private DynamoDBMapper mapper;

	@NotNull
	private Class<?> modelClazz;

	public AdminDynamoDBDAO(Class<?> modelClazz) {
		AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient();
		dynamoDBClient.setRegion(Region.getRegion(Regions.US_EAST_1));
		mapper = new DynamoDBMapper(dynamoDBClient);
		this.modelClazz = modelClazz;
	}

	/**
	 * Creates the entity only if it does not exist.
	 */
	@Override
	public DAOResponse create(Object entity) {
		try {
			mapper.save(entity);
		} catch (ConditionalCheckFailedException e) {
			return new DAOResponse(e.getStatusCode(), "Already exists: " + e.getErrorMessage());
		}
		return new DAOResponse(201, entity);
	}

	@Override
	public DAOResponse read(String key) {
		Object entity;

		entity = mapper.load(modelClazz, key);

		if (entity == null) {
			return new DAOResponse(404, "Item with key " + key + " not found");
		} else {
			return new DAOResponse(200, entity);
		}
	}

	@Override
	public DAOResponse update(String key, Object newEntity) {
		DAOResponse readResponse = read(key);
		if (readResponse.getStatusCode() != 200) {
			return readResponse;
		}

		Object entity = readResponse.getPayload();
		entity = ModelUtils.updateOldObjectWithNewObject(entity, newEntity, newEntity.getClass());

		try {
			mapper.save(entity);
		} catch (ConditionalCheckFailedException e) {
			return new DAOResponse(e.getStatusCode(), "Update " + key + ": " + e.getErrorMessage());
		}
		return new DAOResponse(200, "Successfully updated item with key " + key);
	}

	@Override
	public DAOResponse delete(String key) {
		DAOResponse readResponse = read(key);
		if (readResponse.getStatusCode() != 200) {
			return readResponse;
		}

		Object entity = readResponse.getPayload();

		try {
			mapper.delete(entity);
			return new DAOResponse(200, "Successfully deleted item with key " + key);
		} catch (ConditionalCheckFailedException e) {
			return new DAOResponse(e.getStatusCode(), "Delete " + key + ": " + e.getErrorMessage());
		}
	}

}
