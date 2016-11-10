package coms6998.fall2016.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkRequestManager {

	private static HttpURLConnection conn;

	// send a simple GET request
	public static int sendGetRequest(String urlStr) throws IOException {

		// System.out.println("Sending get request : "+ urlStr);

		URL url = new URL(urlStr);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		// add request header
		conn.setRequestProperty("User-Agent", "Mozilla/5.0");

		int responseCode = conn.getResponseCode();
		System.out.println("Sending get request : " + url);
		System.out.println("Response code : " + responseCode);
		return responseCode;
	}

	// send a simple GET request
	public static int sendGetRequestWithHost(String file, String host) throws IOException {

		URL url = new URL("https", host, file);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		// add request header
		conn.setRequestProperty("User-Agent", "Mozilla/5.0");

		int responseCode = conn.getResponseCode();
		System.out.println("Sending get request : " + url);
		System.out.println("Response code : " + responseCode);
		return responseCode;
	}

	// read String response, in JSON form most likely
	public static String readResponse() throws IOException {
		InputStream inputStream = null;
		if (conn != null) {
			inputStream = conn.getInputStream();
		} else {
			throw new IOException("Connection is not established.");
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		String output;
		StringBuffer response = new StringBuffer();

		while ((output = reader.readLine()) != null) {
			response.append(output);
		}

		reader.close();
		inputStream.close();

		return response.toString();

	}

}
