package com.quickstarts;

import java.util.HashMap;
import java.util.List;

import com.paybook.sync.*;
import com.paybook.sync.Error;

public class quickstart_sat {
	static final String YOUR_API_KEY = "PAYBOOK_API_KEY";
	static final boolean test_environment = false;
	static final String BANK_NAME = "CIEC";
	static final String SAT_RFC = "RFC";
	static final String SAT_CIEC = "CIEC";
	static final String USERNAME = SAT_RFC;
	static final int REQUEST_LIMIT = 100;
    
	User user;
    Session session;
    Catalogues catalogues;
    List<Site> sites;
    Site bank_site;
    Credentials credentials;
    List<Transaction> transactions;
    List<Attachment> attachments;
    Attachment attachment;
    
    public quickstart_sat(){
    }
    
	public boolean start(){
		
		try {
			
			Paybook.init(YOUR_API_KEY);
			
			List<User> users;
			users = User.get();
			user = null;
			
			for(int i=0;i<users.size();i++){
				if(users.get(i).name.equals(USERNAME)){
					System.out.println("User " + USERNAME + " already exists");
					user = users.get(i);
					break;
				}//End of IF
			}//End of For
			
			if(user == null){
				System.out.println("Creating user: " + USERNAME);
				user = new User(USERNAME);
			}//End of IF
			
			session = new Session(user);
			System.out.println("Token: " + session.token);
			
			boolean sessionVerified = session.verify();
			System.out.println("Sesion verified:" + sessionVerified);
			
			sites = Catalogues.get_sites(session,test_environment);
			System.out.println("Sites list:");
			for(int i=0;i<sites.size();i++){
				System.out.println(sites.get(i).name);
				if(sites.get(i).name.equals(BANK_NAME)){
					bank_site = sites.get(i); 
				}//End of IF
			}//End of for
			
			
			HashMap<String,Object> credentials_data = new HashMap<String,Object>();
			credentials_data.put("rfc", SAT_RFC);
			credentials_data.put("password", SAT_CIEC);
			credentials = new Credentials(session,bank_site.id_site,credentials_data); 
			System.out.println("Credentials username: " + credentials.username);
			
			System.out.println("Wait for validation");
			boolean bank_sync_completed = false;
			while(!bank_sync_completed ){
				Thread.sleep(1000);
				List<HashMap<String,Object>> status = credentials.get_status(session);
				System.out.println(status);
				int last_index = status.size()-1;
				int code = (Integer) status.get(last_index).get("code");
				if(code >= 200 && code < 400){
					bank_sync_completed = true;
					break;
				}else if((code >= 400 && code<410) || (code >= 500 && code<=504)){
					return false;
				}//End of if
			}//End of while
			
			HashMap<String,Object> options = new HashMap<String,Object>();
			options.put("id_credential", credentials.id_credential);
			options.put("limit", REQUEST_LIMIT);
			
			transactions = Transaction.get(session, options);
			System.out.println("Bank transactions: " + transactions.size());
			attachments = Attachment.get(session, options);
			System.out.println("Bank attachments: " + attachments.size());
			
			if(attachments.size() > 0){
				String id_attachment = attachments.get(0).id_attachment;
				attachment = Attachment.get(session, id_attachment).get(0);
				System.out.println(attachment.content);
			}//End of IF
		
		} catch (Error e) {
			System.out.println("-- ERROR --");
			System.out.println("Code: " + e.code);
			System.out.println("Message: " + e.message);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//End of try/catch
    	
		
		return false;
	}//End of start
}//End of class
