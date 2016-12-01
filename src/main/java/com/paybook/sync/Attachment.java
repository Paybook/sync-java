package com.paybook.sync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Attachment extends Paybook{

	public String id_attachment;
	public String id_account;
	public String id_user;
	public String id_external;
	public String id_attachment_type;
	public String id_transaction;
	public String is_valid;
	public String file;
	public String mime;
	public String url;
	public HashMap<String,Object> extra;
    public String content;
	public String dt_refresh;

	public Attachment() throws Error{
	}
	
	private static List<Attachment> get(Session session, String id_user, HashMap<String, Object> options) throws Error{
		List<Attachment> attachments = null;
		JSONObject response = null;
		try{
			HashMap<String, Object> data = options;
	
			if(session != null && StringUtils.isBlank(id_user)){
				data.put("token", session.token);
			}else if(session == null && StringUtils.isNotBlank(id_user)){
				data.put("api_key", Paybook.api_key);
				data.put("id_user", id_user);
			}//End of IF
			
			response = call("attachments", Method.GET, data);
			String responseInString = response.get("response").toString();
			ObjectMapper mapper = new ObjectMapper();
			attachments = mapper.readValue(responseInString, new TypeReference<List<Attachment>>(){});
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
    	return attachments;
	}//End of GET
	
	public static List<Attachment> get(Session session, HashMap<String, Object> options) throws Error{
    	return get(session,"",options);
    }//End of get
    
    public static List<Attachment> get(Session session) throws Error{
    	HashMap<String, Object> options = new HashMap<String, Object>();
    	return get(session,"",options);
    }//End of get
    
    public static List<Attachment> get(String id_user, HashMap<String, Object> options) throws Error{
    	return get(null,id_user,options);
    }//End of get
    
    public static List<Attachment> get(String id_user) throws Error{
    	HashMap<String, Object> options = new HashMap<String, Object>();
    	return get(null,id_user,options);
    }//End of get
    
	private static List<Attachment> get(Session session, String id_user, String id_attachment) throws Error{
		List<Attachment> attachments  = new ArrayList<Attachment>();;
		JSONObject response = null;
		try{
			HashMap<String, Object> data = new HashMap<String,Object>();
			if(session != null && StringUtils.isBlank(id_user)){
				data.put("token", session.token);
			}else if(session == null && StringUtils.isNotBlank(id_user)){
				data.put("api_key", Paybook.api_key);
				data.put("id_user", id_user);
			}//End of IF
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("Content-Type", "application/xml");
			response = call("attachments/"+id_attachment, Method.GET, data, headers);
			String responseInString = response.get("content").toString();
			Attachment attachment = new Attachment();
			attachment.content = responseInString;
			attachments.add(attachment);
		}catch(Error e){
			throw e;
		}
    	return attachments;
	}//End of GET
    
    public static List<Attachment> get(Session session, String id_attachment) throws Error{
    	return get(session,"",id_attachment);
    }//End of get
    
    public static List<Attachment> get(String id_user, String id_attachment) throws Error{
    	return get(null,id_user,id_attachment);
    }//End of get
    
    private static int get_count(Session session, String id_user, HashMap<String, Object> options) throws Error{
		int attachments;
		JSONObject response = null;
		try{
			HashMap<String, Object> data = options;
			if(session != null && StringUtils.isBlank(id_user)){
				data.put("token", session.token);
			}else if(session == null && StringUtils.isNotBlank(id_user)){
				data.put("api_key", Paybook.api_key);
				data.put("id_user", id_user);
			}//End of IF
			
			response = call("attachments/count", Method.GET, data);
			attachments = response.getJSONObject("response").getInt("count");
		}catch(Error e){
			throw e;
		}
    	return attachments;
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
    
    @SuppressWarnings("unchecked")
	private static HashMap<String,Object> get_extra(Session session, String id_user, String id_attachment) throws Error{
    	HashMap<String,Object> attachments = null;
		JSONObject response = null;
		try{
			HashMap<String, Object> data = new HashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			if(session != null && StringUtils.isBlank(id_user)){
				data.put("token", session.token);
			}else if(session == null && StringUtils.isNotBlank(id_user)){
				data.put("api_key", Paybook.api_key);
				data.put("id_user", id_user);
			}//End of IF

			response = call("attachments/" + id_attachment + "/extra", Method.GET, data);
			String responseInString = response.get("response").toString();
			attachments = mapper.readValue(responseInString, HashMap.class);
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
    	return attachments;
	}//End of GET
	
	public static HashMap<String,Object> get_extra(Session session, String id_attachment) throws Error{
    	return get_extra(session,"",id_attachment);
    }//End of get
    
    public static HashMap<String,Object> get_extra(String id_user, String id_attachment) throws Error{
    	return get_extra(null,id_user,id_attachment);
    }//End of get

}

