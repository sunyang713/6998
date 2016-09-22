package coms6998.fall2016.lambdaFunctions;

public class CustomerLambdaRequest {
	
	private String operation;
	private String payload; // String? json plz
	
	public CustomerLambdaRequest() { }
	
	public CustomerLambdaRequest(String operation, String payload) {
		this.operation = operation;
		this.payload = payload;
	}
	
	public String getOperation() {
		return operation;
	}
	
	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	public String getData() {
		return payload;
	}
	
	public void setData(String data) {
		this.payload = data;
	}

}
