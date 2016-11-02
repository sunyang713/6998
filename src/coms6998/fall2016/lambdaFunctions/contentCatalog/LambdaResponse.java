package coms6998.fall2016.lambdaFunctions.contentCatalog;

/**
 * 
 * @author Jonathan
 *
 * @param <T> This is the POJO structure/model for the 'payload' in the response.
 */
public class LambdaResponse<T> {

	private String status;
	private String message;
	private T payload;
	
	public LambdaResponse() { }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getPayload() {
		return payload;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}

}
