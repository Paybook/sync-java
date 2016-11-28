package com.paybook.sync;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class User extends Paybook{
	public String id_user;
    public String id_external;
    public String name;
    public String dt_create;
    public String dt_modify;
    
    public User(String name,String id_user,String id_external) throws Error{
    	List<User> users = null;
    	User user = null;
    	JSONObject response = null;
    	String responseInString = null;
    	try {
	    	HashMap<String, Object> data = new HashMap<String, Object>();
	    	ObjectMapper mapper = new ObjectMapper();
	    	data.put("api_key", Paybook.api_key);
	    	if(StringUtils.isNotBlank(name) && StringUtils.isBlank(id_user)){
	    		data.put("name", name);
	    		if(StringUtils.isNotBlank(id_external)){
	    			data.put("id_external", id_external);
	    		}
	    		response = call("users/", Method.POST, data);
	    		responseInString = response.get("response").toString();
	    		user = mapper.readValue(responseInString, User.class);
	    	}else if(StringUtils.isNotBlank(id_user)){
	    		if(StringUtils.isNotBlank(name) || StringUtils.isNotBlank(id_external)){
	    			data.put("id_user", id_user);
	    			if(StringUtils.isNotBlank(name)){
	    				data.put("name", name);
	    			}
	    			if(StringUtils.isNotBlank(id_external)){
	    				data.put("id_external", id_external);
	    			}
	    			response = call("users/"+id_user, Method.PUT, data);
	    			responseInString = response.get("response").toString();
	    			user = mapper.readValue(responseInString, User.class);
	    		}else{
	    			data.put("id_user", id_user);
	    			response = call("users/", Method.GET, data);
	    			responseInString = response.get("response").toString();
	    			users = mapper.readValue(responseInString, new TypeReference<List<User>>(){});
	    	    	user = users.get(0);
	    		}
	    	}else if(StringUtils.isNotBlank(id_external)){
	    		data.put("id_external", id_external);
	    		response = call("users/", Method.GET, data);
	    		responseInString = response.get("response").toString();
	    		users = mapper.readValue(responseInString, new TypeReference<List<User>>(){});
    	    	user = users.get(0);
	    	}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			throw new Error(500,false,e.getMessage(),null);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			throw new Error(500,false,e.getMessage(),null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new Error(500,false,e.getMessage(),null);
		} catch (Error e) {
			// TODO Auto-generated catch block
			throw e;
		}
    	this.id_user = user.id_user;
    	this.name = user.name;
    	this.id_external = user.id_external;
    	this.dt_create = user.dt_create;
    	this.dt_modify = user.dt_modify;
    }//End of constructor
    
    public User(String name) throws Error{
    	this(name,"","");
    }
    
    public User(String name, String id_user) throws Error{
    	this(name,id_user,"");
    }
    
    public User() throws Error{
    }

    
	public static List<User> get(HashMap<String, Object> data) throws Error{
		data.put("api_key", Paybook.api_key);
		
		List<User> users = null;	
		try {
			JSONObject response = call("users/", Method.GET, data);
			String responseInString = response.get("response").toString();
			ObjectMapper mapper = new ObjectMapper();
			users = mapper.readValue(responseInString, new TypeReference<List<User>>(){});
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			throw new Error(500,false,e.getMessage(),null);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			throw new Error(500,false,e.getMessage(),null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new Error(500,false,e.getMessage(),null);
		} catch (Error e) {
			// TODO Auto-generated catch block
			throw e;
		} 
		return users;
	}//End of get
	
	public static List<User> get() throws Error{
		HashMap<String, Object> data = new HashMap<String, Object>();
    	return get(data);
    }
	
	public static boolean delete(String id_user) throws Error{
		try{
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("api_key", Paybook.api_key);
			JSONObject response = call("users/" + id_user, Method.DELETE, data);
			Boolean status = response.getBoolean("status");
			return status;
		}catch(Error e){
			throw e;
		}
    }//End of delete

}
