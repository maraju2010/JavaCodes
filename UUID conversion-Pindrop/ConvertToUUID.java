package ttsclass;

import com.audium.server.AudiumException;
import com.audium.server.session.ActionElementData;
import com.audium.server.voiceElement.ActionElementBase;

public class ConvertToUUID extends ActionElementBase  {
	
	 //declare global variables
	
	final static String nl_ucidIndex = "FA08";
    final static String ex_ucidIndex = "00FA";
    final static String mx_ucidIndex = "FB08";
    final static String px_0000 = "0000";
    final static String px_000 = "000";
    final static String px_00 = "00";
    final static String px_0 = "0";
    final static String prefix = "04C820";
    final static String st_keyword = "04";

    public void doAction(String string, ActionElementData aed) throws Exception {
  
        //declare local variables
  
        String _ta_callid;
        String temp_ta_callid;
        String ucid;
        String uuierror;
        
        // ICM will send ta_callid as one of the session parameters to vxml app.
        
        // block for parsing ta_callid and ucid. 
        
        try{
        	temp_ta_callid = (String) aed.getSessionData("ta_callid");
        	aed.addToLog("temp_ta_callid:",temp_ta_callid);
	        _ta_callid = removePrefix(temp_ta_callid,prefix,aed);  
	        aed.addToLog("_ta_callid:",_ta_callid);
	        ucid = setUCID(temp_ta_callid,aed);
	        aed.addToLog("ucid:",ucid);
	        
	        if(_ta_callid != null ){
	        	StringBuilder builder = new StringBuilder();
	        	
	        	for (int i = 0; i < _ta_callid.length(); i = i + 2) {
	        		// Step-1 Split the hex string into two character group
	        		String s = _ta_callid.substring(i, i + 2);
	        		// Step-2 Convert the each character group into integer using valueOf method
	        		//Integer n = Integer.valueOf(s, 16);
	        		// Step-3 Cast the integer value to char       
	        		builder.append((char) Integer.parseInt(s, 16));
	        		
	        	}
	        	
	        	//write event to activity log.
	        	
	        	aed.setSessionData("ta_callid",builder.toString().replaceAll("\\P{Print}", "").trim());
	        	aed.addToLog("new ta_callid:", (String) aed.getSessionData("ta_callid"));
	        } else {
	        	
	        	//write event to activity log.
	        	
	        	aed.setSessionData("ta_callid",_ta_callid);
	        	aed.addToLog("converted ta_callid:",(String) aed.getSessionData("ta_callid"));
	        }
	        
	        //write event to activity log.
	        aed.setSessionData("ucid",ucid);
	        aed.addToLog("ucid:", (String) aed.getSessionData("ucid"));
	        uuierror = "false";
	        aed.setSessionData("uuierror",uuierror);
	    
        }catch (Exception e) {
        	// passing this field back to ICM will allow to perform enhanced routing based on failure
        	//condition.
        	
        	uuierror = "true";
        	aed.setSessionData("uuierror",uuierror);
        	aed.addToLog("exception generated:",e.toString());
        	
        }  
	    
    }
    
    public static String removePrefix(String s, String prefix,ActionElementData aed) {
    	// method to remove extra padding characters from the string.
    	
        int p;
        String temp_s;
        if (s.startsWith(st_keyword)) {
        	p = s.indexOf(nl_ucidIndex);
            temp_s = s.substring(prefix.length(), p);
            //System.out.println(s);
        }  else {
        	
        	temp_s = null;
        	
        }
        aed.addToLog("temp_s:",temp_s);
        return temp_s;
    }
    
    
    public static String setUCID(String s,ActionElementData aed) {
    	// method to parse pindrop ucid.
    	// corner case exist where multiple ucid are sent from SBC. FA, FB,FC,FD . Each ucid is 20 
        //characters in length.

    	int init;
        int start=0;
        int end=4;
        int strlength = 0;
        
        String temp;
        StringBuilder builder = new StringBuilder();
    	if (s.indexOf(nl_ucidIndex)>0) {
    		// step 1: find length of entire string
            strlength = findLength(s,aed);
            
         // step 2: fetch last 20 characters from original string
            s = s.substring((strlength - 20) ,strlength);
            aed.addToLog("After_length_check:",s);
            //System.out.println(s);
            
         // step 3: standardize the string  to FA08 prefix    
            s = changeprefix(s,aed);
            aed.addToLog("ucid_s:",s);
            
        // step 4: increase the index of FA08    
            init = s.indexOf(nl_ucidIndex);
            
        // step 5 :  strip FA08 and send remaining string for parsing ucid.
            s = s.substring(init+4,s.length());
            
            for (int i = 0;i<=2;i++) {
                temp = getDecimal(slice(s,start,end,aed),aed);
                if (i==1){
                    start=end;
                    end=s.length(); 
                
                }else {
                    start=end;
                    end = end+4;                    
                };
                
                if(temp!=null) {
                    builder.append(temp);
                
                }
                              
            }
        }
    	
        return builder.toString();
    
    }
    
    
    private static String changeprefix(String s,ActionElementData aed) {
		// TODO Auto-generated method stub
    	if(s.substring(0, 2).equals("FA")) {
    		
    		
    	} else {
    		String multi_ucid_id = s.substring(0, 2);
    		s = s.replace(multi_ucid_id, "FA");
    		
    	}
    	aed.addToLog("changeprefix:",s);
    	return s;
	}

	public static String slice(String s,int start,int end,ActionElementData aed) {
    		//utility method to return 4 characters of UCID for parsing for counter 0 & 1.
    		// for counter 2: 6 characters is  returned.
			
            return s.substring(start,end);
    
    }
    
    
    public static int  findLength(String s,ActionElementData aed) {
    	
    	return s.length();;
    }	
    
    
    
    public static String getDecimal(String hex,ActionElementData aed) {
    		// method for formatting ucid. 
    	
            String digit = null;
            //System.out.println(hex);
            if (hex.length()==4) { 
            	
            	int val = Integer.parseInt(hex, 16);
            	
                if (Integer.toString(val).length()==1) {
                    digit = px_0000;
                    digit = digit + Integer.toString(val);
                
                }
            
                if (Integer.toString(val).length()==2) {
                    digit = px_000;
                    digit = digit + Integer.toString(val);
            
                }
            
                if (Integer.toString(val).length()==3) {
                    digit = px_00;
                    digit = digit + Integer.toString(val);
            
                }
            
                if (Integer.toString(val).length()==4) {
                    digit = px_0;
                    digit = digit + Integer.toString(val);
                     
                }
             
                if (Integer.toString(val).length()==5){
                 
                	digit = Integer.toString(val);
             	}
                 
             
            } else {
            	if (hex.length()>4){
                    int val = Integer.parseInt(hex, 16);                    
                    digit = Integer.toString(val);
                    aed.addToLog("digit_parse:",digit);
                    for (int n = 0 ;n<(10 - Integer.toString(val).length());n++){
                        digit = px_0 + digit;
                       
                   }
                }   
            }
            
            aed.addToLog("final_digit:",digit);
            return digit;
         
    }
    
    
}    

