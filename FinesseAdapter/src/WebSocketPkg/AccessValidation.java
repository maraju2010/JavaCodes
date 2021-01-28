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



/**
 * Servlet implementation class AccessValidation
 *
 * The AccessValidation provides external API interface for user. The first step in setting up connection is 
 * to call this api to authenticate the user.
 * 
 * @author Manoj Raju
 */

@WebServlet("/AccessValidation")
public class AccessValidation extends HttpServlet {
	// Purpose :Class acts as an  API Interface to users  for authentication & authorization.
	
	private static final long serialVersionUID = 1L;
	Authentication authenticator = Authentication.getInstance();
	CustomLogger logger = CustomLogger.getInstance();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AccessValidation() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// not implemented
		throw new ServletException("GET method is not supported.");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// users invoke post method and pass on username and password. Users are returned with Token for websocket connection.
		String AgentId = request.getParameter("AgentId");
		//String userName = request.getParameter("Username");
	    //String password = request.getParameter("Password");
		String auth = request.getHeader("Authorization");
		System.out.println(auth);
		
		try {
			  if(authenticator.checkInput(AgentId)) {
					if (authenticator.verifyCredentials(AgentId, auth)) {
					        String token = authenticator.generateAccessToken(AgentId);
					        response.setStatus(HttpServletResponse.SC_OK);
					        response.setContentType("application/json");
					        response.getWriter().write(token);
					        
					    } else {
					    	response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					    	response.setContentType("text/plain");
					    	response.getWriter().write("Invalid credentials");
					    }
			  } else {
				  response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			    	response.setContentType("text/plain");
			    	response.getWriter().write("Input not valid");
				  
			  }		
					
		} catch (SQLException | IOException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			logger.LOGGER.log(Level.SEVERE, e.toString());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    	response.setContentType("text/plain");
	    	response.getWriter().write("Exception while processing request");
		}
	}

}
