package WebSocketPkg;

import java.util.logging.Logger;


/**
 * Utility Class
 * 
 * CustomLogger class
 *
 * This class provides logger instance to other class in this package.
 * 
 * @author Manoj Raju
 */

public class CustomLogger {
	
	private static CustomLogger customlogger = null;
	public final Logger LOGGER;
	
	
	private CustomLogger() {
		
		LOGGER = Logger.getLogger(CustomLogger.class.getName());
		
	}

	public static CustomLogger getInstance() {
		 if (customlogger == null ) {
			 customlogger = new CustomLogger();
		 } 
		 return customlogger;
	}
}
