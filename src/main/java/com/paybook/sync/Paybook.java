package com.paybook.sync;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
* Almost all the classes inherit from Paybook Class so they can access to its attributes and methods
*
* @author  Mateo
* @version 1.0
* @since   2016-12-01 
*/

public class Paybook {
	
	static final String paybook_url = "https://sync.paybook.com/v1/";
	
	protected static String api_key;
	protected static Boolean logger;
	protected static String __INDENT__ = "		";
	
	/**
	* Available API Sync methods 
	*/

	public enum Method {
		POST("POST"),
		GET("GET"),
		DELETE("DELETE"),
		PUT("PUT");
		private final String value;
		
		Method(String value) {
	        this.value = value;
	    }
		public String getValue() {
	        return this.value;
	    }
	}//End of method
	
	/**
	 * Class Constructor
	 * @author: Mateo
	 * */
	public Paybook() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Method for initialize SDK
	 * @param api_key Paybook API KEY 
	 * */
	public static void init(String api_key){
		Paybook.api_key = api_key;
		Paybook.logger = false;
    }//End of init
	
	/**
	* Method for initialize SDK
	* @param api_key Paybook API KEY
	* @param logger Flag to log activity
	*/
	
	public static void init(String api_key,Boolean logger){
		Paybook.api_key = api_key;
		Paybook.logger = logger;
    }//End of init
	
	/**
	* This method should be used for the Sync API connection in order to centralize the call to the API in just one method.
	* @param endpoint Paybook API KEY
	* @param method Flag to log activity
	* @param data Flag to log activity
	* @param headers Flag to log activity
	* @param URL Flag to log activity
	* @return JSONObject API Response
	* @throws Error Error class
	*/
	
	public static JSONObject call(String endpoint, Method method, HashMap<String, Object> data, Map<String, String> headers, String URL) throws Error{
		
		try {
			HttpResponse<JsonNode> jsonResponse = null;
			JSONObject jsonObject = new JSONObject();

			int status = 0;
			String statusText = "";
			if(URL == null){
				URL = paybook_url + endpoint;
			}//End of IF
			
			if(headers == null){
				jsonResponse = Unirest.post(URL)
						.header("accept", "application/json")
				        .header("Content-Type", "application/json")
						.queryString("_method", method.getValue())
						.body(new JSONObject(data))
						.asJson();
				jsonObject = jsonResponse.getBody().getObject();
				status = jsonResponse.getStatus();
				statusText = jsonResponse.getStatusText();
			}else if(headers.get("Content-Type").equals("application/json")){
				jsonResponse = Unirest.post(URL)
						.headers(headers)
						.queryString("_method", method.getValue())
						.body(new JSONObject(data))
						.asJson();
				jsonObject = jsonResponse.getBody().getObject();
				status = jsonResponse.getStatus();
				statusText = jsonResponse.getStatusText();
			}else if(headers.get("Content-Type").equals("application/xml")){
				HttpResponse<String> content = Unirest.post(URL)
					.headers(headers)
					.queryString("_method", method.getValue())
					.body(new JSONObject(data))
					.asString();
				String contentString = content.getBody().toString();
				jsonObject.put("content", contentString);
				status = content.getStatus();
				statusText = content.getStatusText();
			}//End of IF
			log(jsonObject.toString());
			
			if (status >= 200 && status < 400){
				return jsonObject;
			}else{
				throw new Error(status,false,statusText,null);
			}
		} catch (UnirestException e) {
			Pattern p = Pattern.compile(".*TimeoutException.*");
			Matcher m = p.matcher(e.getMessage());
			boolean b = m.matches();
			if(b){
				throw new Error(408,false,e.getMessage(),null);
			}else{
				throw new Error(500,false,e.getMessage(),null);
			}
		} catch (Error e){
			throw new Error(e.code,e.status,e.message,e.response);
		}	
	}//End of call
	
	public static JSONObject call(String endpoint, Method method, HashMap<String, Object> data) throws Error{
		return call(endpoint,method,data,null,null);
	}//End of call
	
	public static JSONObject call(String endpoint, Method method, HashMap<String, Object> data, Map<String, String> headers) throws Error{
		return call(endpoint,method,data,headers,null);
	}//End of call
	
	public static JSONObject call(Method method, HashMap<String, Object> data,String URL) throws Error{
		return call(null,method,data,null,URL);
	}//End of call
	
	public static JSONObject call(Method method, HashMap<String, Object> data, Map<String, String> headers, String URL) throws Error{
		return call(null,method,data,headers,URL);
	}//End of call
	
	public static JSONObject call(String endpoint, Method method) throws Error{
		HashMap<String, Object> data = new HashMap<String, Object>();
		return call(endpoint,method,data,null);	
	}//End of call
	
	public static JSONObject call(String endpoint, Method method, Map<String, String> headers) throws Error{
		HashMap<String, Object> data = new HashMap<String, Object>();
		return call(endpoint,method,data,headers,null);	
	}//End of call
	
	public static void log(String log){
		if(Paybook.logger){
			System.out.println(Paybook.__INDENT__ + log);
		}//End of if
	}//End of log
	
	
}
