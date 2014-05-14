package com.mishadoff.randomorg.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Low level class-util that handles all http work
 * for obtaining results without third-party libs.
 * 
 * @author mishadoff
 *
 */
public final class HTTPUtils {
	
	private final static String GET = "GET";
	
	private HTTPUtils(){}
	
	/**
	 * Retrieves content on provided url.
	 * Recreates connection on each call.
	 * @param urlString
	 * @return BufferedReader
	 * @throws IOException 
	 */
	
//	public static void main(String[] args) throws IOException
//	{
//		ArrayList<String> output = get("http://www.random.org/integers/?format=plain&min=2&max=300&num=2&base=10&col=1");
//		
//		for (String a : output)
//			System.out.println(a);
//	}
	public static ArrayList<String> get(String urlString) throws IOException{
		// TODO handle 503 response
		// TODO add timeout functionality
		HttpURLConnection connection;
		BufferedReader rd = null;
		ArrayList<String> strings = new ArrayList<String>();
		try {
			URL url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(GET);
	        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
//			System.out.println(urlString);
			rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line = "";
				while ((line = rd.readLine()) != null) {
					strings.add(line);
				}
		} finally {
			if (rd != null) {
				rd.close();
			}
		}
		return strings;
	}
}
