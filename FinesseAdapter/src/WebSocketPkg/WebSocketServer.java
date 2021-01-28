package WebSocketPkg;


import javax.websocket.server.*;



import javax.websocket.*;
import java.util.Map;
import java.util.List;


import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Websocket  implementation layer
 * 
 * 
 * Websocket  class
 *
 * This class provides API for external users to invoke websocket connections. It takes Agent Id and onetime token as inputs.
 * 
 * @author Manoj Raju
 *
 */

@ServerEndpoint("/endpoint")
public class WebSocketServer {
	
	Authentication authenticator = Authentication.getInstance();
	
	CustomLogger logger = CustomLogger.getInstance();
	
	@OnOpen
	public void onOpen(Session session) {
		
		System.out.println("Open Connection...");
		Map<String,List<String>> params = session.getRequestParameterMap();
		System.out.println("params passed : " + params.toString());
		
		String AgentId = params.get("AgentId").get(0).toString();
		
		if (params.get("accesstoken")!=null) { 
			if ((AgentId!=null) && (AgentId.length()<=10)) {
				if (authenticator.verifyAccessToken(params.get("accesstoken").get(0).toString(),AgentId.replace(" ",""))) {
					PushService.initialize();
					PushService.add(session,AgentId);
				
				} else {
					System.out.println("invalid token");
					onClose(session);
				}
			} 
		}
	}
	
	@OnClose
	public void onClose(Session session) {
		System.out.println("Close Connection ...");
		PushService.remove(session);
		
		
	}
	
	
	@OnMessage
	public String onMessage(String message) throws Exception {
		System.out.println("Message from the client:" + message);
		throw new Exception("OnMessage operation not supported");
		//String echoMsg = "Message from server: this operation is not supported";
		//return echoMsg;
		
	}
	
	@OnError
	public void onError(Throwable e) {
		e.printStackTrace();
		Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, e);
		
	}
	
}
