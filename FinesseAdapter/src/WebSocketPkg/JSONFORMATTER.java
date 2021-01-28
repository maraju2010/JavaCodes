package WebSocketPkg;

import java.sql.SQLException;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.sun.rowset.CachedRowSetImpl;

/**
 * Utility Class
 * 
 * 
 * JSONFORMATTER
 *
 * This class convert data into JSON format.
 * 
 * @author Manoj Raju
 *
 */

public class JSONFORMATTER {
	
	public static JSONObject formatData(CachedRowSetImpl crs, String key) throws JSONException, SQLException  {
		
		  Date d = new Date(System.currentTimeMillis());
		  JSONObject obj = new JSONObject();
		  System.out.println(key);
		  obj.put("PeripheralNumber",key);
		  obj.put("LoggedOnTime",crs.getString("LoggedOnTime"));
		  obj.put("NotReadyTime",crs.getString("NotReadyTime"));
		  obj.put("AvailTime",crs.getString("AvailTime"));
		  obj.put("ReservedStateTime", crs.getString("ReservedStateTime"));
		  obj.put("Date",crs.getString("Date1"));
		  obj.put("FetchTime",d.toString());
		  obj.put("CallsHandled", crs.getString("CallsHandled"));
		  obj.put("TalkTime", crs.getString("TalkTime"));
		  obj.put("WrapupTime", crs.getString("WrapupTime"));
		  obj.put("HoldTime", crs.getString("HoldTime"));
		  obj.put("CallsHandledTCD", crs.getString("CallsHandledTCD"));
		  obj.put("AHT8mins", crs.getString("AHT8mins"));
		  obj.put("AHT10mins", crs.getString("AHT10mins"));
		  obj.put("AHT12mins", crs.getString("AHT12mins"));
		  obj.put("AHT4mins", crs.getString("AHT4mins"));
		  
		  return obj;
	}
	
	
	public static JSONArray convertoObj(ReportModel rp, TempDict jd) throws JSONException {
		// TODO Auto-generated method stub
		JSONObject reportdata = formatData(rp);
		rp.array.put(reportdata);
		return rp.array;
		
	}

	public static JSONObject formatData(ReportModel reportdata) throws JSONException  {
		
		  Date d = new Date(System.currentTimeMillis());
		  JSONObject obj = new JSONObject();
		  obj.put("PeripheralNumber",reportdata.AgentId);
		  obj.put("StartDate",reportdata.StartDate);
		  obj.put("EndDate",reportdata.EndDate);
		  obj.put("CallsPerAHT4Mins",reportdata.CallsPerAHT4Mins);
		  obj.put("CallsPerAHT8Mins",reportdata.CallsPerAHT8Mins);
		  obj.put("CallsPerAHT10Mins",reportdata.CallsPerAHT10Mins);
		  obj.put("CallsPerAHT12Mins",reportdata.CallsPerAHT12Mins);
		  obj.put("CallsHandledTCD",reportdata.CallsHandledTCD);
		  obj.put("FetchTime",d.toString());
		  return obj;
		  
	}

}
