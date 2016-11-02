package coms6998.fall2016.lambdaFunctions.contentCatalog;

import java.util.HashMap;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.managers.AdminDAO;
import coms6998.fall2016.managers.AdminDAO.DAOException;
import coms6998.fall2016.managers.AdminDynamoDBDAO;
import coms6998.fall2016.managers.ContentValidator;
import coms6998.fall2016.managers.ContentValidator.InvalidContentException;
import coms6998.fall2016.models.Resource;
import coms6998.fall2016.models.dynamodb.ContentDynamoDBModel;

public class ContentCatalogLambdaFunctionHandler implements RequestHandler<LambdaRequest, LambdaResponse<ContentDynamoDBModel>> {
	
	LambdaLogger logger;

	@Override
    public LambdaResponse<ContentDynamoDBModel> handleRequest(LambdaRequest input, Context context) {

		AdminDAO<ContentDynamoDBModel> dataAccessObject;
		logger = context.getLogger();
		logger.log("Received input: " + input);

		String operation = input.getOperation();
		String resourceString = input.getResource();
		String parameter = input.getParameter();
		HashMap<String, String> inputMap = input.getPayload();
		
		// Determine the modelType from the resource string.
		Resource resource;
		try {
			resource = Resource.valueOf(resourceString.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("No resource " + resourceString);
		}
		Class<? extends ContentDynamoDBModel> modelType = resource.getModelType();

		// Initialize the DAO with the appropriate model type.
		dataAccessObject = new AdminDynamoDBDAO(modelType);
		
		switch (operation) {
		case "CREATE":
			// Validate input
			ContentDynamoDBModel payload;
			try {
				payload = ContentValidator.validateMapForCreation(inputMap, modelType);
			} catch (InvalidContentException e) {
				return respondFailure(e.getMessage());
			}
			try {
				dataAccessObject.create(payload);
				return respondSuccess("Created " + resource + "/" + payload.getId());
			} catch (DAOException e) {
				return respondFailure(e.getMessage());
			}
		case "READ":
			try {
				ContentDynamoDBModel content = dataAccessObject.read(parameter);
				return respondSuccess("Retrieved " + resource + "/" + parameter, content);
			} catch (DAOException e) {
				return respondFailure(e.getMessage());
			}
		case "UPDATE":
			try {
				payload = ContentValidator.validateMapForUpdate(inputMap, modelType);
			} catch (InvalidContentException e) {
				return respondFailure(e.getMessage());
			}
			try {
				dataAccessObject.update(parameter, payload);
				return respondSuccess("Updated " + resource + "/" + parameter);
			} catch (DAOException e) {
				return respondFailure(e.getMessage());
			}
		case "DELETE":
			try {
				dataAccessObject.delete(parameter);
				return respondSuccess("Deleted " + resource + "/" + parameter);
			} catch (DAOException e) {
				return respondFailure(e.getMessage());
			}
		default:
			return respondFailure("Invalid operation.");
		}
	}
	
	private <T> LambdaResponse<T> respondSuccess(String message) {
		logger.log("SUCCESS: " + message);
		return _respond("SUCCESS", message, null);
	}

	private <T> LambdaResponse<T> respondSuccess(String message, T payload) {
		logger.log("SUCCESS: " + message + " -- " + payload);
		return _respond("SUCCESS", message, payload);
	}

	private <T> LambdaResponse<T> respondFailure(String message) {
		logger.log("FAILURE: " + message);
		return _respond("FAILURE", message, null);
	}

	private <T> LambdaResponse<T> _respond(String status,String message, T payload) {
		LambdaResponse<T> response = new LambdaResponse<T>();
		response.setStatus(status);
		response.setMessage(message);
		response.setPayload(payload);
		logger.log(message);
		return response;
	}

}
