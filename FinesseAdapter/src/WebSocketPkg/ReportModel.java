package WebSocketPkg;

import org.json.JSONArray;

/**
 * Utility Class
 * 
 * 
 * ReportModel 
 *
 * This class provides temporary storage for Agent values returned from DB layer.
 * 
 * @author Manoj Raju
 *
 */


public class ReportModel {
	
	  String StartDate;
	  String EndDate;
	  String CallsPerAHT12Mins;
	  String CallsPerAHT10Mins;
	  String CallsPerAHT8Mins;
	  String CallsPerAHT4Mins;
	  String AgentId; 
	  String CallsHandledTCD;
	  JSONArray array = new JSONArray();
	 
	  
}
