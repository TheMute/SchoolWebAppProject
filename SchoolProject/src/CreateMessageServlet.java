

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class CreateMessageServlet
 */
@WebServlet("/CreateMessageServlet")
public class CreateMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger( CreateMessageServlet.class.getName() );

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateMessageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost( request, response );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String messageName = request.getParameter("MessageName");
		String senderUserTypeSTR = request.getParameter("SenderUserType");
		String senderUserIDSTR = request.getParameter("SenderUserID");
		
		String [] receiver = request.getParameter("ReceiverDropdown").split(" ");
		String receiverUserTypeSTR = receiver[0];
		String receiverUserIDSTR = receiver[1];
		
		String userName = request.getParameter("UserName");

		
		String message = request.getParameter("Message");
		
		int senderUserType = Integer.parseInt(senderUserTypeSTR);
		int senderUserID = Integer.parseInt(senderUserIDSTR);
		int receiverUserType = Integer.parseInt(receiverUserTypeSTR);
		int receiverUserID = Integer.parseInt(receiverUserIDSTR);
		
		// JDBC driver name and database URL
		final String JDBC_DRIVER="com.mysql.jdbc.Driver";  
		final String DB_URL="jdbc:mysql://localhost:3306/SchoolProject";
		
		//  Database credentials
		final String USER = "cleung";
		final String PASS = "cleung";
		  
		Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    try{
	         // Register JDBC driver
	         Class.forName("com.mysql.jdbc.Driver");
	         // Open a connection
	         conn = DriverManager.getConnection(DB_URL,USER,PASS);
	         
	         stmt = conn.prepareStatement("INSERT INTO Message VALUES( NULL, ?, ?, ?, ?, ?, ?, CURDATE(), NOW() )");
	         
	         stmt.setString(1, messageName);
	         stmt.setInt(2, senderUserType);
	         stmt.setInt(3, senderUserID);
	         stmt.setInt(4, receiverUserType);
	         stmt.setInt(5, receiverUserID);
	         stmt.setString(6, message);
	         
	         
	         stmt.executeUpdate();
	         
	    }catch(SQLException se){
	         //Handle errors for JDBC
	    	  System.out.println(" SQLException occurred! ");
	    	  logger.error( "SQL Exception ocurred", se);
	         se.printStackTrace();
	    }catch(Exception e){
	         //Handle errors for Class.forName
	    	  System.out.println(" Exception occurred! ");
	    	  logger.error( "Exception ocurred", e);
	         e.printStackTrace();
	    }finally{
	         //finally block used to close resources
			 try{
			    if(stmt!=null)
			       stmt.close();
			 }catch(SQLException se2){
				  System.out.println(" SQLException2 occurred! ");
		    	  logger.error( "SQL Exception ocurred", se2);
			 }// nothing we can do
			 try{
			    if(conn!=null)
			    conn.close();
			 }catch(SQLException se){
				 System.out.println(" SQLException4 occurred! ");
		    	 logger.error( "SQL Exception ocurred", se);
				 se.printStackTrace();
			 }//end finally try
	    } //end try
	    
	    response.setContentType("text/html");  
		PrintWriter out = response.getWriter();
		String title = "Message Sent!";
		out.println( "<!DOCTYPE html> \n" +
					 "<html> \n" + 
					 "<head> \n" +
					 "<meta charset=\"ISO-8859-1\"> \n" +
					 "<title>" + title + "</title> \n" +
					 "</head> \n" +
					 "<body> \n" );
		
		
		out.println( "Message \"" + messageName + "\" sent!" );
		out.println( "<br> \n" );
		//out.println( "<a href=\"http://localhost:8080/SchoolProject/TeacherHomeServlet\">Return to Teacher Home Page</a> \n");
		//out.println(" <A HREF=\"javascript:history.back()\">Go Back</A> \n ");
		
		out.println("<form action=\"MailboxServlet\" method=\"post\" > \n");
		
		if( senderUserType == 0 ){
			out.println("<input type=\"hidden\" name=\"StudentID\" value=\"" + request.getParameter("SenderUserID")  + "\"> \n");
		}
		else{
			out.println("<input type=\"hidden\" name=\"TeacherID\" value=\"" + request.getParameter("SenderUserID")  + "\"> \n");

		}
		out.println("<input type=\"hidden\" name=\"UserName\" value=\"" + userName  + "\"> \n");
		
		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");

		out.println("<br><input type=\"submit\" value=\"Return to Mailbox!\"> \n");
		out.println("</form> \n");
		
		
		out.println("<form action=\"StudentHomeServlet\" method=\"post\" > \n");
		
		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
		
		out.println("<br><input type=\"submit\" value=\"Return to Student Home Page!\"> \n");
		out.println("</form> \n");
		
		out.println( "</body> \n" );
		out.println( "</html>\n" );
		
	}

}
