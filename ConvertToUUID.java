package appstartcode;

import com.audium.server.AudiumException;
import com.audium.server.session.ActionElementData;
import com.audium.server.voiceElement.ActionElementBase;



public class ConvertToUUID extends ActionElementBase  {

    public void doAction(String string, ActionElementData aed) throws Exception {
        //To change body of generated methods, choose Tools | Templates.
        String _t_callid;
        String temp_t_callid;
        String ucid;
        String prefix = "04C820";
        temp_t_callid = (String) aed.getSessionData("t_callid");
        
        // set ta_callid and ucid.
        try{
	        _t_callid = removePrefix(temp_t_callid,prefix);        
	        ucid = setUCID(temp_t_callid);
	        
	        if(_t_callid!= null ){
	        	StringBuilder builder = new StringBuilder();
	        	
	        	for (int i = 0; i < _t_callid.length(); i = i + 2) {
	        		// Step-1 Split the hex string into two character group
	        		String s = _t_callid.substring(i, i + 2);
	        		// Step-2 Convert the each character group into integer using valueOf method
	        		//Integer n = Integer.valueOf(s, 16);
	        		// Step-3 Cast the integer value to char       
	        		builder.append((char) Integer.parseInt(s, 16));
	        		
	        	}
	        	aed.setSessionData("t_callid",builder.toString().replaceAll("\\P{Print}", "").trim());
	        	aed.addToLog("new t_callid:", (String) aed.getSessionData("t_callid"));
	        }
	        else {
	        	aed.setSessionData("t_callid",_t_callid);
	        	aed.addToLog("converted t_callid:",(String) aed.getSessionData("t_callid"));
	        }
	        
	        aed.setSessionData("ucid",ucid);
	        aed.addToLog("ucid:", (String) aed.getSessionData("ucid"));
	        aed.setSessionData("uuierror","false");
	    
        }catch (Exception e) {
    	
        	aed.setSessionData("uuierror","true");
        }  
	    
    }
    
    public static String removePrefix(String s, String prefix) {
        int p;
        p = s.indexOf("FA");
        if (s.startsWith("04")) {
            s = s.substring(prefix.length(), p);
            //System.out.println(s);
        }
        
        if (s.startsWith("00FA")) {
            s = null;
            //System.out.println(s);
        }
        return s;
    }
    
    
    public static String setUCID(String s) {
    	int init;
        int start=0;
        int end=4;
        String temp;
        StringBuilder builder = new StringBuilder();
    	if (s.indexOf("FA")>0) {
            init = s.indexOf("FA");
            s = s.substring(init+4,s.length());
            //System.out.println(s);
            for (int i = 0;i<=2;i++) {
                temp = getDecimal(slice(s,start,end));
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
    
    
    public static String slice(String s,int start,int end) {
            return s.substring(start,end);
    
    }
    
    
    public static String getDecimal(String hex) {
            String digit = null;
            //System.out.println(hex);
            if (hex.length()==4) { 
            	
            	int val = Integer.parseInt(hex, 16);
            	
                if (Integer.toString(val).length()==1) {
                    digit = "0000";
                    digit = digit + Integer.toString(val);
                
                }
            
                if (Integer.toString(val).length()==2) {
                    digit = "000";
                    digit = digit + Integer.toString(val);
            
                }
            
                if (Integer.toString(val).length()==3) {
                    digit = "00";
                    digit = digit + Integer.toString(val);
            
                }
            
                if (Integer.toString(val).length()==4) {
                    digit = "0";
                    digit = digit + Integer.toString(val);
                     
                }
             
                if (Integer.toString(val).length()==5){
                 
                	digit = Integer.toString(val);
             	}
                 
             
            } else {
            	if (hex.length()>4){
                    int val = Integer.parseInt(hex, 16);                    
                    digit = Integer.toString(val);
                    for (int n = 0 ;n<(10 - Integer.toString(val).length());n++){
                        digit = "0" + digit;
                       
                   }
                }   
            }
            
            return digit;
         
    }
    
    
}    

