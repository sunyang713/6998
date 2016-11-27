package coms6998.fall2016.lambda;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class APIGatewayProxyEvent {

	private String body;
	private String resource;
	private RequestContext requestContext;
	private Map<String, String> queryStringParameters;
	private Headers headers;
	private Map<String, String> pathParameters;
	private String httpMethod;
	private Map<String, String> stageVariables;
	private String path;
	private boolean isBase64Encoded;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public RequestContext getRequestContext() {
		return requestContext;
	}

	public void setRequestContext(RequestContext requestContext) {
		this.requestContext = requestContext;
	}

	public Map<String, String> getQueryStringParameters() {
		return queryStringParameters;
	}

	public void setQueryStringParameters(Map<String, String> queryStringParameters) {
		this.queryStringParameters = queryStringParameters;
	}

	public Headers getHeaders() {
		return headers;
	}

	public void setHeaders(Headers headers) {
		this.headers = headers;
	}

	public Map<String, String> getPathParameters() {
		return pathParameters;
	}

	public void setPathParameters(Map<String, String> pathParameters) {
		this.pathParameters = pathParameters;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public Map<String, String> getStageVariables() {
		return stageVariables;
	}

	public void setStageVariables(Map<String, String> stageVariables) {
		this.stageVariables = stageVariables;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isBase64Encoded() {
		return isBase64Encoded;
	}

	public void setBase64Encoded(boolean isBase64Encoded) {
		this.isBase64Encoded = isBase64Encoded;
	}

	@Override
	public String toString() {
		return "APIGatewayProxyEvent [body=" + body + ", resource=" + resource + ", requestContext=" + requestContext
				+ ", queryStringParameters=" + queryStringParameters + ", headers=" + headers + ", pathParameters="
				+ pathParameters + ", httpMethod=" + httpMethod + ", stageVariables=" + stageVariables + ", path="
				+ path + "]";
	}

	public class RequestContext {

		private String resourceId;
		private String apiId;
		private String resourcePath;
		private String httpMethod;
		private String requestId;
		private String accountId;
		private Identity identity;
		private String stage;

		public String getResourceId() {
			return resourceId;
		}

		public void setResourceId(String resourceId) {
			this.resourceId = resourceId;
		}

		public String getApiId() {
			return apiId;
		}

		public void setApiId(String apiId) {
			this.apiId = apiId;
		}

		public String getResourcePath() {
			return resourcePath;
		}

		public void setResourcePath(String resourcePath) {
			this.resourcePath = resourcePath;
		}

		public String getHttpMethod() {
			return httpMethod;
		}

		public void setHttpMethod(String httpMethod) {
			this.httpMethod = httpMethod;
		}

		public String getRequestId() {
			return requestId;
		}

		public void setRequestId(String requestId) {
			this.requestId = requestId;
		}

		public String getAccountId() {
			return accountId;
		}

		public void setAccountId(String accountId) {
			this.accountId = accountId;
		}

		public Identity getIdentity() {
			return identity;
		}

		public void setIdentity(Identity identity) {
			this.identity = identity;
		}

		public String getStage() {
			return stage;
		}

		public void setStage(String stage) {
			this.stage = stage;
		}

		@Override
		public String toString() {
			return "RequestContext [resourceId=" + resourceId + ", apiId=" + apiId + ", resourcePath=" + resourcePath
					+ ", httpMethod=" + httpMethod + ", requestId=" + requestId + ", accountId=" + accountId
					+ ", identity=" + identity + ", stage=" + stage + "]";
		}

		public class Identity {

			private String apiKey;
			private String userArn;
			private String cognitoAuthenticationType;
			private String caller;
			private String userAgent;
			private String user;
			private String cognitoIdentityPoolId;
			private String cognitoAuthenticationProvider;
			private String sourceIp;
			private String accountId;

			public String getApiKey() {
				return apiKey;
			}

			public void setApiKey(String apiKey) {
				this.apiKey = apiKey;
			}

			public String getUserArn() {
				return userArn;
			}

			public void setUserArn(String userArn) {
				this.userArn = userArn;
			}

			public String getCognitoAuthenticationType() {
				return cognitoAuthenticationType;
			}

			public void setCognitoAuthenticationType(String cognitoAuthenticationType) {
				this.cognitoAuthenticationType = cognitoAuthenticationType;
			}

			public String getCaller() {
				return caller;
			}

			public void setCaller(String caller) {
				this.caller = caller;
			}

			public String getUserAgent() {
				return userAgent;
			}

			public void setUserAgent(String userAgent) {
				this.userAgent = userAgent;
			}

			public String getUser() {
				return user;
			}

			public void setUser(String user) {
				this.user = user;
			}

			public String getCognitoIdentityPoolId() {
				return cognitoIdentityPoolId;
			}

			public void setCognitoIdentityPoolId(String cognitoIdentityPoolId) {
				this.cognitoIdentityPoolId = cognitoIdentityPoolId;
			}

			public String getCognitoAuthenticationProvider() {
				return cognitoAuthenticationProvider;
			}

			public void setCognitoAuthenticationProvider(String cognitoAuthenticationProvider) {
				this.cognitoAuthenticationProvider = cognitoAuthenticationProvider;
			}

			public String getSourceIp() {
				return sourceIp;
			}

			public void setSourceIp(String sourceIp) {
				this.sourceIp = sourceIp;
			}

			public String getAccountId() {
				return accountId;
			}

			public void setAccountId(String accountId) {
				this.accountId = accountId;
			}

			@Override
			public String toString() {
				return "Identity [apiKey=" + apiKey + ", userArn=" + userArn + ", cognitoAuthenticationType="
						+ cognitoAuthenticationType + ", caller=" + caller + ", userAgent=" + userAgent + ", user="
						+ user + ", cognitoIdentityPoolId=" + cognitoIdentityPoolId + ", cognitoAuthenticationProvider="
						+ cognitoAuthenticationProvider + ", sourceIp=" + sourceIp + ", accountId=" + accountId + "]";
			}

		}

	}

	public class Headers {

		@JsonProperty("AcceptEncoding")
		private String via;
		@JsonProperty("Accept-Language")
		private String acceptLanguage;
		@JsonProperty("CloudFront-Is-Desktop-Viewer")
		private String cloudFrontIsDesktopViewer;
		@JsonProperty("CloudFront-Is-Smart-TV-Viewer")
		private String cloudFrontIsSmartTVViewer;
		@JsonProperty("CloudFront-Is-Mobile-Viewer")
		private String cloudFrontIsMobileViewer;
		@JsonProperty("XForwardedFor")
		private String xForwardedFor;
		@JsonProperty("CloudFrontViewerCountry")
		private String cloudFrontViewerCountry;
		@JsonProperty("Accept")
		private String accept;
		@JsonProperty("Upgrade-Insecure-Requests")
		private String upgradeInsecureRequests;
		@JsonProperty("X-Forwarded-Port")
		private String xForwardedPort;
		@JsonProperty("Host")
		private String host;
		@JsonProperty("X-Forwarded-Proto")
		private String xForwardedProto;
		@JsonProperty("X-Amz-Cf-Id")
		private String xAmzCfId;
		@JsonProperty("CloudFront-Is-Tablet-Viewer")
		private String cloudFrontIsTabletViewer;
		@JsonProperty("Cache-Control")
		private String cacheControl;
		@JsonProperty("User-Agent")
		private String userAgent;
		@JsonProperty("Cloud-Front-Forwarded-Proto")
		private String cloudFrontForwardedProto;
		@JsonProperty("Accept-Encoding")
		private String acceptEncoding;

		public String getVia() {
			return via;
		}

		public void setVia(String via) {
			this.via = via;
		}

		public String getAcceptLanguage() {
			return acceptLanguage;
		}

		public void setAcceptLanguage(String acceptLanguage) {
			this.acceptLanguage = acceptLanguage;
		}

		public String getCloudFrontIsDesktopViewer() {
			return cloudFrontIsDesktopViewer;
		}

		public void setCloudFrontIsDesktopViewer(String cloudFrontIsDesktopViewer) {
			this.cloudFrontIsDesktopViewer = cloudFrontIsDesktopViewer;
		}

		public String getCloudFrontIsSmartTVViewer() {
			return cloudFrontIsSmartTVViewer;
		}

		public void setCloudFrontIsSmartTVViewer(String cloudFrontIsSmartTVViewer) {
			this.cloudFrontIsSmartTVViewer = cloudFrontIsSmartTVViewer;
		}

		public String getCloudFrontIsMobileViewer() {
			return cloudFrontIsMobileViewer;
		}

		public void setCloudFrontIsMobileViewer(String cloudFrontIsMobileViewer) {
			this.cloudFrontIsMobileViewer = cloudFrontIsMobileViewer;
		}

		public String getxForwardedFor() {
			return xForwardedFor;
		}

		public void setxForwardedFor(String xForwardedFor) {
			this.xForwardedFor = xForwardedFor;
		}

		public String getCloudFrontViewerCountry() {
			return cloudFrontViewerCountry;
		}

		public void setCloudFrontViewerCountry(String cloudFrontViewerCountry) {
			this.cloudFrontViewerCountry = cloudFrontViewerCountry;
		}

		public String getAccept() {
			return accept;
		}

		public void setAccept(String accept) {
			this.accept = accept;
		}

		public String getUpgradeInsecureRequests() {
			return upgradeInsecureRequests;
		}

		public void setUpgradeInsecureRequests(String upgradeInsecureRequests) {
			this.upgradeInsecureRequests = upgradeInsecureRequests;
		}

		public String getxForwardedPort() {
			return xForwardedPort;
		}

		public void setxForwardedPort(String xForwardedPort) {
			this.xForwardedPort = xForwardedPort;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public String getxForwardedProto() {
			return xForwardedProto;
		}

		public void setxForwardedProto(String xForwardedProto) {
			this.xForwardedProto = xForwardedProto;
		}

		public String getxAmzCfId() {
			return xAmzCfId;
		}

		public void setxAmzCfId(String xAmzCfId) {
			this.xAmzCfId = xAmzCfId;
		}

		public String getCloudFrontIsTabletViewer() {
			return cloudFrontIsTabletViewer;
		}

		public void setCloudFrontIsTabletViewer(String cloudFrontIsTabletViewer) {
			this.cloudFrontIsTabletViewer = cloudFrontIsTabletViewer;
		}

		public String getCacheControl() {
			return cacheControl;
		}

		public void setCacheControl(String cacheControl) {
			this.cacheControl = cacheControl;
		}

		public String getUserAgent() {
			return userAgent;
		}

		public void setUserAgent(String userAgent) {
			this.userAgent = userAgent;
		}

		public String getCloudFrontForwardedProto() {
			return cloudFrontForwardedProto;
		}

		public void setCloudFrontForwardedProto(String cloudFrontForwardedProto) {
			this.cloudFrontForwardedProto = cloudFrontForwardedProto;
		}

		public String getAcceptEncoding() {
			return acceptEncoding;
		}

		public void setAcceptEncoding(String acceptEncoding) {
			this.acceptEncoding = acceptEncoding;
		}

		@Override
		public String toString() {
			return "Headers [via=" + via + ", acceptLanguage=" + acceptLanguage + ", cloudFrontIsDesktopViewer="
					+ cloudFrontIsDesktopViewer + ", cloudFrontIsSmartTVViewer=" + cloudFrontIsSmartTVViewer
					+ ", cloudFrontIsMobileViewer=" + cloudFrontIsMobileViewer + ", xForwardedFor=" + xForwardedFor
					+ ", cloudFrontViewerCountry=" + cloudFrontViewerCountry + ", accept=" + accept
					+ ", upgradeInsecureRequests=" + upgradeInsecureRequests + ", xForwardedPort=" + xForwardedPort
					+ ", host=" + host + ", xForwardedProto=" + xForwardedProto + ", xAmzCfId=" + xAmzCfId
					+ ", cloudFrontIsTabletViewer=" + cloudFrontIsTabletViewer + ", cacheControl=" + cacheControl
					+ ", userAgent=" + userAgent + ", cloudFrontForwardedProto=" + cloudFrontForwardedProto
					+ ", acceptEncoding=" + acceptEncoding + "]";
		}

	}

}
