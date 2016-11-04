package coms6998.fall2016.managers;

public class HTTPRedirect {
	
	public static final int SUCCESS = 302;
	public static final int FAILURE = 400;
	
	private String url;
	private int code;
	

	public HTTPRedirect(String authorizationURL, int code) {
		// TODO Auto-generated constructor stub
		this.setUrl(authorizationURL);
		this.setCode(code);
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public int getCode() {
		return code;
	}


	public void setCode(int code) {
		this.code = code;
	}

}
