package coms6998.fall2016.lambdaFunctions.contentcatalog;

import java.util.HashMap;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.managers.AdminDAO;
import coms6998.fall2016.managers.AdminDAO.DAOResponse;
import coms6998.fall2016.managers.AdminDynamoDBDAO;
import coms6998.fall2016.managers.ValidationManager;
import coms6998.fall2016.managers.ValidationManager.InvalidContentException;
import coms6998.fall2016.models.contentcatalog.ContentModel;
import coms6998.fall2016.models.contentcatalog.Resource;

public class ContentCatalogLambdaFunctionHandler implements RequestHandler<LambdaRequest, LambdaResponse<ContentModel>> {
	
	LambdaLogger logger;

	@Override
    public LambdaResponse<ContentModel> handleRequest(LambdaRequest input, Context context) {

		AdminDAO<ContentModel> dataAccessObject;
		DAOResponse<? extends ContentModel> daoResponse;

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
		Class<? extends ContentModel> modelType = resource.getModelType();
		
		// Initialize the DAO with the appropriate model type.
		dataAccessObject = new AdminDynamoDBDAO(modelType);

		switch (operation) {
		case "CREATE":
			// Validate input
			ContentModel payload;
			try {
				payload = ValidationManager.validateMapForCreation(inputMap, modelType);
			} catch (InvalidContentException e) {
				return respondFailure(e.getMessage());
			}
			
			daoResponse = dataAccessObject.create(payload);
			if (daoResponse.succeeded()) {
				return respondSuccess("Created " + resource + "/" + payload.getId());
			} else {
				return respondFailure(daoResponse.getMessage());
			}

		case "READ":
			daoResponse = dataAccessObject.read(parameter);
			if (daoResponse.succeeded()) {
				return respondSuccess("Retrieved " + resource + "/" + parameter, daoResponse.getPayload());
			} else {
				return respondFailure(daoResponse.getMessage());
			}
		case "UPDATE":
			try {
				payload = ValidationManager.validateMapForUpdate(inputMap, modelType);
			} catch (InvalidContentException e) {
				return respondFailure(e.getMessage());
			}
			daoResponse = dataAccessObject.update(parameter, payload);
			if (daoResponse.succeeded()) {
				return respondSuccess("Updated " + resource + "/" + parameter);
			} else {
				return respondFailure(daoResponse.getMessage());
			}
		case "DELETE":
			daoResponse = dataAccessObject.delete(parameter);
			if (daoResponse.succeeded()) {
				return respondSuccess("Deleted " + resource + "/" + parameter);
			} else {
				return respondFailure(daoResponse.getMessage());
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
