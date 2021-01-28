package WebSocketPkg;


/**
 * Utility Class
 * 
 * 
 * Websocket Access Token class
 *
 * This class provides temporary storage for one time token.
 * 
 * @author Manoj Raju
 *
 */

public class WebSocketAccessToken {

	
	 private String token;

     public WebSocketAccessToken() {

     }

     public WebSocketAccessToken(String token) {
         this.token = token;
     }

     public String getToken() {
         return token;
     }

     public void setToken(String token) {
         this.token = token;
     }
 
}
