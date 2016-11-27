package coms6998.fall2016.lambda;

import java.util.Map;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class APIGatewayProxyResponse {

	@Max(599)
	@Min(100)
	private int statusCode;

	private Map<String, String> headers;

	@NotNull
	private String body;

	public APIGatewayProxyResponse() {

	}

	public APIGatewayProxyResponse(int statusCode, String body) {
		setStatusCode(statusCode);
		setBody(body);
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "APIGatewayProxyResponse [statusCode=" + statusCode + ", headers=" + headers + ", body=" + body + "]";
	}

}
