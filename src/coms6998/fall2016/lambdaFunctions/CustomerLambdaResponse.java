package coms6998.fall2016.lambdaFunctions;

public class CustomerLambdaResponse {
	
	private String responseStatus;
	
	/**
	 * Need to explicitly provide the zero argument constructors 
	 */
	public CustomerLambdaResponse() { }
	
	public CustomerLambdaResponse(String responseStatus) {
		this.responseStatus = responseStatus;
	}
		
	public String getResponseStatus() {
		return responseStatus;
	}
	
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
	
}
