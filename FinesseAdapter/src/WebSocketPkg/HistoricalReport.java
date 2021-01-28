package WebSocketPkg;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class HistoricalReport
 * 
 * 
 * Historical Report class
 *
 * This class provides API for external users to invoke historical reports. It takes Agent Id, username, password , 
 * start time and end time as inputs.
 * 
 * @author Manoj Raju
 *
 */

@WebServlet("/HistoricalReport")
public class HistoricalReport extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Authentication authenticator = Authentication.getInstance();
	CustomLogger logger = CustomLogger.getInstance();
	HistoricalPushService historicalservice = HistoricalPushService.getInstance();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HistoricalReport() {
        super();
        // TODO Auto-generated constructor stub

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String AgentId = request.getParameter("AgentId");
		//String userName = request.getParameter("Username");
	    //String password = request.getParameter("Password");
	    String starttime = request.getParameter("StartTime");
	    String endtime = request.getParameter("EndTime");
	    
	    String auth = request.getHeader("Authorization");
		System.out.println(auth);
		
		try {
			if(authenticator.checkInput(AgentId)) {
				 if (authenticator.checkInput(starttime, endtime)) {	
						if (authenticator.verifyCredentials(AgentId, auth)) {
							JSONObject ReportObject = historicalservice.getHistoricalReport(AgentId,starttime,endtime);
							response.setStatus(HttpServletResponse.SC_OK);
							response.setContentType("application/json");
							response.getWriter().write(ReportObject.toString());
					
						} else {
					    	response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					    	response.setContentType("text/plain");
					    	response.getWriter().write("Invalid credentials");
					    }
						
				 } else { 
					 response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				    	response.setContentType("text/plain");
				    	response.getWriter().write("Input not in valid format");
				 }
				 
			} else {
				  response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			    	response.setContentType("text/plain");
			    	response.getWriter().write("Input not valid");
				  
			  }	
		} catch (SQLException | IOException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			logger.LOGGER.log(Level.SEVERE, e.toString());
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	    	response.setContentType("text/plain");
	    	response.getWriter().write("Exception while validating credentials");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			logger.LOGGER.log(Level.SEVERE, e.toString());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		throw new ServletException("POST method is not supported.");
	}

}
