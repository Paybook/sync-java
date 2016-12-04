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

public class Account extends Paybook{
	public String id_account;
    public String id_user;
    public String id_external;
    public String id_credential;
    public String id_site;
    public String id_site_organization;
    public String name;
    public String number;
    public float balance;
    public Site site;
    public String id_account_type;
    public String account_type;
    public int is_disable;
    public String currency;
    public HashMap<String, Object> extra;
    public String dt_refresh;
   
    public Account() throws Error{
	}
    
    private static List<Account> get(Session session, String id_user, HashMap<String, Object> options) throws Error{
    	List<Account> accounts = null;
		JSONObject response = null;
		try{
			HashMap<String, Object> data = options;
			if(session != null && StringUtils.isBlank(id_user)){
				data.put("token", session.token);
			}else if(session == null && StringUtils.isNotBlank(id_user)){
				data.put("api_key", Paybook.api_key);
				data.put("id_user", id_user);
			}//End of IF
			
			response = call("accounts", Method.GET, data);
			String responseInString = response.get("response").toString();
			ObjectMapper mapper = new ObjectMapper();
			accounts = mapper.readValue(responseInString, new TypeReference<List<Account>>(){});
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
    	return accounts;
    }//End of get
    
    public static List<Account> get(Session session, HashMap<String, Object> options) throws Error{
    	return get(session,"",options);
    }//End of get
    
    public static List<Account> get(Session session) throws Error{
    	HashMap<String, Object> options = new HashMap<String, Object>();
    	return get(session,"",options);
    }//End of get
    
    public static List<Account> get(String id_user, HashMap<String, Object> options) throws Error{
    	return get(null,id_user,options);
    }//End of get
    
    public static List<Account> get(String id_user) throws Error{
    	HashMap<String, Object> options = new HashMap<String, Object>();
    	return get(null,id_user,options);
    }//End of get
    
}
