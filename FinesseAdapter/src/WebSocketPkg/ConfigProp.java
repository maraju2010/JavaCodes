package WebSocketPkg;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.logging.Level;


/**
 * Utility Class
 * 
 * ConfigProp class
 *
 * This class reads config.properties file and shares the attributes for other class.
 * 
 * @author Manoj Raju
 */


public class ConfigProp {
	String sqlquery1;
	String sqlquery2;
	String sqlquery3;
	Integer ThreadLoopTimer;
	CustomLogger logger = CustomLogger.getInstance(); 
	private static ConfigProp configprop = null;
	
	private ConfigProp() {
		
		try (InputStream input = this.getClass().getResourceAsStream("config.properties")) {

   		 Properties prop = new Properties();
   		 prop.load(input);
   		 //this.db_url = prop.getProperty("db.url");
   		 //this.sqluser = prop.getProperty("db.user");
            //this.sqlpass = prop.getProperty("db.password");
            this.sqlquery1 = prop.getProperty("db.AuthQuery");
            this.sqlquery2 = prop.getProperty("db.RealTimeDashboard");
            this.sqlquery3 = prop.getProperty("db.HistoricalDashboard");
            this.ThreadLoopTimer = Integer.parseInt(prop.getProperty("ThreadLoopTimer"));
           // this.instance = prop.getProperty("db.instance");
            //this.param1 = prop.getProperty("db.param1");
            //this.param2 = prop.getProperty("db.param2");
            //this.param3 = prop.getProperty("db.param3");
            //this.param4 = prop.getProperty("db.param4");
            //this.param5 = prop.getProperty("db.param5");
            //this.param6 = prop.getProperty("db.param6");
            //this.param7 = prop.getProperty("db.param7");
            //this.param8 = prop.getProperty("db.param8");
            //this.param9 = prop.getProperty("db.param9");
            //this.param10 = prop.getProperty("db.param10");
            
           
   	 } catch (Exception e) {
   		 
   		 logger.LOGGER.log(Level.SEVERE, e.toString());
   	 }
   }
	
	public static ConfigProp getInstance() {
		 if (configprop == null ) {
			 configprop = new ConfigProp();
		 } 
		 return configprop;
	}
	
	public static LocalDateTime getTodayDate() {
		
		LocalDateTime now = LocalDateTime.now();
		return now;
		
	}
	
    public static String getStartOfDay(LocalDateTime now) {
    	// purpose : get start dateTime. It is uses in stored procedure query to pass start datetime parameter.
    	String day = now.format(DateTimeFormatter.ISO_DATE);
        return day + " " + "00:00:00";
    }
    
    public static String getEndOfDay(LocalDateTime now) {
    	// purpose : get end dateTime. It is uses in stored procedure query to pass end datetime parameter.
    	String day = now.format(DateTimeFormatter.ISO_DATE);
        return day + " " + "23:59:59";
    }
	
}