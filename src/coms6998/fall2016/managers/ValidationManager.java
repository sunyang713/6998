package coms6998.fall2016.managers;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;  
import java.io.IOException;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;

import coms6998.fall2016.models.Customer;
import coms6998.fall2016.models.Address;
import coms6998.fall2016.models.SmartyStreetsAddress;
import coms6998.fall2016.models.contentcatalog.ContentModel;

public class ValidationManager {
	private static final String URL_COMPONENTS_BEG = "https://us-street.api.smartystreets.com/street-address";
	private static final String MY_AUTH_ID = "5f9489fc-56a5-93dd-cebc-7fc90f9167f7";
	private static final String AUTH_ID = "auth-id";
	private static final String AUTH_TOKEN = "auth-token";
	private static final String MY_AUTH_TOKEN = "iuM7Z5G0bs9s61zkg76U";
	private static final String STREET = "street";
	private static final String CITY = "city";
	private static final String STATE = "state";
	private static final String CANDIDATES = "candidates";
	private static final String CANDIDATES_COUNT = "5";
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final String MATCH_DEFAULT = "strict";
	private static final Pattern CONTENT_ID_REGEX = Pattern.compile("^[a-zA-Z0-9-_]+$");

	private Context context;

	public ValidationManager(Context context){
		this.setContext(context);
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	private static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
			Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	public boolean isValidEmail(String email) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
		boolean isValid = matcher.find();
		if (!isValid) {
			context.getLogger().log("Invalid email!");
		}
		return isValid;
	}

	public boolean isLettersOnly(String input){
		String cleaned = input.replaceAll("\\s+","");
		boolean isValid = cleaned.chars().allMatch(x -> Character.isLetter(x));
		if (!isValid) {
			context.getLogger().log("Invalid letters-only entry!");
		}
		return isValid;
	}

	public boolean isValidPhoneNumber(String pn){
		boolean isDigits = pn.chars().allMatch(x -> Character.isDigit(x));
		boolean isValid = isDigits && (pn.length() == 10);
		if (!isValid){
			context.getLogger().log("Invalid phone number!");
		}
		return isValid;
	}

	public boolean isValidZipCode(String zc){
		boolean isDigits = zc.chars().allMatch(x -> Character.isDigit(x));
		boolean isValid = isDigits && (zc.length() == 5);
		if (!isValid){
			context.getLogger().log("Invalid zip code!");
		}
		return isValid;
	}

	public boolean isValidCustomer(Customer customer){
		return isValidEmail(customer.getEmail()) && 
				isLettersOnly(customer.getFirstName()) &&
				isLettersOnly(customer.getLastName()) &&
				isValidPhoneNumber(customer.getPhoneNumber());
	}
	
	//checks for valid Address AND sets the DPBarcode
	public boolean isValidAddress(Address address) throws IOException{
		String deliveryBarcode = deliveryPointBarcode(smartyStreetsGetResponse(address));
		
		if (!deliveryBarcode.isEmpty()){
			address.setDPBarcode(deliveryBarcode);
			return true;
		}
		return false;
	}
	
	
	public static <T extends ContentModel> T validateMapForUpdate(Map<String, String> map, Class<T> modelType) throws InvalidContentException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			T payload = mapper.convertValue(map, modelType);
			return validateContentForUpdate(payload);
		} catch (IllegalArgumentException e) {
			throw new InvalidContentException("Invalid fields in payload.");
		}
	}

	public static <T extends ContentModel> T validateMapForCreation(Map<String, String> map, Class<T> modelType) throws InvalidContentException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			T payload = mapper.convertValue(map, modelType);
			return validateContentForCreation(payload);
		} catch (IllegalArgumentException e) {
			throw new InvalidContentException("Invalid fields in payload.");
		}
	}

	public static <T extends ContentModel> T validateContentForUpdate(T content) throws InvalidContentException {
		if (content.getId() != null) {
			throw new InvalidContentException("Cannot change ID.");
		}
		if (content.getName() != null && content.getName().isEmpty()) {
			throw new InvalidContentException("Name cannot be empty.");
		}
		return content;
	}

	public static <T extends ContentModel> T validateContentForCreation(T content) throws InvalidContentException {
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
	
	
	//returns a deliveryPointBarcode or empty string given a SmartyStreets GET response
	//TODO - this is very coupled with SmartyStreets API, maybe we should move it elsewhere or 
	//have it as its own "SmartyStreetsAddress" class
	private String deliveryPointBarcode(String response) {
		//Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonArray responseAsArray = parser.parse(response).getAsJsonArray();
		if (responseAsArray.size() > 0){
			//SmartyStreetsAddress ssAddress = gson.fromJson(responseAsArray.get(0), SmartyStreetsAddress.class);
			//System.out.println("Made it to the deliveryPointBarcode if check");
			JsonObject obj = responseAsArray.get(0).getAsJsonObject();
			String dpBarcode = obj.get("delivery_point_barcode").getAsString();
			
			//System.out.println("Printing the delivery point barcode: " + dpBarcode);
			return dpBarcode;
		}
		return "";
	}

	//returns a Stringified JSON response from SmartyStreets GET request
	private String smartyStreetsGetResponse(Address address) throws IOException{
		String urlString = formSmartyStreetsGetRequest(address);

		System.out.println(urlString);
		
		URL url = new URL(urlString);  
		HttpURLConnection con = (HttpURLConnection) url.openConnection();  

		// By default it is GET request  
		con.setRequestMethod("GET");  

		//add request header  
		con.setRequestProperty("User-Agent", "Mozilla/5.0");  

		int responseCode = con.getResponseCode();  
		System.out.println("Sending get request : "+ url);  
		System.out.println("Response code : "+ responseCode);  

		// Reading response from input Stream  
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));  
		String output;  
		StringBuffer response = new StringBuffer();  

		while ((output = in.readLine()) != null) {  
			response.append(output);  
		}  
		in.close();  

		//printing result from response  
		System.out.println("Getting response: " + response.toString());  

		return response.toString();
	}

	//form a properly formatted GET request to SmartyStreets API given an Address
	private String formSmartyStreetsGetRequest(Address address) throws UnsupportedEncodingException{
		String eStreet = URLEncoder.encode(address.getNumber() + address.getStreet(), DEFAULT_ENCODING);
		String eCity = URLEncoder.encode(address.getCity(), DEFAULT_ENCODING);
		String eState = URLEncoder.encode(address.getState(), DEFAULT_ENCODING);
		
		String[] queries = { AUTH_ID + "=" + MY_AUTH_ID,
				AUTH_TOKEN + "=" + MY_AUTH_TOKEN,
				STREET + "=" + eStreet,
				CITY + "=" + eCity,
				STATE + "=" + eState,
				CANDIDATES + "=" + CANDIDATES_COUNT,
				"match=" + MATCH_DEFAULT 
				};

		String query = Arrays.stream(queries).collect(Collectors.joining("&"));
		
		String urlString = URL_COMPONENTS_BEG + "?" + query;
	
		return urlString.replace("+", "%20");
	}

	public static class InvalidContentException extends Exception {
	    public InvalidContentException(String message) {
	        super(message);
	    }
	}

}
