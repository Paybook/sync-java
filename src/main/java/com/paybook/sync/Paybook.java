package com.paybook.sync;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Paybook {
	static final String paybook_url = "https://sync.paybook.com/v1/";
	
	protected static String api_key;
	protected static Boolean logger;
	protected static String __INDENT__ = "		";
	
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
	
	public Paybook() {
		// TODO Auto-generated constructor stub
	}
	
	public static void init(String api_key){
		Paybook.api_key = api_key;
    }//End of init
	
	public static void init(String api_key,Boolean logger){
		Paybook.api_key = api_key;
		Paybook.logger = logger;
    }//End of init

	public static JSONObject call(String endpoint, Method method, HashMap<String, Object> data, String URL) throws Error{
		
		try {
			HttpResponse<JsonNode> jsonResponse = null;
			if(URL == null){
				URL = paybook_url + endpoint;
			}//End of IF
			jsonResponse = Unirest.post(URL)
					.header("accept", "application/json")
			        .header("Content-Type", "application/json")
					.queryString("_method", method.getValue())
					.body(new JSONObject(data))
					.asJson();
			JSONObject jsonObject = jsonResponse.getBody().getObject();
			log(jsonObject.toString());
			if (jsonResponse.getStatus() >= 200 && jsonResponse.getStatus() < 400){
				return jsonObject;
			}else{
				throw new Error(jsonResponse.getStatus(),false,jsonResponse.getStatusText(),null);
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
		return call(endpoint,method,data,null);
	}//End of call
	
	public static JSONObject call(Method method, HashMap<String, Object> data,String URL) throws Error{
		return call(null,method,data,URL);
	}//End of call
	
	public static JSONObject call(String endpoint, Method method) throws Error{
		HashMap<String, Object> data = new HashMap<String, Object>();
		return call(endpoint,method,data,null);	
	}//End of call
	
	public static void log(String log){
		if(Paybook.logger){
			System.out.println(Paybook.__INDENT__ + log);
		}//End of if
	}//End of log
	
	
}
