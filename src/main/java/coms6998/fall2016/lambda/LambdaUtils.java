package coms6998.fall2016.lambda;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.validation.ConstraintViolation;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import coms6998.fall2016.data.models.ModelUtils;
import coms6998.fall2016.data.models.ReferenceAttribute;

public final class LambdaUtils {

	public static ObjectMapper mapper = new ObjectMapper();

	@SuppressWarnings("unchecked")
	public static <T> T getSingleValue(Map<String, T> map) {
		return (T) map.values().toArray()[0];
	}

	public static <T> T convertJsonToObject(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, clazz);
	}

	public static String convertObjectToJson(Object object) {
		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> APIGatewayProxyResponse createResponse(APIGatewayProxyEvent input, int statusCode,
			Set<ConstraintViolation<T>> violationSet) {
		List<String> violationList = new Vector<>();
		for (ConstraintViolation<T> violation : violationSet) {
			violationList.add(violation.getMessage());
		}
		Map<String, Object> payload = new HashMap<>();
		payload.put("violations", violationList);
		return new APIGatewayProxyResponse(statusCode, convertObjectToJson(payload));
	}

	public static APIGatewayProxyResponse createResponse(APIGatewayProxyEvent input, int statusCode, Object payload) {
		if (payload instanceof String) {
			return _createResponse(input, statusCode, (String) payload);
		} else {
			return _createResponse(input, statusCode, payload);
		}
	}

	public static APIGatewayProxyResponse _createResponse(APIGatewayProxyEvent input, int statusCode, Object payload) {

		Map<String, Object> bodyMap;
		// Convert object to Map
		try {
			bodyMap = mapper.readValue(mapper.writeValueAsString(payload), new TypeReference<Map<String, Object>>() {
			}); // TODO there should be a better way to do this other than obj
				// -> str -> map
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// Get all reference links
		List<ReferenceLink> links;
		links = getHATEOASLinks(payload, payload.getClass());
		links.add(new ReferenceLink("self", input.getPath()));
		bodyMap.put("links", links);

		String body;
		// Return map as string
		try {
			body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bodyMap);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		// TODO return headers too using input

		return new APIGatewayProxyResponse(statusCode, body);
	}

	public static APIGatewayProxyResponse _createResponse(APIGatewayProxyEvent input, int statusCode, String message) {
		APIGatewayProxyResponse response;
		MessagePayload messagePayload;
		String body;

		response = new APIGatewayProxyResponse();
		messagePayload = new MessagePayload(message);
		try {
			body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messagePayload);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		response.setStatusCode(statusCode);
		response.setBody(body);
		// TODO set response headers based on the input
		return response;
	}

	public static List<ReferenceLink> getHATEOASLinks(Object payload, Class<?> clazz) {

		// Get all reference attributes from payload
		List<ReferenceLink> links = new Vector<>();
		String linkResourceName;
		String linkResourceId;
		// String linkRelation;
		for (Method method : clazz.getDeclaredMethods()) {
			// need to check if it is 'getter'
			if (ModelUtils.isRelevantGetter(method)
					&& ModelUtils.getterOrFieldHasAnnotation(method, ReferenceAttribute.class)) {
				ReferenceAttribute ref = ModelUtils.getAnnotationFromGetterOrField(method, ReferenceAttribute.class);
				linkResourceName = ref.value().getSimpleName(); // class name
				try {
					linkResourceId = method.invoke(payload).toString();
				} catch (NullPointerException | IllegalArgumentException | IllegalAccessException
						| InvocationTargetException e) {
					throw new RuntimeException(e);
				}
				// TODO fix this
				links.add(new ReferenceLink(linkResourceName, "/" + linkResourceName + "/" + linkResourceId));
			}
		}
		return links;

	}

	static class MessagePayload {
		private String message;

		public MessagePayload(String message) {
			setMessage(message);
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	static class ReferenceLink {

		private String rel;
		private String href;

		public ReferenceLink() {

		}

		public ReferenceLink(String rel, String href) {
			setRel(rel);
			setHref(href);
		}

		@Override
		public String toString() {
			return "ReferenceLink [rel=" + rel + ", href=" + href + "]";
		}

		public String getRel() {
			return rel;
		}

		public void setRel(String rel) {
			this.rel = rel;
		}

		public String getHref() {
			return href;
		}

		public void setHref(String href) {
			this.href = href;
		}

	}

}
