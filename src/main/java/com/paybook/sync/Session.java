package com.paybook.sync;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

public class Session extends Paybook{
	public User user;
	public String token;
    public String iv;
    public String key;
    
    private Session(User user, String token) throws Error{
    	JSONObject response = null;
    	try{
    		if(StringUtils.isNotBlank(token)){
    			this.token = token;
    			this.iv = null;
    			this.key = null;
    		}else if(!user.equals(null)){
    			HashMap<String, Object> data = new HashMap<String, Object>();
    			data.put("api_key", Paybook.api_key);
    			data.put("id_user", user.id_user);
    			response = call("sessions/", Method.POST, data);
    			this.iv = response.getJSONObject("response").get("iv").toString();
    			this.key = response.getJSONObject("response").get("key").toString();
    			this.token = response.getJSONObject("response").get("token").toString();
    		}
    	}catch(Error e){
    		throw e;
    	}
	}//End of Session
    
    public Session(User user) throws Error{
    	this(user,"");
	}
    
    public Session(String token) throws Error{
    	this(null,token);
	}
    
    public Session() throws Error{
	}
	
    public boolean verify() throws Error{
		try{
			JSONObject response = call("sessions/" + this.token + "/verify", Method.GET);
			Boolean status = response.getBoolean("status");
			return status;
		}catch(Error e){
			throw e;
		}
    }//End of verify
    
    public static boolean delete(String token) throws Error{
    	try{
	    	JSONObject response = call("sessions/" + token , Method.DELETE);
	    	boolean status = response.getBoolean("status");
	    	return status;
	    }catch(Error e){
			throw e;
		}
    };//End of delete
    
}
