package coms6998.fall2016.lambdaFunctions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;


import coms6998.fall2016.managers.FacebookConnector;
import coms6998.fall2016.models.ErrorPayload;
import coms6998.fall2016.models.Customer;

//return Customer model with suggested fields from FB
//in addition store FB user_id and access token into Customer DynamoDB
//For exchanging short term access token w long term
//: https://developers.facebook.com/docs/facebook-login/access-tokens/expiration-and-extension

public class OAuth2LambdaFunctionHandler implements RequestHandler<String, Customer> {
	
	//TODO - put this in a NetworkRequestManager.java class
	public static Map<String, String> getQueryParams(String url){
		
		try{
			Map<String, String> params = new HashMap<String, String>();
			String[] urlParts = url.split("#");
			if (urlParts.length > 1){
				String query = urlParts[1];
				String[] pair = query.split("=");
				String key = URLDecoder.decode(pair[0], "UTF-8");
				String value = "";
				if (pair.length > 1){
					value = URLDecoder.decode(pair[1], "UTF-8");
				}

				if (params.get(key) == null){
					params.put(key,value);
				}
			}
			return params;
		} catch (UnsupportedEncodingException e){
			throw new AssertionError(e);
		}

	}
	
	@Override
	public Customer handleRequest(String input, Context context){
	
		String access_token = input;

		if (access_token != null) {
			System.out.println("Parsing url for access token: " + access_token);
			try {
				Customer customer = FacebookConnector.facebookRedirect2(access_token);
				if (customer == null){
					ErrorPayload errorPayload = new ErrorPayload("Error", 500, context.getAwsRequestId(), "Unknown Internal Error");
					throw new RuntimeException(errorPayload.toString());
				}
				return customer;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			ErrorPayload errorPayload = new ErrorPayload("NotFound", 404, context.getAwsRequestId(), "User declined FB Login.");
			throw new RuntimeException(errorPayload.toString());
		}
		
		//if fb returned an error
		ErrorPayload errorPayload = new ErrorPayload("Error", 500, context.getAwsRequestId(), "Unknown Internal Error");
		throw new RuntimeException(errorPayload.toString()); 
	}

}
