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

public class Transaction extends Paybook{
	
	public String id_transaction;
	public String id_user;
	public String id_site;
	public String id_site_organization;
	public String id_site_organization_type;
	public String id_account;
	public String id_account_type;
	public String id_currency;
	public String is_disable;
	public String description;
	public String amount;
	public String currency;
	public List<HashMap<String, Object>> attachments;
	public HashMap<String, Object> extra;
	public String reference;
	public String dt_transaction;
	public String dt_refresh;
	public String dt_disable;
	
	public Transaction() throws Error{
	}
	
	private static List<Transaction> get(Session session, String id_user, HashMap<String, Object> options) throws Error{
		List<Transaction> transactions = null;
		JSONObject response = null;
		try{
			HashMap<String, Object> data = options;
			if(session != null && StringUtils.isBlank(id_user)){
				data.put("token", session.token);
			}else if(session == null && StringUtils.isNotBlank(id_user)){
				data.put("api_key", Paybook.api_key);
				data.put("id_user", id_user);
			}//End of IF
			
			response = call("transactions", Method.GET, data);
			String responseInString = response.get("response").toString();
			ObjectMapper mapper = new ObjectMapper();
			transactions = mapper.readValue(responseInString, new TypeReference<List<Transaction>>(){});
		}catch(Error e){
			throw e;
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			throw new Error(500,false,e.getMessage(),null);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			throw new Error(500,false,e.getMessage(),null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new Error(500,false,e.getMessage(),null);
		}
    	return transactions;
	}//End of GET
	
	public static List<Transaction> get(Session session, HashMap<String, Object> options) throws Error{
    	return get(session,"",options);
    }//End of get
    
    public static List<Transaction> get(Session session) throws Error{
    	HashMap<String, Object> options = new HashMap<String, Object>();
    	return get(session,"",options);
    }//End of get
    
    public static List<Transaction> get(String id_user, HashMap<String, Object> options) throws Error{
    	return get(null,id_user,options);
    }//End of get
    
    public static List<Transaction> get(String id_user) throws Error{
    	HashMap<String, Object> options = new HashMap<String, Object>();
    	return get(null,id_user,options);
    }//End of get
    
    private static int get_count(Session session, String id_user, HashMap<String, Object> options) throws Error{
		int transactions;
		JSONObject response = null;
		try{
			HashMap<String, Object> data = options;
			if(session != null && StringUtils.isBlank(id_user)){
				data.put("token", session.token);
			}else if(session == null && StringUtils.isNotBlank(id_user)){
				data.put("api_key", Paybook.api_key);
				data.put("id_user", id_user);
			}//End of IF
			
			response = call("transactions/count", Method.GET, data);
			transactions = response.getJSONObject("response").getInt("count");
		}catch(Error e){
			throw e;
		}
    	return transactions;
	}//End of GET
	
	public static int get_count(Session session, HashMap<String, Object> options) throws Error{
    	return get_count(session,"",options);
    }//End of get
    
    public static int get_count(Session session) throws Error{
    	HashMap<String, Object> options = new HashMap<String, Object>();
    	return get_count(session,"",options);
    }//End of get
    
    public static int get_count(String id_user, HashMap<String, Object> options) throws Error{
    	return get_count(null,id_user,options);
    }//End of get
    
    public static int get_count(String id_user) throws Error{
    	HashMap<String, Object> options = new HashMap<String, Object>();
    	return get_count(null,id_user,options);
    }//End of get
	
	
	
}
