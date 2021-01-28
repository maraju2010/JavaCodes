package WebSocketPkg;

import java.text.SimpleDateFormat;
import java.util.Date;



public class Security {

	
	private static Security instance = null;
	
	
	private Security (){
		
		
	}
	
	public static Security getInstance()
	   	// purpose: singleton method to generate authentication instance.
	    { 
		  	
	        if (instance == null) 
	            instance = new Security(); 
	  
	        return instance; 
	 }  
	
	public  boolean enterChecks(String agent) {
		boolean result = false;
		try {
			result = checkLength(agent);
			if (result) {
				result = checkRange(agent);
				if (result) {
					result = checkPlausible(agent);
					if(result) {
						result = checkPresence(agent);
					}
				}	
			}
				
		} catch (Exception e) {
			
		}
		System.out.println(result);
		return result;
		
	}
	
	public  boolean enterChecks(String startdate,String endDate) {
		boolean result = false;
		
		try {
			result = checkLength(startdate,endDate);
			System.out.println("checklength:" + result);
			if (result) {
				result = checkRange(startdate,endDate);
				System.out.println("checrange:" + result);
				if (result) {
					result = checkPlausible(startdate,endDate);
					System.out.println("plausibleresult:" + result);
					if(result) {
						result = checkPresence(startdate,endDate);
					}
				}	
			}
				
		} catch (Exception e) {
			
		}
		
		return result;
		
	}
	
	private  boolean checkLength(String data) {
		boolean result = false;
		
		 if (data.length()<=10) {
			 
			 result = true;
		 }
		 System.out.println(result);
		return result;
	}
	
	private  boolean checkRange(String data) {
		boolean result = false;
		 for(int i=0; i < data.length(); i++) {
	         Boolean flag = Character.isDigit(data.charAt(i));
	         if(flag) {
	            result=true;
	         }
	         else {
	            result = false;
	            break;
	         }
	      }
		 //System.out.println(result);
		return result;
	}
	
	private  boolean checkPlausible(String data) {
		boolean result = false;
		
		if(data.startsWith("0")) {
			result = false;
		} else {
			result = true;
		} 
		//System.out.println(result);
		return result;
		
	}
	
	private  boolean checkPresence(String data) {
		boolean result = false;
		if (data.isEmpty()) {
			
			result = false;
		} else {
			
			result = true;
		}
			
		//System.out.println(result);	
		return result;
		
	}
	
	
	
	private  boolean checkLength(String data1,String data2) {
		boolean result = false;
		System.out.println(data1);
		System.out.println(data2);
		 if (data1.length()<25) {
			 result=true;
			 
		 }else if (data2.length()<25) {
			 
			 result = true;
		 } else {
			 result = false;
		 }
		
		 //System.out.println(result);
		return result;
	}
	
	private  boolean checkRange(String data1,String data2) throws java.text.ParseException {
		boolean result = false;
		Date date1 = null;
		Date date2 = null;
		
		SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdfrmt.setLenient(false);
		   try {
			   date1  = sdfrmt.parse(data1);
			   date2  = sdfrmt.parse(data2);
			   if(date1 != null) {
				   result = true;
			   } else if (date2.toString().startsWith("2020")) {
				   result = true;
			   } else {
				   
				   result = false;
			   }
	        } catch (Exception e) {
	        	//System.out.println(e);
	            result = false;
	        }
		   //System.out.println(result); 
		return result;
	}
	
	private  boolean checkPlausible(String data1,String data2) {
		boolean result = false;
		
		if(data1.startsWith("00")) {
			result = false;
		} else if (data2.startsWith("00")) {
			result = false;
		}else {
			result = true;
		}
		 System.out.println(result);
		return result;
		
	}
	
	private  boolean checkPresence(String data1,String data2) {
		boolean result = false;
		if (data1.isEmpty()) {
			
			result = false;
		} else if (data2.isEmpty()) {
			result = false;
		} else {
			
			result = true;
		}
			
		 System.out.println(result);	
		return result;
		
	}
	
	
}
