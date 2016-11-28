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

public class Credentials extends Paybook{
	public String id_credential;
    public String id_site;
    public String username;
    public String id_site_organization;
    public String id_site_organization_type;
    public String ws;
    public String status;
    public String twofa;
    public HashMap<String, Object> twofa_config;
    public String dt_refresh;
	
    private Credentials(Session session, String id_user,String id_site, HashMap<String, Object> credentials) throws Error{
		JSONObject response = null;
    	String responseInString = null;
		try{
			HashMap<String, Object> data = new HashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			if(session != null && StringUtils.isBlank(id_user)){
				data.put("token", session.token);
			}else if(session == null && StringUtils.isNotBlank(id_user)){
				data.put("api_key", Paybook.api_key);
				data.put("id_user", id_user);
			}//End of IF
			data.put("id_site", id_site);
			data.put("credentials", credentials);
			response = call("credentials", Method.POST, data);
			responseInString = response.get("response").toString();
			Credentials credential = mapper.readValue(responseInString, Credentials.class);
			
			this.id_credential = credential.id_credential;
			this.id_site = credential.id_site;
			this.username = credential.username;
			this.id_site_organization = credential.id_site_organization;
			this.id_site_organization_type = credential.id_site_organization_type;
			this.ws = credential.ws;
			this.status = credential.status;
			this.twofa = credential.twofa;
			this.dt_refresh= credential.dt_refresh;
		}catch(Error e){
    		throw e;
    	}catch (JsonParseException e) {
			// TODO Auto-generated catch block
    		throw new Error(500,false,e.getMessage(),null);
		}catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			throw new Error(500,false,e.getMessage(),null);
		}catch (IOException e) {
			// TODO Auto-generated catch block
			throw new Error(500,false,e.getMessage(),null);
		}
	}//End of Credentials session
	
	public Credentials(String id_user, String id_site, HashMap<String, Object> credentials) throws Error{
		this(null,id_user,id_site,credentials);
	}//End of Credentials id_user
	
	public Credentials(Session session, String id_site, HashMap<String, Object> credentials) throws Error{
		this(session,"",id_site,credentials);
	}//End of Credentials id_user
	
	public Credentials(){	
	}
	
	private static boolean delete(String id_credential,Session session, String id_user) throws Error{
		JSONObject response = null;
		try{
			HashMap<String, Object> data = new HashMap<String, Object>();
			if(session != null && StringUtils.isBlank(id_user)){
				data.put("token", session.token);
			}else if(session == null && StringUtils.isNotBlank(id_user)){
				data.put("api_key", Paybook.api_key);
				data.put("id_user", id_user);
			}//End of IF
			response = call("credentials/" + id_credential, Method.DELETE, data);
			boolean status = response.getBoolean("status");
			return status;
		}catch(Error e){
			throw e;
		}
	}//End of delete
	
	public static boolean delete(String id_credential,Session session) throws Error{
		return delete(id_credential,session,"");
	}//End of delete
	
	public static boolean delete(String id_credential,String id_user) throws Error{
		return delete(id_credential,null,id_user);
	}//End of delete
	
	private static List<Credentials> get(Session session, String id_user) throws Error{
		JSONObject response = null;
    	String responseInString = null;
    	List<Credentials> credentials = null;
		try{
			HashMap<String, Object> data = new HashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			if(session != null && StringUtils.isBlank(id_user)){
				data.put("token", session.token);
			}else if(session == null && StringUtils.isNotBlank(id_user)){
				data.put("api_key", Paybook.api_key);
				data.put("id_user", id_user);
			}//End of IF
			
			response = call("credentials", Method.GET, data);
			responseInString = response.get("response").toString();
			credentials = mapper.readValue(responseInString, new TypeReference<List<Credentials>>(){});
		}catch(Error e){
    		throw e;
    	}catch (JsonParseException e) {
			// TODO Auto-generated catch block
    		throw new Error(500,false,e.getMessage(),null);
		}catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			throw new Error(500,false,e.getMessage(),null);
		}catch (IOException e) {
			// TODO Auto-generated catch block
			throw new Error(500,false,e.getMessage(),null);
		}
		return credentials;
	}//End of get
	
	public static List<Credentials> get(Session session) throws Error{
		return get(session,"");
	}//End of get
	
	public static List<Credentials> get(String id_user) throws Error{
		return get(null,id_user);
	}//End of get
	
	
	private List<HashMap<String, Object>> get_status(Session session, String id_user) throws Error{
		JSONObject response = null;
    	String responseInString = null;
    	List<HashMap<String, Object>> status = null;
		try{
			HashMap<String, Object> data = new HashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			if(session != null && StringUtils.isBlank(id_user)){
				data.put("token", session.token);
			}else if(session == null && StringUtils.isNotBlank(id_user)){
				data.put("api_key", Paybook.api_key);
				data.put("id_user", id_user);
			}//End of IF
			
			response = call(Method.GET, data,this.status);
			responseInString = response.get("response").toString();
			status = mapper.readValue(responseInString, new TypeReference<List<HashMap<String, Object>>>(){});
			for(int i=0; i<status.size(); i++){
				int code = (Integer) status.get(i).get("code");
				if(code == 410){
					this.twofa = (String) status.get(i).get("address");
					@SuppressWarnings("unchecked")
					List<HashMap<String, Object>> config = (List<HashMap<String, Object>>) status.get(i).get("twofa");
					this.twofa_config = config.get(0);
				}//End of IF
			}//End of FOR
			
		}catch(Error e){
    		throw e;
    	}catch (JsonParseException e) {
			// TODO Auto-generated catch block
    		throw new Error(500,false,e.getMessage(),null);
		}catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			throw new Error(500,false,e.getMessage(),null);
		}catch (IOException e) {
			// TODO Auto-generated catch block
			throw new Error(500,false,e.getMessage(),null);
		}
		return status;
	}//End of get_status
	
	public List<HashMap<String, Object>> get_status(Session session) throws Error{
		return this.get_status(session, "");
	}//End of get_status
	
	public List<HashMap<String, Object>> get_status(String id_user) throws Error{
		return this.get_status(null, id_user);
	}//End of get_status
	
	private boolean set_twofa(Session session, String id_user,HashMap<String, Object> twofa_value) throws Error{
		JSONObject response = null;
    	boolean status = false;
		try{
			HashMap<String, Object> data = new HashMap<String, Object>();
			if(session != null && StringUtils.isBlank(id_user)){
				data.put("token", session.token);
			}else if(session == null && StringUtils.isNotBlank(id_user)){
				data.put("api_key", Paybook.api_key);
				data.put("id_user", id_user);
			}//End of IF

			HashMap<String, Object> value = new HashMap<String, Object>();
			
			HashMap<String, Object> config = this.twofa_config;
			String name = (String) config.get("name");
			value.put(name,twofa_value.get(name));

			data.put("twofa", value);
		
			response = call(Method.POST, data, this.twofa);
			System.out.println(response);
			status = response.getBoolean("status");
				
		}catch(Error e){
    		throw e;
    	}
		return status;
	}//End of set_twofa
	
	public boolean set_twofa(Session session,HashMap<String, Object> twofa_value) throws Error{
		return set_twofa(session,"",twofa_value);
	}//End of set_twofa
	
	public boolean set_twofa(String id_user,HashMap<String, Object> twofa_value) throws Error{
		return set_twofa(null,id_user,twofa_value);
	}//End of set_twofa
	
	
}
