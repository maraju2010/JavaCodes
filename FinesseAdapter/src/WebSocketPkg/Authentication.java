package WebSocketPkg;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.util.Base64;  


/**
 * Middleware Layer 
 * 
 * Authentication class
 *
 * This class authenticates user based on username,password & Agent Id provided by user.
 *  It returns one time token for authorization with websocket.
 * 
 * @author Manoj Raju
 */

public class Authentication {
	
	// purpose : This class provides model for authentication and authorization.
	
	private static Authentication instance = null;
	private static DBAccess dbinstance = null;
	private static Security secureinstance = null;
	private SecureRandom random;
	private Cache<String, String> accessTokens;

	private static Map<String, String> users = new HashMap<>();
	
	CustomLogger logger = CustomLogger.getInstance();
	
	private Authentication (){
		  		// purpose :constructor to initiate authentication object
				 { random = new SecureRandom();
				 	accessTokens = CacheBuilder.newBuilder()
		             .expireAfterAccess(60, TimeUnit.SECONDS) // Entries expire in 15 seconds
		             .build();
				 }
				 
				 {
				        users.put("admin", "e39b9c178b2c9be4e99b141d956c6ff6");
				        users.put("crmuser", "e39b9c178b2c9be4e99b141d956c6ff6");
				    }
				 
				 try {
						dbinstance = new DBAccess();
						secureinstance = Security.getInstance();
						
					} catch(Exception e) {
						logger.LOGGER.log(Level.SEVERE, e.toString());
					}
	        
	  }
	  
	  public static Authentication getInstance()
	   	// purpose: singleton method to generate authentication instance.
	    { 
		  	
	        if (instance == null) 
	            instance = new Authentication(); 
	  
	        return instance; 
	    }  
	  
	
	public String generateAccessToken(String username) {
		// Purpose : invoke method to generate token and store in Table. 
        String accessToken = generateRandomString();
        accessTokens.put(accessToken, username);
        return accessToken;
    }
	
	 private String generateRandomString() {
		// Purpose : generate random Token String for websocket connections.
		 	System.out.println(random);
	        return new BigInteger(130, random).toString(32);
	    }
	 
	@SuppressWarnings("static-access")
	public boolean verifyCredentials (String AgentId, String auth) throws SQLException, NoSuchAlgorithmException {
		// Purpose : verify credentials entered by user. It first checks in it's temp table if username and password exists.
		//or else invokes DB lookup 
			boolean cond = false;
			
			String dStr = decodeBase64(auth);
			String username = getUsername(dStr);
			String password = getPassword(dStr);
		
			
			
		 	if (this.users.containsKey(username)) {
		 		logger.LOGGER.info("verifying credentials" );
		 		String hashtext = users.get(username);
		 		System.out.println(hashtext);
		 		//cond = hashtext.equals(getMD5(password));
		 		cond = hashtext.equals(password);
		 		if (cond) {
		 			
		 			Person p = checkAgainstAW(AgentId);
			 		//System.out.println(p.user);
			 		if (p.agent != null) {
			 			cond = true;
			 			return cond;
			 		} else {
			 			cond = false;
			 			return cond;
			 			
			 		}
		 			
		 		} else {
		 			return cond;
		 		}
		 	} else {
		 		return cond;
		 	}
	  }
	
	
	public Person checkAgainstAW(String AgentId) throws SQLException {
		// Purpose : invoke DB lookup to fetch credentials from ICM AW.
		Person p = dbinstance.validateUser(AgentId);
		return p;
		
	}
	
	 public boolean verifyAccessToken(String accessToken,String accessUser) {
		// Purpose :Validate access token provided by user while connecting to websocket.
		 
		 //System.out.println("access Tokens list:" + accessTokens.toString());
		 
		 String username = accessTokens.getIfPresent(accessToken);
		 logger.LOGGER.info("accessTokenuser : " + username + " " +  "compare with " + "AgentId: " + accessUser);
		 
		  if (username == null) {
	            return false;
	        } else if (username.equals(accessUser)) {
	        	accessTokens.invalidate(accessToken); // The token can be used only once
	            return true;
	        	
	        }else {
	        	return false;
	        }
		 
	 }
	 
	  
	 public static String getMD5(String data) throws NoSuchAlgorithmException { 
		 	// Purpose :ICM AW stores password in MD5 format.This method convert user provided plain text password to MD5.
		 	MessageDigest md = MessageDigest.getInstance("MD5");

		 	byte[] messageDigest = md.digest(data.getBytes()); 
		 	BigInteger no = new BigInteger(1, messageDigest);
		 	String hashtext = no.toString(16); 
		 	while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            }
		 	System.out.println(hashtext);
            return hashtext;
	    }	
	 
	 
	  public static String decodeBase64(String auth) {
		 String [] authval = auth.split(" ");
		 Base64.Decoder decoder = Base64.getDecoder();  
		 String dStr = new String(decoder.decode(authval[1]));
		 
		 return dStr;
	 }
	 
	 public static String getUsername(String dStr) {
		 String[] parts = dStr.split(":");
		 String Username = parts[0]; 
		 return Username;
	 }
	 
	 
	 public static String getPassword(String dStr) {
		 String[] parts = dStr.split(":");
		 String Password = parts[1]; 
		 return Password;
		 
	 }
	
	 
	 public  boolean checkInput(String AgentId) {
		 
		  return secureinstance.enterChecks(AgentId);
		 
	 }
	 
	 public  boolean checkInput(String StartDate,String EndDate) {
		 return secureinstance.enterChecks(StartDate,EndDate);
	 }
	 
	
}
 