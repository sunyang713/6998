package coms6998.fall2016.lambdaFunctions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class LambdaFunctionHandler implements RequestHandler<Request, Response> {

    @Override
    public Response handleRequest(Request input, Context context) {
        context.getLogger().log("Input: " + input);
        
        Gson myGson = new Gson();
        
        Response obj = null;
        if (input.getOperation() == "create") {
        	
        	//make a call to READ first
        	
        	//if entry is not found, then write
        	obj = new Response(myGson.toJson("hi"));
        	
        }
        
        // TODO: implement your handler
        return obj;
    }

}

