package coms6998.fall2016.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Properties;
//import HTTPRedirect;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import coms6998.fall2016.models.Address;

public class FacebookConnector {
	
	private static final String S3_BUCKET = "blahblah";
	
	private static final String FACEBOOK_APP_KEY = "608354519366225";
	
	private static final String FACEBOOK_APP_SECRET = "5b8f4670f168f8e7f7047a71ff15f682";
	
	private static final String FACEBOOK_LOGIN_REDIRECT_URL = "https://www.facebook.com/v2.8/dialog/oauth";
	
	private static Properties facebookProperties;
	
	private static final String failureURL = "www.google.com";
	
	private static final String DEFAULT_ENCODING = "UTF-8";
	
	private static boolean connectorInitialized = false;
	
	public class SimpleRequestToken{
		String token;
		String secret;
		
		public SimpleRequestToken(String t, String s){
			this.token = t;
			this.secret = s;
		}
	}
	
	public class FacebookAuth {
		public String oauth_token;
		//public String oauth_verifier;
	}
	
	public static boolean initializeConnector(/*String access_id, String access_token*/) {
		
		System.out.println("Initializing the Facebook Connector");
		facebookProperties = new Properties();
		facebookProperties.setProperty("oauth.consumerKey", FACEBOOK_APP_KEY);
		facebookProperties.setProperty("oauth.consumerSecret", FACEBOOK_APP_SECRET);
		/*if (access_id != null){
			facebookProperties.setProperty("oauth.accessToken", access_id);	
		}
		
		if (access_token != null){
			facebookProperties.setProperty("oauth.accessTokenSecret",access_token);
		}*/
		
		connectorInitialized = true;
		
		return connectorInitialized;
	}
	
	//step 1 in FB OAuth2 login
	public static HTTPRedirect facebookRedirect1(){
		
		//System.out.println("How about here?");
		FacebookConnector.initializeConnector();
		
		//String result = "failure";
		HTTPRedirect url = null;
		try {
			
			String authorizationURL = FACEBOOK_LOGIN_REDIRECT_URL + 
					"client_id=" + facebookProperties.getProperty("oauth.consumerKey") + 
					"&redirect_uri=" + "www.google.com";
			
			url  = new HTTPRedirect(authorizationURL, 302);
			
			//https://www.facebook.com/v2.8/dialog/oauth?
			 // client_id={app-id}
			 // &redirect_uri={redirect-uri}
			
			
			//FacebookConnector.initializeConnector();
			
		} catch (Exception e){
			System.out.println("Something went wrong");
		}
		
		if (url == null){
			System.out.println("Could not redirect to Facebook Login.");
			
			return new HTTPRedirect(failureURL, 400);
		}
		
		return url;
				
		
	}
	
	public static String facebookRedirect2(String inputToken){
		//String inputToken = accessToken.oauth_token;
		
		//HTTPRedirect url = null;
		FacebookConnector.initializeConnector();
		
		String user_id = "";
		try {
			
			user_id = fbUserID(fbGetTokenResponse(inputToken, 
					facebookProperties.getProperty("oauth.consumerKey"), 
					facebookProperties.getProperty("oauth.consumerSecret")));
		//SimpleRequestToken accessT = new FacebookConnector.SimpleRequestToken(facebookProperties.getProperty("oauth.consumerKey"), facebookProperties.getProperty("oauth.consumerSecret") ); 
		//SimpleRequestToken accessT = FacebookConnector.new SimpleRequestToken("hello", "world");
			
		
		} catch (Exception e){
			System.out.println("Something went wrong");
		}
		
		return user_id;
	}
	
	

	private static String fbUserID(String response) {
		//Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		
		//jsonObject.get("authors").getAsJsonArray();
		
		JsonObject jObject = parser.parse(response).getAsJsonObject();
		
		
		if (jObject != null){
			//SmartyStreetsAddress ssAddress = gson.fromJson(responseAsArray.get(0), SmartyStreetsAddress.class);
			//System.out.println("Made it to the deliveryPointBarcode if check");
			//JsonArray responseArray = jObject.get("data").getAsJsonArray();
			JsonObject obj = jObject.getAsJsonObject("data");
			
			
			String user_id = obj.getAsJsonObject("user_id").getAsString();

			//System.out.println("Printing the delivery point barcode: " + dpBarcode);
			return user_id;
		}
		return "";
	}

	//returns a Stringified JSON response from fb GET request
	private static String fbGetTokenResponse(String inputToken, String apiKey, String apiSecret) throws IOException{
		String urlString = formFBAccessTokenRequest(inputToken, apiKey, apiSecret);

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
	private static String formFBAccessTokenRequest(String inputToken, String apiKey, String apiSecret) throws UnsupportedEncodingException{
		String encodedInputToken = URLEncoder.encode(inputToken, DEFAULT_ENCODING);
		String encodedAPIKey = URLEncoder.encode(apiKey, DEFAULT_ENCODING);
		String encodedAPISecret = URLEncoder.encode(apiSecret, DEFAULT_ENCODING);
		
		//GET graph.facebook.com/debug_token?
	     //input_token={token-to-inspect}
	     //&access_token={app-token-or-admin-token}

		String[] queries = { "input_token" + "=" + encodedInputToken,
				"access_token" + "=" + encodedAPIKey + "|" + encodedAPISecret 
		};

		String query = Arrays.stream(queries).collect(Collectors.joining("&"));

		String urlString = "graph.facebook.com/debug_token" + "?" + query;

		return urlString.replace("+", "%20");
	}

}
