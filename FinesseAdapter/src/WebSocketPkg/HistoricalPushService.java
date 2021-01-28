package WebSocketPkg;


import java.sql.SQLException;
import java.util.logging.Level;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Middleware Layer
 * 
 * HistoricalPushService class
 *
 * This class provides middleware layer between HistoricalReport interface and DB Access layer.
 * 
 * @author Manoj Raju
 */


public class HistoricalPushService {
	
	private static DBAccess dbinstance = null;
	
	private static HistoricalPushService instance = null;
	
	CustomLogger logger = CustomLogger.getInstance();
	
	private HistoricalPushService (){
		  		// purpose :constructor to initiate authentication object
				 try {
						dbinstance = new DBAccess();
						
					} catch(Exception e) {
						logger.LOGGER.log(Level.SEVERE, e.toString());
					}
	        
	  }
	  
	  public static HistoricalPushService getInstance()
	   	// purpose: singleton method to generate authentication instance.
	    { 
		  	
	        if (instance == null) 
	            instance = new HistoricalPushService(); 
	  
	        return instance; 
	    } 
	
	
	public  JSONObject getHistoricalReport(String agentId, String starttime, String endtime) throws JSONException, SQLException {
		// TODO Auto-generated method stub
		TempDict reportdata = dbinstance.getAgentReport(agentId,starttime,endtime);
		return reportdata.Jsondict;
	}

}
