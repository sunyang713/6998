package coms6998.fall2016.models;

public class ErrorPayload {
	
	private String errorType;
	private int httpStatus;
	private String requestId;
	private String message;
	
	public ErrorPayload(){}
	
	public ErrorPayload(String errorType, int httpStatus, String requestId, String message) {
		super();
		this.errorType = errorType;
		this.httpStatus = httpStatus;
		this.requestId = requestId;
		this.message = message;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
