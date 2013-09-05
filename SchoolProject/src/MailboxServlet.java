

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class MailboxServlet
 */
@WebServlet("/MailboxServlet")
public class MailboxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger( MailboxServlet.class.getName() );

    /**
     * @see HttpServlet#HttpServlet()
     */
    public MailboxServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getParameter("UserName");
		
		
		String studentIDstr = null, teacherIDstr = null;
		int studentID = 0, teacherID = 0, userType = 0;
		if( request.getParameter("StudentID") != null){
			studentIDstr = request.getParameter("StudentID");
			studentID = Integer.parseInt(studentIDstr);
			userType = 0;
		}
		else if( request.getParameter("TeacherID") != null){
			teacherIDstr = request.getParameter("TeacherID");
			teacherID = Integer.parseInt(teacherIDstr);
			userType = 1;
		}
		
		
		Vector<String> names = new Vector<String>();
		Vector<Integer> userIDs = new Vector<Integer>();
		Vector<Integer> userTypes = new Vector<Integer>();
		
		
		
		// JDBC driver name and database URL
		final String JDBC_DRIVER="com.mysql.jdbc.Driver";  
		final String DB_URL="jdbc:mysql://localhost:3306/SchoolProject";
		
		//  Database credentials
		final String USER = "cleung";
		final String PASS = "cleung";
		  
		Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
		RequestDispatcher disp = null;
		
	    try{
	         // Register JDBC driver
	         Class.forName("com.mysql.jdbc.Driver");
	         // Open a connection
	         conn = DriverManager.getConnection(DB_URL,USER,PASS);
	         
	         if( userType == 0 ){
			     stmt = conn.prepareStatement( "SELECT * FROM STUDENT WHERE StudentID != ?" );
			     stmt.setInt( 1, studentID);
	         }
	         else{
			     stmt = conn.prepareStatement( "SELECT * FROM STUDENT" );
	         }
	         rs = stmt.executeQuery();
	         
	         while( rs.next() ){
	        	 names.add( rs.getString("FirstName") + " " + rs.getString("LastName"));
	        	 userIDs.add( rs.getInt("StudentID"));
	        	 userTypes.add( 0 );
	         }
	         
	         if( userType == 0 ){
			     stmt = conn.prepareStatement( "SELECT * FROM TEACHER" );
	         }
	         else{
			     stmt = conn.prepareStatement( "SELECT * FROM TEACHER WHERE TeacherID != ?" );
			     stmt.setInt( 1, teacherID);

	         }
	         rs = stmt.executeQuery();
	         
	         while( rs.next() ){
	        	 names.add( rs.getString("FirstName") + " " + rs.getString("LastName"));
	        	 userIDs.add( rs.getInt("TeacherID"));
	        	 userTypes.add( 1 );
	         }
	         
	    }catch(SQLException se){
	         //Handle errors for JDBC
	    	  System.out.println(" SQLException occurred! ");
	    	  logger.error( "SQL Exception ocurred", se);
	         se.printStackTrace();
	         request.setAttribute("SQLException", se);
	         disp = request.getRequestDispatcher("/SQLExceptionPageServlet"); 
	         disp.forward(request, response);  
	    }catch(Exception e){
	         //Handle errors for Class.forName
	    	  System.out.println("Exception occurred! ");
	    	  logger.error( "Exception ocurred", e);
	         e.printStackTrace();
	         request.setAttribute("Exception", e);
	         disp = request.getRequestDispatcher("/ExceptionPageServlet"); 
	         disp.forward(request, response);  	    
	    }finally{
	         //finally block used to close resources
			 try{
			    if(stmt!=null)
			       stmt.close();
			 }catch(SQLException se2){
				  System.out.println(" SQLException2 occurred! ");
		    	  logger.error( "SQL Exception ocurred", se2);
		    	  se2.printStackTrace();
		         request.setAttribute("SQLException", se2);
		         disp = request.getRequestDispatcher("/SQLExceptionPageServlet"); 
		         disp.forward(request, response);  			 }// nothing we can do
			 try{
			    if(conn!=null)
			    conn.close();
			 }catch(SQLException se){
				 System.out.println(" SQLException4 occurred! ");
		    	 logger.error( "SQL Exception ocurred", se);
				 se.printStackTrace();
		         request.setAttribute("SQLException", se);
		         disp = request.getRequestDispatcher("/SQLExceptionPageServlet"); 
		         disp.forward(request, response);  
			 }//end finally try
	    } //end try
		
		response.setContentType("text/html");  
		PrintWriter out = response.getWriter();
		String title = "Your Mailbox!";
		out.println( "<!DOCTYPE html> \n" +
					 "<html> \n" + 
					 "<head> \n" +
					 "<meta charset=\"ISO-8859-1\"> \n" +
					 "<title>" + title + "</title> \n" );

	      out.println( "<script type = \"text/javascript\"> \n" );

		
	      out.println( "function checkFormCreateMessage(form) " );
	      out.println( "{ \n");
	      
	      out.println( " if( form.MessageName.value == \"\" ){ \n ");
	      out.println( "alert(\"Error: Message Name cannot be blank!\" ); \n");
	      out.println( "form.MessageName.focus() \n" );
	      out.println( "return false; \n");
	      out.println( "} \n" );
	      
	      out.println( " if( form.Message.value == \"\" ){ \n ");
	      out.println( "alert(\"Error: Message cannot be blank!\" ); \n");
	      out.println( "form.Message.focus() \n" );
	      out.println( "return false; \n");
	      out.println( "} \n" );
	      
	      out.println( "return true; \n");
	      out.println( "} \n" );
	      
	      
		
	      out.println("</script> \n");

		out.println("</head> \n" +
				 "<body> \n" );
		
		
		out.println( "<h3>" + userName +"'s Mailbox! </h4> \n" );
		
		out.println("<form action=\"InboxServlet\" method=\"post\" > \n");
		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"UserType\" value=\"" + userType  + "\"> \n");
		if( userType == 0 ){
			out.println("<input type=\"hidden\" name=\"UserID\" value=\"" + studentID  + "\"> \n");
		}
		else{
			out.println("<input type=\"hidden\" name=\"UserID\" value=\"" + teacherID  + "\"> \n");
		}
		
		out.println("<input type=\"hidden\" name=\"StudentID\" value=\"" + studentID  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"TeacherID\" value=\"" + teacherID  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"UserName\" value=\"" + userName  + "\"> \n");

	    out.println("<br><input type=\"submit\" value=\"View Your Inbox!\"> \n");
		out.println("</form> \n");
		
		out.println("<form action=\"OutboxServlet\" method=\"post\" > \n");
		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"UserType\" value=\"" + userType  + "\"> \n");
		if( userType == 0 ){
			out.println("<input type=\"hidden\" name=\"UserID\" value=\"" + studentID  + "\"> \n");
		}
		else{
			out.println("<input type=\"hidden\" name=\"UserID\" value=\"" + teacherID  + "\"> \n");
		}
		
		out.println("<input type=\"hidden\" name=\"StudentID\" value=\"" + studentID  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"TeacherID\" value=\"" + teacherID  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"UserName\" value=\"" + userName  + "\"> \n");

	    out.println("<br><input type=\"submit\" value=\"View Your Outbox!\"> \n");
		out.println("</form> \n");
		
		
		out.println("<h4> Send a Message! </h4> \n");
		
		out.println("<form action=\"CreateMessageServlet\" method=\"post\" onsubmit=\"return checkFormCreateMessage(this)\" > \n");
		
		out.println("<table> \n");
		
	    out.println( "<tr><td><font face=\"verdana\" size=\"2px\">Send Message To: </font></td> \n" );
		out.println("<td><select name=\"ReceiverDropdown\"> \n");
		int j = 0;
		for( String s : names){
			out.println("<option value=\"" + userTypes.get(j) + " " + userIDs.get(j) + "\">" + s + "</option> \n");

			j++;
		}
		out.println("</select></td></tr> \n");
		
		

		
	    out.println( "<tr><td><font face=\"verdana\" size=\"2px\">Message Name: </font></td> \n" );
	    out.println( "<td><input type=\"text\" name=\"MessageName\"></td></tr> \n" );
	    

		
		out.println("</table> \n");
		
		out.println( " <font face=\"verdana\" size=\"2px\">Message:</font> \n ");
	    out.println( "<br><textarea name = \"Message\" rows=\"4\" cols=\"75\"></textarea> \n ");
		
		out.println("<input type=\"hidden\" name=\"SenderUserType\" value=\"" + userType  + "\"> \n");
		if( userType == 0 ){
			out.println("<input type=\"hidden\" name=\"SenderUserID\" value=\"" + studentID  + "\"> \n");
		}
		else{
			out.println("<input type=\"hidden\" name=\"SenderUserID\" value=\"" + teacherID  + "\"> \n");
		}
		
		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
		
		out.println("<input type=\"hidden\" name=\"StudentID\" value=\"" + studentID  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"TeacherID\" value=\"" + teacherID  + "\"> \n");

		out.println("<input type=\"hidden\" name=\"UserName\" value=\"" + userName  + "\"> \n");

		
	    out.println("<br><input type=\"submit\" value=\"Send Your Message!\"> \n");
		out.println("</form> \n");
		
		if( userType == 0){
			out.println("<br><form action=\"StudentHomeServlet\" method=\"post\" > \n");
			
			out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
			
			out.println("<br><input type=\"submit\" value=\"Return to Student Home Page!\"> \n");
			out.println("</form> \n");	
		}
		else if( userType == 1 ){
			out.println("<br><form action=\"TeacherHomeServlet\" method=\"post\" > \n");
			
			out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
			
			out.println("<br><input type=\"submit\" value=\"Return to Teacher Home Page!\"> \n");
			out.println("</form> \n");	
		}

		out.println( "</body> \n" );
		out.println( "</html>\n" );	
		
	}

}
