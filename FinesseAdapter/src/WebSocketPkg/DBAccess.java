package WebSocketPkg;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.sun.rowset.CachedRowSetImpl;
import java.util.logging.Level; 
import java.sql.CallableStatement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONException;



/**
 * Internal Layer to Database.
 * 
 * DBAccess class
 *
 * This class provides SQL interface to invoke stored procedures on cisco AW server.
 * 
 * @author Manoj Raju
 */


public class DBAccess {
	
	// purpose: class holds DB session with ICM AW and invokes stored procedure calls.
	
	
	ConfigProp customprop ;
	CustomLogger logger = CustomLogger.getInstance(); 
     
    public DBAccess()  {
    	//purpose: constructor to invoke DB session with ICM AW.
    		customprop = ConfigProp.getInstance();
    		//	String sqluser = "root";
    		//String sqlpass = "Hitler.55";
    		//Class.forName("com.mysql.jdbc.Driver");
         
    }
     
    public CachedRowSetImpl getRealTime(){
    	// purpose: execute stored procedure and returns result rows.

    			//String StartTime = "2020-09-15 00:00:00";
    			//String EndTime = "2020-09-15 23:00:00";
    		    Context ctx = null;
    		    Connection connection = null;
    		    CallableStatement  cs = null;
    			ResultSet rs = null;
    			CachedRowSetImpl crs = null ;
    			//LocalDateTime now = customprop.getTodayDate();
    			//String StartTime = customprop.getStartOfDay(now);
    			//String EndTime = customprop.getEndOfDay(now);
    			
                try {
                	
                	ctx = new InitialContext();
                	DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/Finsqlserver");
                	connection = ds.getConnection();
            		cs = connection.prepareCall(customprop.sqlquery2);
                	//cs.setString(1, StartTime);
                    //cs.setString(2, EndTime);
                    System.out.println(cs);
                    rs = cs.executeQuery();
                    crs = new CachedRowSetImpl();
                    crs.populate(rs);
                      
                } catch(SQLException e) {
                	logger.LOGGER.log(Level.SEVERE, e.toString());
                    
                    
          } catch (NamingException e) {
					// TODO Auto-generated catch block
        	  logger.LOGGER.log(Level.SEVERE, e.toString());
		} finally {
			try {
				rs.close();
				cs.close();
				connection.close();
				ctx.close();
			} catch(SQLException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getSQLState());
                System.out.println(e.getErrorCode());
                
		} catch (NamingException e) {
				// TODO Auto-generated catch block
			logger.LOGGER.log(Level.SEVERE, e.toString());
      	} 
			
	} 
                
         return crs;   
    }
    
 
    


	public Person validateUser(String AgentId){
		// TODO Auto-generated method stub
		// purpose: stored procedure call to get user credentials from ICM AW.
		Person p = null;
		Context ctx = null;
		Connection connection = null;
		CallableStatement  cs = null;
		ResultSet rs = null;
		try {
			ctx = new InitialContext();
        	DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/Finsqlserver");
        	connection = ds.getConnection();
    		cs = connection.prepareCall(customprop.sqlquery1);	
        	cs.setString(1,AgentId);
            System.out.println(cs);
            rs = cs.executeQuery();
            System.out.println(rs);
            
            if (rs.next() == false) {
            	System.out.println("ResultSet in empty in Java"); 
            	p = new Person();
            	p.user = null;
            	p.pass = null;
            	p.agent = null;
            	
            } else {
            
            	do {
            		p = new Person();
            		//p.user=rs.getString("username"); 
            		//p.pass=rs.getString("password").toLowerCase();
            		p.agent=rs.getString("AgentId");
            		p.user = null;
                	p.pass = null;
            		System.out.println(p.agent);
            	}while (rs.next());
            }
            
        } catch(SQLException e) {
        	logger.LOGGER.log(Level.SEVERE, e.toString());
        } catch (NamingException e) {
			// TODO Auto-generated catch block
        	logger.LOGGER.log(Level.SEVERE, e.toString());
		} finally {
			try {
				rs.close();
				cs.close();
				connection.close();
				ctx.close();
				
			} catch(SQLException e) {
				logger.LOGGER.log(Level.SEVERE, e.toString());
	        } catch (NamingException e) {
				// TODO Auto-generated catch block
	        	logger.LOGGER.log(Level.SEVERE, e.toString());
			}
			
		}
        
		return p;	
		
	}

	public TempDict getAgentReport(String agentId, String starttime, String endtime) throws JSONException {
		// TODO Auto-generated method stub
		logger.LOGGER.info(" method : getAgentReport() invoke db stored procedure for historical agent report");;
		ReportModel rp = null;
		TempDict jd = null;
		Context ctx = null;
		Connection connection = null;
		CallableStatement  cs = null;
		ResultSet rs = null;
		try {
			ctx = new InitialContext();
        	DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/Finsqlserver");
        	connection = ds.getConnection();
    		cs = connection.prepareCall(customprop.sqlquery3);	
        	cs.setString(1,agentId);
        	cs.setString(2,starttime);
        	cs.setString(3,endtime);
            System.out.println(cs);
            rs = cs.executeQuery();
            System.out.println(rs);
            jd = new TempDict();
            
            if (rs.next() == false) {
            	System.out.println("ResultSet in empty in Java"); 
            	rp = new ReportModel();
            	
            	rp.CallsPerAHT4Mins = null;
            	rp.CallsPerAHT8Mins = null;
            	rp.CallsPerAHT10Mins = null;
            	rp.CallsPerAHT12Mins = null;
            	rp.AgentId = null;
        		rp.CallsHandledTCD = null;
            	String date = rs.getString("Date3");
            	
            	JSONArray ja = JSONFORMATTER.convertoObj(rp,jd);
            	
            	jd.Jsondict.put(date, ja);
            	
            	
            	
            } else {
            	
            	do {
            		rp = new ReportModel();
            		//p.user=rs.getString("username"); 
            		//p.pass=rs.getString("password").toLowerCase();
            		rp.CallsPerAHT4Mins =rs.getString("AHT4mins");
            		rp.CallsPerAHT8Mins =rs.getString("AHT8mins");
            		rp.CallsPerAHT10Mins =rs.getString("AHT10mins");
            		rp.CallsPerAHT12Mins =rs.getString("AHT12mins");
            		rp.AgentId =rs.getString("PeripheralNumber");
            		rp.CallsHandledTCD =rs.getString("CallsHandledTCD");
            		String date = rs.getString("Date3");
            		JSONArray ja = JSONFORMATTER.convertoObj(rp,jd);
                	
                	jd.Jsondict.put(date, ja);
            		//System.out.println(p.agent);
            	}while (rs.next());
            }
            
        } catch(SQLException e) {
        	
            logger.LOGGER.log(Level.SEVERE, e.toString());
        } catch (NamingException e) {
			// TODO Auto-generated catch block
        	logger.LOGGER.log(Level.SEVERE, e.toString());
		} finally {
			try {
				rs.close();
				cs.close();
				connection.close();
				ctx.close();
				logger.LOGGER.info("returning connection to pool");
			} catch(SQLException e) {
	            
	            logger.LOGGER.log(Level.SEVERE, e.toString());
	            
	        } catch (NamingException e) {
				// TODO Auto-generated catch block
				
				logger.LOGGER.log(Level.SEVERE, e.toString());
			}
			
		}
        
		return jd;	
		
	}
	

       
}
