package coms6998.fall2016.managers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import coms6998.fall2016.models.Customer;
import coms6998.fall2016.managers.NetworkRequestManager;

public class FacebookConnector {

	private static final String S3_BUCKET = "blahblah";

	private static final String FACEBOOK_APP_KEY = "612805472254463";

	private static final String FACEBOOK_APP_SECRET = "88172f24b1ad977b0abfde62f5418b52";

	private static final String FACEBOOK_LOGIN_REDIRECT_URL = "https://www.facebook.com/v2.8/dialog/oauth";

	private static final String FACEBOOK_GRAPH_HOST = "graph.facebook.com";

	private static Properties facebookProperties;

	private static final String failureURL = "www.google.com";

	private static final String DEFAULT_ENCODING = "UTF-8";

	private static boolean connectorInitialized = false;

	public class SimpleRequestToken {
		String token;
		String secret;

		public SimpleRequestToken(String t, String s) {
			this.token = t;
			this.secret = s;
		}
	}

	public class FacebookAuth {
		public String oauth_token;
		// public String oauth_verifier;
	}

	public static boolean initializeConnector() {

		System.out.println("Initializing the Facebook Connector");
		facebookProperties = new Properties();
		facebookProperties.setProperty("oauth.consumerKey", FACEBOOK_APP_KEY);
		facebookProperties.setProperty("oauth.consumerSecret", FACEBOOK_APP_SECRET);
		/*
		 * if (access_id != null){
		 * facebookProperties.setProperty("oauth.accessToken", access_id); }
		 * 
		 * if (access_token != null){
		 * facebookProperties.setProperty("oauth.accessTokenSecret",access_token
		 * ); }
		 */

		connectorInitialized = true;

		return connectorInitialized;
	}

	// step 1 in FB OAuth2 login
	public static HTTPRedirect facebookRedirect1() {

		FacebookConnector.initializeConnector();

		HTTPRedirect url = null;
		try {

			String authorizationURL = FACEBOOK_LOGIN_REDIRECT_URL + "?client_id="
					+ facebookProperties.getProperty("oauth.consumerKey") + "&redirect_uri="
					+ "https://x768se4ytc.execute-api.us-east-1.amazonaws.com/prod/authReturn";
			String email = "email";
			String user_likes = "user_likes";
			String permissions = "&scope=" + email + "," + user_likes;

			String response_type = "&response_type=token";

			authorizationURL += permissions + response_type;

			url = new HTTPRedirect(authorizationURL, 302);

			// https://www.facebook.com/v2.8/dialog/oauth?
			// client_id={app-id}
			// &redirect_uri={redirect-uri}

		} catch (Exception e) {
			System.out.println("Something went wrong");
		}

		if (url == null) {
			System.out.println("Could not redirect to Facebook Login.");

			return new HTTPRedirect(failureURL, 400);
		}

		return url;
	}

	// step 2 in FB Login
	public static Customer facebookRedirect2(String inputToken) throws IOException {

		FacebookConnector.initializeConnector();

		String user_id = "";
		Customer customer = null;
		try {

			user_id = fbUserID(fbGetTokenResponse(inputToken, facebookProperties.getProperty("oauth.consumerKey"),
					facebookProperties.getProperty("oauth.consumerSecret")));

		} catch (Exception e) {
			System.out.println("Something went wrong");
		}

		if (!user_id.isEmpty()) {
			int responseCode = NetworkRequestManager.sendGetRequestWithHost(formFBUserRequest(user_id, inputToken),
					FACEBOOK_GRAPH_HOST);

			switch (responseCode) {
			case 200:
				String response = NetworkRequestManager.readResponse();
				System.out.println("Getting response: " + response);

				// TODO - FB always returns 200 regardless of error. We need to
				// parse the JSON response
				// for an errorcode and return that appropriately
				customer = fbCustomer(response);
				customer.setFbUserId(user_id);
				customer.setFbAccessToken(inputToken);
				break;
			case 110:
				// user_id doesn't exist
			case 210:
				// user not visible
			case 3001:
				// invalid query
				break;
			}
		}

		return customer;
	}

	// forms a Customer object from a FB Graph User response
	private static Customer fbCustomer(String response) {

		String EMAIL_FIELD = "email";
		String FIRST_NAME_FIELD = "first_name";
		String LAST_NAME_FIELD = "last_name";
		String FULL_NAME_FIELD = "name";

		JsonParser parser = new JsonParser();

		JsonObject jObject = parser.parse(response).getAsJsonObject();

		Customer c = new Customer();
		if (jObject != null) {

			if (jObject.has(EMAIL_FIELD)) {
				c.setEmail(jObject.get(EMAIL_FIELD).getAsString());
			}

			if (jObject.has(FIRST_NAME_FIELD)) {
				c.setFirstName(jObject.get(FIRST_NAME_FIELD).getAsString());
			}

			if (jObject.has(LAST_NAME_FIELD)) {
				c.setLastName(jObject.get(LAST_NAME_FIELD).getAsString());
			}

			if (c.getFirstName() == null) {
				if (jObject.has(FULL_NAME_FIELD)) {
					String fname = jObject.get(FULL_NAME_FIELD).getAsString();
					String[] names = fname.split("\\s+");
					if (names.length > 0) {
						c.setFirstName(names[0]);
						c.setLastName(names[names.length - 1]);
					}
				}
			}
		}
		return c;
	}

	// gets a user_id from a Facebook user from the auth2 response
	private static String fbUserID(String response) {

		System.out.println("Getting user id");
		JsonParser parser = new JsonParser();
		JsonObject jObject = parser.parse(response).getAsJsonObject();

		if (jObject != null) {
			JsonObject obj = jObject.getAsJsonObject("data");
			if (obj.has("user_id")) {
				String user_id = obj.get("user_id").getAsString();
				return user_id;
			}
		}
		return "";
	}

	// returns a Stringified JSON response from fb GET request
	private static String fbGetTokenResponse(String inputToken, String apiKey, String apiSecret) throws IOException {
		String urlString = formFBAccessTokenRequest(inputToken, apiKey, apiSecret);

		int responseCode = NetworkRequestManager.sendGetRequest(urlString);

		String response = "";
		if (responseCode == 200) {
			response = NetworkRequestManager.readResponse();
		}

		return response;
	}

	private static String formFBAccessTokenRequest(String inputToken, String apiKey, String apiSecret)
			throws UnsupportedEncodingException {
		String encodedInputToken = URLEncoder.encode(inputToken, DEFAULT_ENCODING);
		String encodedAPIKey = URLEncoder.encode(apiKey, DEFAULT_ENCODING);
		String encodedAPISecret = URLEncoder.encode(apiSecret, DEFAULT_ENCODING);
		String encodedPipe = URLEncoder.encode("|", DEFAULT_ENCODING);

		// GET graph.facebook.com/debug_token?
		// input_token={token-to-inspect}
		// &access_token={app-token-or-admin-token}

		String[] queries = { "input_token" + "=" + encodedInputToken,
				"access_token" + "=" + encodedAPIKey + encodedPipe + encodedAPISecret };

		String query = Arrays.stream(queries).collect(Collectors.joining("&"));

		String urlString = "https://graph.facebook.com/debug_token" + "?" + query;

		return urlString.replace("+", "%20");
	}

	// https://graph.facebook.com/v2.8/{user-id}?access_token=...
	private static String formFBUserRequest(String user_id, String auth_token) {
		String request = "/v2.8/" + user_id + "?access_token=" + auth_token;
		return request.replace("+", "%20");
	}

}
