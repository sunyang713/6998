package coms6998.fall2016.data.access;

public class DAOResponse {

	private int statusCode;
	private Object payload;

	public DAOResponse(int statusCode, Object payload) {
		setStatusCode(statusCode);
		setPayload(payload);
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

}
