package coms6998.fall2016.lambdaFunctions;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;


import coms6998.fall2016.managers.FacebookConnector;
import coms6998.fall2016.models.ErrorPayload;

public class OAuth2LambdaFunctionHandler implements RequestHandler<String, String> {
	
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
	
	
	public String handleRequest(String input, Context context){
		
		Map<String, String> params = getQueryParams(input);
		String access_token = params.get("access_token");

		
		if (access_token != null) {
			System.out.println("Parsing url for access token: " + access_token);
			
			return FacebookConnector.facebookRedirect2(access_token);
		} else {
			ErrorPayload errorPayload = new ErrorPayload("NotFound", 404, context.getAwsRequestId(), "User declined FB Login.");
			throw new RuntimeException(errorPayload.toString());
		}
	}

}
