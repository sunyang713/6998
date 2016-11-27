package coms6998.fall2016.lambda;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import coms6998.fall2016.data.access.AdminDAO;
import coms6998.fall2016.data.access.DAOResponse;
import coms6998.fall2016.data.models.BaseModel;
import coms6998.fall2016.data.validation.ModelValidator;

/**
 * microservice-http-endpoint A simple backend (read/write to DynamoDB) with a
 * RESTful API endpoint using Amazon API Gateway.
 * 
 * @author Jonathan
 *
 */
public class MicroserviceLambda<ModelType extends BaseModel>
		implements RequestHandler<APIGatewayProxyEvent, APIGatewayProxyResponse> {

	@NotNull
	private AdminDAO dataAccessObject;

	@NotNull
	private Class<ModelType> modelType;

	public MicroserviceLambda(AdminDAO dataAccessObject, Class<ModelType> modelType) {
		this.dataAccessObject = dataAccessObject;
		this.modelType = modelType;
	}

	@Override
	public APIGatewayProxyResponse handleRequest(APIGatewayProxyEvent input, Context context) {
		LambdaLogger logger = context.getLogger();
		logger.log("Received request: " + input.toString());

		String pathParameter;
		Set<ConstraintViolation<ModelType>> violations;
		DAOResponse daoResponse;
		ModelType entity;
		switch (input.getHttpMethod()) {
		case "GET":
			// TODO if necessary, do bean validation for APIGatewayProxyEvent
			if ((pathParameter = LambdaUtils.getSingleValue(input.getPathParameters())) == null) {
				return LambdaUtils.createResponse(input, 400, "No path parameter provided");
			}
			daoResponse = dataAccessObject.read(pathParameter);
			return LambdaUtils.createResponse(input, daoResponse.getStatusCode(), daoResponse.getPayload());

		case "POST":
			try {
				entity = LambdaUtils.convertJsonToObject(input.getBody(), modelType);
			} catch (JsonParseException e) {
				return LambdaUtils.createResponse(input, 400, e.getOriginalMessage());
			} catch (JsonMappingException e) {
				return LambdaUtils.createResponse(input, 400, e.getOriginalMessage());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			if ((violations = ModelValidator.validateForCreate(entity)).size() > 0) {
				return LambdaUtils.createResponse(input, 400, violations);
			}

			entity.setCreatedOnTimestamp(new Date());
			entity.setUpdatedOnTimestamp(new Date());
			daoResponse = dataAccessObject.create(entity);
			return LambdaUtils.createResponse(input, daoResponse.getStatusCode(), daoResponse.getPayload());

		case "PUT":
			// TODO if necessary, do bean validation for APIGatewayProxyEvent
			if ((pathParameter = LambdaUtils.getSingleValue(input.getPathParameters())) == null) {
				return LambdaUtils.createResponse(input, 400, "No path parameter provided");
			}

			// TODO wrap this in a custom 'LambdaException' with cleaned
			// messages.
			try {
				entity = LambdaUtils.convertJsonToObject(input.getBody(), modelType);
			} catch (JsonParseException e) {
				return LambdaUtils.createResponse(input, 400, e.getOriginalMessage());
			} catch (JsonMappingException e) {
				return LambdaUtils.createResponse(input, 400, e.getOriginalMessage());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			if ((violations = ModelValidator.validateForUpdate(entity)).size() > 0) {
				return LambdaUtils.createResponse(input, 400, violations);
			}

			entity.setUpdatedOnTimestamp(new Date());
			daoResponse = dataAccessObject.update(pathParameter, entity);
			return LambdaUtils.createResponse(input, daoResponse.getStatusCode(), daoResponse.getPayload());

		case "DELETE":
			// TODO if necessary, do bean validation for APIGatewayProxyEvent
			if ((pathParameter = LambdaUtils.getSingleValue(input.getPathParameters())) == null) {
				return LambdaUtils.createResponse(input, 400, "No path parameter provided");
			}

			daoResponse = dataAccessObject.delete(pathParameter);
			return LambdaUtils.createResponse(input, daoResponse.getStatusCode(), daoResponse.getPayload());

		default:
			return LambdaUtils.createResponse(input, 400, "Invalid HTTP method");

		}

	}

}

// DynamoDBMapper.
// paginatedScanList
// DynamoDBMapperTableModel thing = new DynamoDBMapperTableModel(clazz,
