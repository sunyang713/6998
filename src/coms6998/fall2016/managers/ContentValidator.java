package coms6998.fall2016.managers;


import java.lang.reflect.Field;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

import coms6998.fall2016.models.dynamodb.ContentDynamoDBModel;

public final class ContentValidator {

	private static final Pattern CONTENT_ID_REGEX = Pattern.compile("^[a-zA-Z0-9-_]+$");
	private static final Pattern EMAIL_ADDRESS_REGEX = Pattern.compile(
			"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE
	);

	public static boolean isLettersOnly(String input){
		String cleaned = input.replaceAll("\\s+","");
		return cleaned.chars().allMatch(x -> Character.isLetter(x));
	}

	public static boolean isValidPhoneNumber(String pn){
		boolean isDigits = pn.chars().allMatch(x -> Character.isDigit(x));
		return isDigits && pn.length() == 10;
	}

	public static boolean isValidEmail(String email) {
		Matcher matcher = EMAIL_ADDRESS_REGEX.matcher(email);
		return matcher.find();
	}

	public static boolean isValidZipCode(String zc){
		boolean isDigits = zc.chars().allMatch(x -> Character.isDigit(x));
		return isDigits && (zc.length() == 5);
	}
	
	public static <T extends ContentDynamoDBModel> T validateMapForUpdate(Map<String, String> map, Class<T> modelType) throws InvalidContentException {
		// Coerce the input payload mapping into the appropriate model type POJO.
		ObjectMapper mapper = new ObjectMapper();
		try {
			T payload = mapper.convertValue(map, modelType);
			return validateContentForUpdate(payload);
		} catch (IllegalArgumentException e) {
			throw new InvalidContentException("Invalid fields in payload.");
			// throw new InvalidContentException(e.getMessage());
		}
	}

	public static <T extends ContentDynamoDBModel> T validateMapForCreation(Map<String, String> map, Class<T> modelType) throws InvalidContentException {
		// Coerce the input payload mapping into the appropriate model type POJO.
		ObjectMapper mapper = new ObjectMapper();
		try {
			T payload = mapper.convertValue(map, modelType);
			return validateContentForCreation(payload);
		} catch (IllegalArgumentException e) {
			throw new InvalidContentException("Invalid fields in payload.");
			// throw new InvalidContentException(e.getMessage());
		}
	}

	public static <T extends ContentDynamoDBModel> T validateContentForUpdate(T content) throws InvalidContentException {
		if (content.getId() != null) {
			throw new InvalidContentException("Cannot change ID.");
		}
		if (content.getName() != null && content.getName().isEmpty()) {
			throw new InvalidContentException("Name cannot be empty.");
		}
		if (content.getRef() != null && content.getRef().isEmpty()) {
			throw new InvalidContentException("Ref cannot be empty.");
		}
		return content;
	}

	public static <T extends ContentDynamoDBModel> T validateContentForCreation(T content) throws InvalidContentException {
		if (content.getId() == null || content.getId().isEmpty()) {
			throw new InvalidContentException("ID cannot be empty.");
		}
		Matcher matcher = CONTENT_ID_REGEX.matcher(content.getId());
		if (!matcher.matches()) {
			throw new InvalidContentException("Invalid ID. Must be alphanumeric and dashes only.");
		}
		if (content.getName() == null) {
			throw new InvalidContentException("Must provide name.");
		}
		if (content.getName().isEmpty()) {
			throw new InvalidContentException("Name cannot be empty.");
		}
		return content;
	}

	public static class InvalidContentException extends Exception {
	    public InvalidContentException(String message) {
	        super(message);
	    }
	}

}
