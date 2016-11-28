package sync;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.paybook.sync.*;
import com.paybook.sync.Error;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PaybookUnitTest {

	static final String PAYBOOK_API_KEY = "PAYBOOK_API_KEY";
	static final String PAYBOOK_WRONG_API_KEY = "PAYBOOK_WRONG_API_KEY";
	static final String USERNAME = "java_13";
	static final String USER_ID_EXTERNAL = USERNAME + "_id_external";
	
	static final boolean IS_TEST = true;
	static final String SITE_NAME = "Normal";
	static final String CREDENTIAL_USERNAME = "username";
	static final String CREDENTIAL_PASSWORD = "password";
	static final String CREDENTIAL_USERNAME_VALUE = "test";
	static final String CREDENTIAL_PASSWORD_VALUE = "test";
	
	static final boolean LOGGER = false;
	
	static User user;
	static Session session;
	static Site SATSite = null;
	static Credentials credential;
	static Site tokenSite;
	
	@Test
    public void A_InicializeWrongAPIKEY(){
		System.out.println("A.- Initialize Wrong Paybook API KEY");
		Paybook.init(PAYBOOK_WRONG_API_KEY,LOGGER);
		assertNull(null);
    }//End of InicializeWrongAPIKEY

	@Test
    public void B_RequestWithWrongAPIKEY(){
		try {
			System.out.println("B.- Request with Wrong Paybook API KEY");
			List<User> users = User.get();
			System.out.println("Users: " + users.size());
			fail();
		} catch (Error e) {
			// TODO Auto-generated catch block
			assertEquals("Result: ", e.code, 401);
		}
    }//End of RequestWithWrongAPIKEY
	
	@Test
    public void C_InicializeValidAPIKEY()
    {	
		System.out.println("C.- Initialize Valid Paybook API KEY");
		Paybook.init(PAYBOOK_API_KEY,LOGGER);
		assertNull(null);
    }//End of InicializeValidAPIKEY
	
	@Test
    public void D_getUsers()
    {
        try {
        	System.out.println("D.- User list request");
        	List<User> users = User.get();
        	System.out.println("Users: " + users.size());
        	assertNotNull(users);
		} catch (Error e) {
			System.out.println(e.code);
			fail();
		}
    }//end of getUsers
	
	@Test
    public void E_createUser()
    {
        try {
        	System.out.println("E.- Create new user");
        	user = new User(USERNAME);
        	System.out.println("	User name: " + user.name);
        	System.out.println("	User external: " + user.id_external);
        	assertNotNull(user.name);
		} catch (Error e) {
			fail();
		}
    }//End of createUser
	
	@Test
    public void F_getUsers()
    {
        try {
        	System.out.println("F.- User list request, new user added");
        	List<User> users = User.get();
        	System.out.println("Users: " + users.size());
        	assertNotNull(users);
		} catch (Error e) {
			System.out.println(e.code);
			fail();
		}
    }//End of getUsers
	
	@Test
    public void G_deleteUser()
    {
        try {
        	System.out.println("G.- Delete user");
        	System.out.println("	User name: " + user.id_user);
        	boolean deleted = User.delete(user.id_user);
        	assertEquals(true,deleted);
		} catch (Error e) {
			fail();
		}
    }//End of deleteUser
	
	@Test
    public void H_getUsers()
    {
        try {
        	System.out.println("H.- User list request, new user missed");
        	List<User> users = User.get();
        	System.out.println("Users: " + users.size());
        	assertNotNull(users);
		} catch (Error e) {
			System.out.println(e.code);
			fail();
		}
    }//End of getUsers
	
	@Test
    public void I_createUser()
    {
        try {
        	System.out.println("I.- Create new user again");
        	user = new User(USERNAME);
        	System.out.println("	User name: " + user.name);
        	System.out.println("	User external: " + user.id_external);
        	assertNotNull(user.name);
		} catch (Error e) {
			fail();
		}
    }//End of createUser
	
	@Test
    public void J_updateUser()
    {
        try {
        	System.out.println("J.- Update user");
        	user = new User(USERNAME,user.id_user,USER_ID_EXTERNAL);
        	System.out.println("	User name: " + user.name);
        	System.out.println("	User external: " + user.id_external);
        	assertNotNull(user.name);
		} catch (Error e) {
			fail();
		}
    }//End of updateUser*/
	
	@Test
    public void K_getUserByExternalID()
    {
        try {
        	System.out.println("K.- Get User by External ID");
        	user = new User("","",USER_ID_EXTERNAL);
        	System.out.println("Username: " + user.name);
        	System.out.println("User_id: " + user.id_user);
        	System.out.println("User_external: " + user.id_external);
        	assertNotNull(user);
		} catch (Error e) {
			System.out.println(e.code);
			fail();
		}
    }//End of getUserByExternalID
	
	@Test
    public void L_createUserSession()
    {
        try {
        	System.out.println("L.- Create User Session");
        	session = new Session(user);
        	System.out.println("Token Session: " + session.token);
        	System.out.println("Key Session: " + session.key);
        	System.out.println("Iv Session: " + session.iv);
        	assertNotNull(session);
		} catch (Error e) {
			System.out.println(e.code);
			fail();
		}
    }//End of createUserSession
	
	@Test
    public void M_verifySession()
    {
        try {
        	System.out.println("M.- Verify Session");
        	boolean session_verified = session.verify();
        	System.out.println("Verify Session: " + session_verified);
        	assertEquals(true,session_verified);
		} catch (Error e) {
			System.out.println(e.code);
			fail();
		}
    }//End of verifySession
	
	@Test
    public void N_deleteSession()
    {
        try {
        	System.out.println("N.- Delete Session");
        	boolean session_deleted = Session.delete(session.token);
        	System.out.println("Session deleted: " + session_deleted);
        	assertEquals(true,session_deleted);
		} catch (Error e) {
			System.out.println(e.code);
			fail();
		}
    }//End of deleteSession
	
	@Test
    public void O_verifySession()
    {
        try {
        	System.out.println("O.- Verify Session Again");
        	boolean session_verified = session.verify();
        	System.out.println("Verify Session: " + session_verified);
        	fail();
        	
		} catch (Error e) {
			System.out.println(e.code);
			assertEquals(401,e.code);
		}
    }//End of verifySession
	
	@Test
    public void P_createUserSessionAgain()
    {
        try {
        	System.out.println("P.- Create User Session Again");
        	session = new Session(user);
        	System.out.println("Token Session: " + session.token);
        	System.out.println("Key Session: " + session.key);
        	System.out.println("Iv Session: " + session.iv);
        	assertNotNull(session);
		} catch (Error e) {
			System.out.println(e.code);
			fail();
		}
    }//End of createUserSession
	
	@Test
	public void Q_getCataloguesAccountType(){
		System.out.println("Q.- Get Catalogues Account Type");
		try {
			List<Account_Type> account_types = Catalogues.get_account_types(session);
			System.out.println(account_types.size());
			assertNotNull(account_types);
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}	
	}//End of getCataloguesAccountType
	
	@Test
	public void R_getCataloguesAccountTypeByIdUser(){
		System.out.println("R.- Get Catalogues Account Type By ID User");
		try {
			List<Account_Type> account_types = Catalogues.get_account_types(user.id_user);
			System.out.println(account_types.size());
			assertNotNull(account_types);
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}	
	}//End of getCataloguesAccountTypeByIdUser
	
	@Test
	public void S_getCataloguesAttachmentType(){
		System.out.println("S.- Get Catalogues Attachment Type");
		try {
			List<Attachment_Type> attachment_types = Catalogues.get_attachment_types(session);
			System.out.println(attachment_types.size());
			assertNotNull(attachment_types);
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}	
	}//End of getCataloguesAccountType
	
	@Test
	public void T_getCataloguesAttachmentTypeByIdUser(){
		System.out.println("T.- Get Catalogues Attachment Type By ID User");
		try {
			List<Attachment_Type> attachment_types = Catalogues.get_attachment_types(user.id_user);
			System.out.println(attachment_types.size());
			assertNotNull(attachment_types);
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}	
	}//End of getCataloguesAccountTypeByIdUser
	
	@Test
	public void U_getCataloguesCountries(){
		System.out.println("U.- Get Catalogues Countries");
		try {
			List<Country> countries = Catalogues.get_countries(session);
			System.out.println(countries.size());
			assertNotNull(countries);
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}	
	}//End of U_getCataloguesCountries
	
	@Test
	public void V_getCataloguesCountriesByIdUser(){
		System.out.println("V.- Get Catalogues Countries By ID User");
		try {
			List<Country> countries = Catalogues.get_countries(user.id_user);
			System.out.println(countries.size());
			assertNotNull(countries);
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}	
	}//End of V_getCataloguesCountriesByIdUser
	
	@Test
	public void W_10_getCataloguesSites(){
		System.out.println("W_10.- Get Catalogues Sites");
		try {
			List<Site> sites = Catalogues.get_sites(session);
			System.out.println(sites.size());
			assertNotNull(sites);
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}	
	}//End of getCataloguesSites
	
	@Test
	public void W_11_getCataloguesTestSites(){
		System.out.println("W_11.- Get Catalogues Test Sites");
		try {
			List<Site> sites = Catalogues.get_sites(session,true);
			System.out.println(sites.size());
			
			for(int i=0;i<sites.size();i++){
				if(sites.get(i).name.equals("Token")){
					tokenSite = sites.get(i);
				}//End of IF
			}//End of for
			
			assertNotNull(sites);
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}	
	}//End of getCataloguesSites
	
	@Test
	public void X_getCataloguesSitesByIdUser(){
		System.out.println("X.- Get Catalogues Sites By ID User");
		try {
			List<Site> sites = Catalogues.get_sites(user.id_user);
			System.out.println(sites.size());
			assertNotNull(sites);
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}	
	}//End of getCataloguesSitesByIdUser
	
	@Test
	public void Y_getCataloguesSiteOrganization(){
		System.out.println("Y.- Get Catalogues Site Organization");
		try {
			List<Site_Organization> site_organizations = Catalogues.get_site_organizations(session);
			System.out.println(site_organizations.size());
			assertNotNull(site_organizations);
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}	
	}//End of getCataloguesSiteOrganization
	
	@Test
	public void Z_10_getCataloguesSiteOrganizationByIdUser(){
		System.out.println("Z_10.- Get Catalogues Site Organization By ID User");
		try {
			List<Site_Organization> site_organizations = Catalogues.get_site_organizations(user.id_user);
			System.out.println(site_organizations.size());
			assertNotNull(site_organizations);
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}	
	}//End of getCataloguesSiteOrganizationByIdUser
	
	@Test
	public void Z_11_getSiteSAT(){
		System.out.println("Z_11.- Get Site SAT");
		try {
			List<Site> sites = Catalogues.get_sites(session,IS_TEST);
			String SATSiteName = SITE_NAME;
			int i = 0;
			String siteName = null;
			while(i < sites.size() && !SATSiteName.equals(siteName) ){
				SATSite = sites.get(i);
				siteName = SATSite.name;
				System.out.println(siteName);
				i++; 
			}
			System.out.println("SAT Site: " + SATSite.id_site);
			assertNotNull(SATSite);
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}	
	}//End of getSiteSAT
	
	@Test
	public void Z_12_getCredentials(){
		System.out.println("Z_12.- Get Credentials");
		try {
			List<Credentials> credentials = Credentials.get(session);
			System.out.println(credentials.size());
			assertNotNull(credentials);
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}	
	}//End of getCredentials
	
	@Test
	public void Z_13_createSATCredentials(){
		System.out.println("Z_13.- Create SAT Credentials");
		try {
			HashMap<String, Object> SATCredentials = new HashMap<String, Object>();
			SATCredentials.put(CREDENTIAL_USERNAME, CREDENTIAL_USERNAME_VALUE);
			SATCredentials.put(CREDENTIAL_PASSWORD, CREDENTIAL_PASSWORD_VALUE);
			
			HashMap<String, Object> credential_data = new HashMap<String, Object>();
			for(int i = 0; i< SATSite.credentials.size(); i++){
				String name = SATSite.credentials.get(i).name;
				String value = (String) SATCredentials.get(name);
				credential_data.put(name, value);
			}//End of for
			
			credential = new Credentials(session,SATSite.id_site,credential_data);
			System.out.println("id_credential: " + credential.id_credential);
			System.out.println("username: " + credential.username);
			System.out.println("status: " + credential.status);
			System.out.println("twofa: " + credential.twofa);
			assertNotNull(credential);
		} catch (Error e) {
			// TODO Auto-generated catch block
			System.out.println(e.code);
			System.out.println(e.message);
			fail();
		}	
	}//End of createSATCredentials
	
	@Test
	public void Z_14_getCredentials(){
		System.out.println("Z_14.- Get credentials");
		try {
			List<Credentials> credentials = Credentials.get(session);
			System.out.println(credentials.size());
			assertNotNull(credentials);
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}	
	}//End of getCredentials
	
	@Test
	public void Z_15_deteleCredentials(){
		System.out.println("Z_15.- Delete credentials");
		try {
			boolean deleted = Credentials.delete(credential.id_credential, session);
			assertEquals(true,deleted);
		} catch (Error e) {
			// TODO Auto-generated catch block
			System.out.println("Code: " + e.code);
			System.out.println("Message: " + e.message);
			fail();
		}	
	}//End of deteleCredentials
	
	@Test
	public void Z_16_getCredentials(){
		System.out.println("Z_16.- Get credentials");
		try {
			List<Credentials> credentials = Credentials.get(session);
			System.out.println(credentials.size());
			assertNotNull(credentials);
		} catch (Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}	
	}//End of getCredentials
	
	@Test
	public void Z_17_createSATCredentials(){
		System.out.println("Z_17.- Create SAT Credentials");
		try {
			HashMap<String, Object> SATCredentials = new HashMap<String, Object>();
			SATCredentials.put(CREDENTIAL_USERNAME, CREDENTIAL_USERNAME_VALUE);
			SATCredentials.put(CREDENTIAL_PASSWORD, CREDENTIAL_PASSWORD_VALUE);
			
			HashMap<String, Object> credential_data = new HashMap<String, Object>();
			for(int i = 0; i< SATSite.credentials.size(); i++){
				String name = SATSite.credentials.get(i).name;
				String value = (String) SATCredentials.get(name);
				credential_data.put(name, value);
			}//End of for
			
			credential = new Credentials(session,SATSite.id_site,credential_data);
			System.out.println("id_credential: " + credential.id_credential);
			System.out.println("username: " + credential.username);
			System.out.println("status: " + credential.status);
			System.out.println("twofa: " + credential.twofa);
			assertNotNull(credential);
		} catch (Error e) {
			// TODO Auto-generated catch block
			System.out.println(e.code);
			System.out.println(e.message);
			fail();
		}	
	}//End of createSATCredentials
	
	@Test
	public void Z_18_createTokenCredentials(){
		System.out.println("Z_18.- Create Token Credentials");
		try {
			HashMap<String, Object> tokenCredentials = new HashMap<String, Object>();
			tokenCredentials.put("username", "test");
			tokenCredentials.put("password", "test");
			
			credential = new Credentials(session,tokenSite.id_site,tokenCredentials);
			System.out.println("id_credential: " + credential.id_credential);
			System.out.println("username: " + credential.username);
			System.out.println("status: " + credential.status);
			System.out.println("twofa: " + credential.twofa);
			assertNotNull(credential);
		} catch (Error e) {
			// TODO Auto-generated catch block
			System.out.println(e.code);
			System.out.println(e.message);
			fail();
		}
	}//End of createTokenCredentials
	
	@Test
	public void Z_19_createTokenCredentials(){
		System.out.println("Z_19.- Create Token Credentials");
		try {
			HashMap<String, Object> tokenCredentials = new HashMap<String, Object>();
			tokenCredentials.put("username", "test");
			tokenCredentials.put("password", "test");
			
			credential = new Credentials(session,tokenSite.id_site,tokenCredentials);
			System.out.println("id_credential: " + credential.id_credential);
			System.out.println("username: " + credential.username);
			System.out.println("status: " + credential.status);
			System.out.println("twofa: " + credential.twofa);
			assertNotNull(credential);
		} catch (Error e) {
			// TODO Auto-generated catch block
			System.out.println(e.code);
			System.out.println(e.message);
			fail();
		}
	}//End of createTokenCredentials
	
	@Test
	public void Z_20_waitForTwofaStatus(){
		System.out.println("Z_20.- Wait for Twofa Status");
		try {
			boolean code_410 = false;
			List<HashMap<String, Object>> statuses;
			while(!code_410){
				System.out.println(". . . .");
				Thread.sleep(1000); 
				statuses = credential.get_status(session);
				for(int i=0; i<statuses.size();i++){
					int code = (Integer) statuses.get(i).get("code");
					if(code == 410){
						code_410 = true;
						break;
					}//End of IF
				}//End of For
			}//End of while
			System.out.println("Code 410: " + code_410);
			assertEquals(true,code_410);
		} catch (Error e) {
			// TODO Auto-generated catch block
			System.out.println(e.code);
			System.out.println(e.message);
			fail();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//End of waitForTwofaStatus
	
	@Test
	public void Z_21_sendToken(){
		System.out.println("Z_21.- Send Token");
		try {
			HashMap<String, Object> twofa = new HashMap<String, Object>();
			twofa.put("token", "test");
			boolean result = credential.set_twofa(session, twofa);
			System.out.println("Token sent: " + result);
			assertEquals(true,result);
		} catch (Error e) {
			// TODO Auto-generated catch block
			System.out.println(e.code);
			System.out.println(e.message);
			fail();
		} 
	}//End of sendToken
	
	@Test
	public void Z_22_getAccounts(){
		System.out.println("Z_22.- Get Accounts");
		try {
			List<Account> accounts = Account.get(session);
			System.out.println("Accounts: " + accounts.size());
			assertNotNull(accounts);
		} catch (Error e) {
			// TODO Auto-generated catch block
			System.out.println(e.code);
			System.out.println(e.message);
			fail();
		} 
	}//End of sendToken
	
	@Test
	public void Z_23_getTransaction(){
		System.out.println("Z_23.- Get Transactions");
		try {
			List<Transaction> transactions = Transaction.get(session);
			System.out.println("Transactions: " + transactions.size());
			assertNotNull(transactions);
		} catch (Error e) {
			// TODO Auto-generated catch block
			System.out.println(e.code);
			System.out.println(e.message);
			fail();
		} 
	}//End of getTransaction
	
	@Test
	public void Z_24_getCountTransaction(){
		System.out.println("Z_24.- Get Count Transactions");
		try {
			int transactions = Transaction.get_count(session);
			System.out.println("Transactions Count: " + transactions);
			assertNotNull(transactions);
		} catch (Error e) {
			// TODO Auto-generated catch block
			System.out.println(e.code);
			System.out.println(e.message);
			fail();
		} 
	}//End of getCountTransaction
	
	@Test
	public void Z_25_getCountAttachment(){
		System.out.println("Z_25.- Get Count Attachments");
		try {
			int attachments = Attachment.get_count(session);
			System.out.println("Attachments Count: " + attachments);
			assertNotNull(attachments);
		} catch (Error e) {
			// TODO Auto-generated catch block
			System.out.println(e.code);
			System.out.println(e.message);
			fail();
		} 
	}//End of sendToken
	
	@Test
	public void Z_26_getAttachment(){
		System.out.println("Z_26.- Get Attachment");
		try {
			List<Attachment> attachments = Attachment.get(session);
			System.out.println("Attachments: " + attachments.size());
			String id_attachment = attachments.get(0).id_attachment;
			HashMap<String,Object> attachment = Attachment.get_extra(session, id_attachment);
			System.out.println(attachment.toString());
			assertNotNull(attachments);
		} catch (Error e) {
			// TODO Auto-generated catch block
			System.out.println(e.code);
			System.out.println(e.message);
			fail();
		} 
	}//End of getTransaction
	
}
