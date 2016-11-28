package com.paybook.sync;

import com.mashape.unirest.http.JsonNode;

public class Error extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int code;
	public Boolean status;
	public String message;
	public JsonNode response;
	
	public Error(){
	}
	
	public Error(int code, Boolean status, String message, JsonNode response){
		super(message);
		this.code = code;
		this.status = status;
		this.message = message;
		this.response = response;
	}
	
}
