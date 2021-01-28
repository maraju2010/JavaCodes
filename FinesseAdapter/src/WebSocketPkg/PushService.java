package WebSocketPkg;

import java.util.Map;
import java.util.logging.Level;
import java.util.HashMap;
import java.io.IOException;
import java.sql.SQLException;

import com.sun.rowset.CachedRowSetImpl;
import org.json.*;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.Session;


/**
 * Middleware Layer
 * 
 * 
 * PushService Class
 *
 * This class provides middleware layer between DB layer and websocket interface
 * 
 * @author Manoj Raju
 *
 */


public class PushService implements Runnable {
	
	// purpose: This class implements this middleware layer. 
	
	private static PushService instance;
	private static DBAccess dbinstance;
	private static Map<String, Session> sMap = new HashMap<String,Session>();
	CustomLogger logger = CustomLogger.getInstance();
	ConfigProp customprop = ConfigProp.getInstance();
	int loopTimer;
	
	
	private PushService() {}
	
	public static void add(Session s,String AgentId) {
		//purpose : add session and agentId to list
		
		sMap.put(AgentId, s);
		
		//System.out.println(AgentId);
		//System.out.println(s);
		
	}
	
	public static void initialize() {
		// purpose : constructor to intialize push service instance
		if(instance == null) {
			
			instance = new PushService();
			new Thread(instance).start();
			
			
		}
		
		if (dbinstance == null) {
			try {
				dbinstance = new DBAccess();
				
			} catch(Exception e) {
				e.printStackTrace();
			}	
		}
	}
	
	@Override
	public void run() {
		// purpose : thread run method. invokes DB interface in every 10 mins. 
		if (customprop.ThreadLoopTimer > 0) {
			loopTimer = customprop.ThreadLoopTimer;
		} else {
			loopTimer = 2000;
		}
		
		while (true) {
			try {
				
				Thread.sleep(10*loopTimer);
				if (sMap.isEmpty()) {
					System.out.println("Skipping DB lookup, HashMap is empty");
				} else {
					
					CachedRowSetImpl crs = dbinstance.getRealTime();
					System.out.println(crs);
					while(crs.next()) {
						  String key = crs.getString("PeripheralNumber");
						  if (sMap.containsKey(key)) {
							  Session s = sMap.get(key);
							  JSONObject obj = JSONFORMATTER.formatData(crs,key);
							  //System.out.println(key);
							  //System.out.println(s);					  
								
								if(s.isOpen()) {
									send(s,obj);
									
									} else {
										
									sMap.remove(key);
								}
								
						  } else {
						  
							 System.out.println(key + "	is not present in table");
						  }
							
					}
				}
				
			} catch (SQLException e) {
				logger.LOGGER.log(Level.SEVERE, e.toString());
				try {
					sendError(sMap,e);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					logger.LOGGER.log(Level.SEVERE, e1.toString());
				}
			} catch (Exception e) {
				logger.LOGGER.log(Level.SEVERE, e.toString());
				try {
					sendError(sMap,e);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					logger.LOGGER.log(Level.SEVERE, e1.toString());
				}
			}
	}
	
}
	
	

	private void sendError(Map<String, Session> sMap2, Exception e) throws IOException {
		// TODO Auto-generated method stub
		for (Map.Entry<String, Session> set : sMap2.entrySet()) {
			Session s = set.getValue();	
			if(s.isOpen()) {
				CloseReason closeReason = new CloseReason(CloseCodes.UNEXPECTED_CONDITION, e.getMessage());
				s.close(closeReason);
			}
		}
		
	}

	private void send(Session s, JSONObject obj) throws IOException {
		// TODO Auto-generated method stub
		s.getBasicRemote().sendText(obj.toString());
		
	}
	

	public static void remove (Session s) {
		try {
			s.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	

	
	
}	
