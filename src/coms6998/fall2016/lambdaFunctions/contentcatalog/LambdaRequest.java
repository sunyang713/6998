package coms6998.fall2016.lambdaFunctions.contentcatalog;

import java.util.HashMap;
import java.util.StringJoiner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LambdaRequest {

	private String operation;
	private String resource;
	private String parameter;
	private HashMap<String, String> query;
	private HashMap<String, String> payload;

	public LambdaRequest() { }

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public HashMap<String, String> getQuery() {
		return query;
	}

	public void setQuery(HashMap<String, String> query) {
		this.query = query;
	}

	public HashMap<String, String> getPayload() {
		return payload;
	}

	public void setPayload(HashMap<String, String> payload) {
		this.payload = payload;
	}
	
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringJoiner joiner = new StringJoiner("/");
		return joiner.add(operation).add(resource).add(parameter).toString();
	}

}
