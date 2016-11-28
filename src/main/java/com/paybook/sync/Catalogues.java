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

public class Catalogues extends Paybook{
	private static List<Account_Type> get_account_types(Session session,String id_user) throws Error{
		List<Account_Type> account_types = null;
		JSONObject response = null;
		try{
			HashMap<String, Object> data = new HashMap<String, Object>();
			if(session != null && StringUtils.isBlank(id_user)){
				data.put("token", session.token);
			}else if(session == null && StringUtils.isNotBlank(id_user)){
				data.put("api_key", Paybook.api_key);
				data.put("id_user", id_user);
			}//End of IF
			log(data.toString());
			response = call("catalogues/account_types", Method.GET, data);
			String responseInString = response.get("response").toString();
			ObjectMapper mapper = new ObjectMapper();
			account_types = mapper.readValue(responseInString, new TypeReference<List<Account_Type>>(){});
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
		return account_types;
	}// End of get_account_types with session
	
	public static List<Account_Type> get_account_types(Session session) throws Error{
		log("get account from session");
		return get_account_types(session,"");
	}// End of get_account_types with id_user
	
	public static List<Account_Type> get_account_types(String id_user) throws Error{
		log("get account from id_user");
		log(id_user);
		return get_account_types(null,id_user);
	}// End of get_account_types with id_user
	
	private static List<Attachment_Type> get_attachment_types(Session session,String id_user) throws Error{
		List<Attachment_Type> attachment_types = null;
		JSONObject response = null;
		try{
			HashMap<String, Object> data = new HashMap<String, Object>();
			if(session != null && StringUtils.isBlank(id_user)){
				data.put("token", session.token);
			}else if(session == null && StringUtils.isNotBlank(id_user)){
				data.put("api_key", Paybook.api_key);
				data.put("id_user", id_user);
			}//End of IF
			response = call("catalogues/attachment_types", Method.GET, data);
			String responseInString = response.get("response").toString();
			ObjectMapper mapper = new ObjectMapper();
			attachment_types = mapper.readValue(responseInString, new TypeReference<List<Attachment_Type>>(){});
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
		return attachment_types;
	}// End of get_attachment_types with session
	
	public static List<Attachment_Type> get_attachment_types(Session session) throws Error{
		return get_attachment_types(session,"");
	}// End of get_attachment_types with id_user
	
	public static List<Attachment_Type> get_attachment_types(String id_user) throws Error{
		return get_attachment_types(null,id_user);
	}// End of get_attachment_types with id_user
	
	private static List<Country> get_countries(Session session,String id_user) throws Error{
		List<Country> countries = null;
		JSONObject response = null;
		try{
			HashMap<String, Object> data = new HashMap<String, Object>();
			if(session != null && StringUtils.isBlank(id_user)){
				data.put("token", session.token);
			}else if(session == null && StringUtils.isNotBlank(id_user)){
				data.put("api_key", Paybook.api_key);
				data.put("id_user", id_user);
			}//End of IF
			response = call("catalogues/countries", Method.GET, data);
			String responseInString = response.get("response").toString();
			ObjectMapper mapper = new ObjectMapper();
			countries = mapper.readValue(responseInString, new TypeReference<List<Country>>(){});
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
		return countries;
	}// End of get_countries with session
	
	public static List<Country> get_countries(Session session) throws Error{
		return get_countries(session,"");
	}// End of get_countries with id_user
	
	public static List<Country> get_countries(String id_user) throws Error{
		return get_countries(null,id_user);
	}// End of get_countries with id_user
	
	private static List<Site> get_sites(Session session,String id_user, boolean is_test) throws Error{
		List<Site> sities = null;
		JSONObject response = null;
		try{
			HashMap<String, Object> data = new HashMap<String, Object>();
			if(session != null && StringUtils.isBlank(id_user)){
				data.put("token", session.token);
			}else if(session == null && StringUtils.isNotBlank(id_user)){
				data.put("api_key", Paybook.api_key);
				data.put("id_user", id_user);
			}//End of IF
			if(is_test == true){
				data.put("is_test", is_test);
			}//End of IF
			response = call("catalogues/sites", Method.GET, data);
			String responseInString = response.get("response").toString();
			ObjectMapper mapper = new ObjectMapper();
			sities = mapper.readValue(responseInString, new TypeReference<List<Site>>(){});
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
		return sities;
	}// End of get_sites with session
	
	public static List<Site> get_sites(Session session) throws Error{
		return get_sites(session,"",false);
	}// End of get_sites with id_user
	
	public static List<Site> get_sites(Session session,boolean is_test) throws Error{
		return get_sites(session,"",is_test);
	}// End of get_sites with id_user
	
	public static List<Site> get_sites(String id_user) throws Error{
		return get_sites(null,id_user,false);
	}// End of get_sites with id_user
	
	public static List<Site> get_sites(String id_user,boolean is_test) throws Error{
		return get_sites(null,id_user,is_test);
	}// End of get_sites with id_user
	
	private static List<Site_Organization> get_site_organizations(Session session, String id_user, boolean is_test) throws Error{
		List<Site_Organization> site_organizations = null;
		JSONObject response = null;
		try{
			HashMap<String, Object> data = new HashMap<String, Object>();
			if(session != null && StringUtils.isBlank(id_user)){
				data.put("token", session.token);
			}else if(session == null && StringUtils.isNotBlank(id_user)){
				data.put("api_key", Paybook.api_key);
				data.put("id_user", id_user);
			}//End of IF
			if(is_test == true){
				data.put("is_test", is_test);
			}//End of IF
			response = call("catalogues/site_organizations", Method.GET, data);
			String responseInString = response.get("response").toString();
			ObjectMapper mapper = new ObjectMapper();
			site_organizations = mapper.readValue(responseInString, new TypeReference<List<Site_Organization>>(){});
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
		return site_organizations;
	}// End of get_sites with session
	
	public static List<Site_Organization> get_site_organizations(Session session) throws Error{
		return get_site_organizations(session,"",false);
	}// End of get_sites with id_user
	
	public static List<Site_Organization> get_site_organizations(Session session,boolean is_test) throws Error{
		return get_site_organizations(session,"",is_test);
	}// End of get_sites with id_user
	
	public static List<Site_Organization> get_site_organizations(String id_user) throws Error{
		return get_site_organizations(null,id_user,false);
	}// End of get_sites with id_user
	
	public static List<Site_Organization> get_site_organizations(String id_user,boolean is_test) throws Error{
		return get_site_organizations(null,id_user,is_test);
	}// End of get_sites with id_user
	
}
