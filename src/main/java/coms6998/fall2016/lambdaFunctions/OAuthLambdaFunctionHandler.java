package coms6998.fall2016.lambdaFunctions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import coms6998.fall2016.managers.FacebookConnector;
import coms6998.fall2016.managers.HTTPRedirect;

public class OAuthLambdaFunctionHandler implements RequestHandler<String, String> {
	
	@Override
	public String handleRequest(String input, Context context){
		//System.out.println("Do I reach here?");
		
		HTTPRedirect redirect = FacebookConnector.facebookRedirect1();
		
		System.out.println(redirect.getUrl());
		
		//TODO - capture and return result of JSON response here
		
		return redirect.getUrl();
	}

}
