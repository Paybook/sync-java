package com.quickstarts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import com.paybook.sync.Attachment;
import com.paybook.sync.Catalogues;
import com.paybook.sync.Credentials;
import com.paybook.sync.Error;
import com.paybook.sync.Paybook;
import com.paybook.sync.Session;
import com.paybook.sync.Site;
import com.paybook.sync.Transaction;
import com.paybook.sync.User;

public class quickstart_token {
	static final String YOUR_API_KEY = "PAYBOOK_API_KEY";
	static final boolean test_environment = true;
	static final String BANK_NAME = "Token";
	static final String BANK_USERNAME = "test";
	static final String BANK_PASSWORD = "test";
	static final String USERNAME = "java_test";
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
    
    public quickstart_token(){
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
			credentials_data.put("username", BANK_USERNAME);
			credentials_data.put("password", BANK_PASSWORD);
			credentials = new Credentials(session,bank_site.id_site,credentials_data); 
			System.out.println("Credentials username: " + credentials.username);
			
			System.out.println("Wait for token request");
			boolean status_410 = false;
			while(!status_410 ){
				Thread.sleep(1000);
				List<HashMap<String,Object>> status = credentials.get_status(session);
				System.out.println(status);
				for(int i=0;i<status.size();i++){
					int code = (Integer) status.get(i).get("code");
					if(code == 410){
						status_410 = true;
						break;
					}else if( (code >= 400 && code<410) || (code >= 500 && code<=504)){
						System.out.println("There was an error with your credentials with code:" + code);
						return false;
					}//End of if
				}//End of for
			}//End of while
			
			System.out.print("Enter token: ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String token_value;
			token_value = br.readLine();
			HashMap<String,Object> twofa_value = new HashMap<String,Object>();
			twofa_value.put("token",token_value);
			credentials.set_twofa(session, twofa_value);
			
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
			System.out.println("-- ERROR --");
			System.out.println("Message: " + e.getMessage());
		}catch (IOException e) {
			// TODO Auto-generated catch block
        	System.out.println("-- ERROR --");
			System.out.println("Message: " + e.getMessage());
		}//End of try/catch
    	
		
		return false;
	}//End of start
}
