

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class OutboxServlet
 */
@WebServlet("/OutboxServlet")
public class OutboxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OutboxServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost( request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String userTypeSTR = request.getParameter("UserType");
		String userIDSTR = request.getParameter("UserID");
		String userName = request.getParameter("UserName");

		int userType = Integer.parseInt(userTypeSTR);
		int userID = Integer.parseInt( userIDSTR );
		
		Vector<Integer> receiverUserTypes = new Vector<Integer>();
		Vector<Integer> receiverUserIDs = new Vector<Integer>();
		Vector<String> messageNames = new Vector<String>();
		Vector<String> messages = new Vector<String>();
		Vector<Date> dates = new Vector<Date>();
		Vector<String> names = new Vector<String>();
		
		Vector<Timestamp> times = new Vector<Timestamp>();

		
		
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
	         
	         stmt = conn.prepareStatement( "SELECT * FROM Message WHERE SenderUserType=? AND SenderUserID=? ORDER BY DateTimeCreated DESC");
	         stmt.setInt(1, userType);
	         stmt.setInt(2, userID);
	         rs = stmt.executeQuery();
	         
	         while(rs.next()){
	        	 receiverUserTypes.add( rs.getInt("ReceiverUserType"));
	        	 receiverUserIDs.add( rs.getInt("ReceiverUserID"));
	        	 messageNames.add( rs.getString("MessageName"));
	        	 messages.add( rs.getString("Message"));
	        	 dates.add( rs.getDate("DateCreated"));
	        	 times.add( rs.getTimestamp("DateTimeCreated"));
	        	 
	         }
	         
	         int j = 0;
	         for( int i : receiverUserTypes ){
	        	 if( i == 0 ){
	    	         stmt = conn.prepareStatement( "SELECT * FROM Student WHERE StudentID = ?");
	    	         stmt.setInt(1, receiverUserIDs.get(j));
	    	         rs = stmt.executeQuery();
	    	         
	    	         while( rs.next() ){
	    	        	 names.add( rs.getString("FirstName") + " " + rs.getString("LastName"));
	    	         }
	    	         
	        	 }
	        	 else if( i == 1){
	    	         stmt = conn.prepareStatement( "SELECT * FROM Teacher WHERE TeacherID = ?");
	    	         stmt.setInt(1, receiverUserIDs.get(j));
	    	         rs = stmt.executeQuery();
	    	         
	    	         while( rs.next() ){
	    	        	 names.add( rs.getString("FirstName") + " " + rs.getString("LastName"));
	    	         }
	        	 }
	        	 j++;
	        	 
	         }
	         
	    }catch(SQLException se){
	         //Handle errors for JDBC
	    	  System.out.println(" SQLException occurred! ");
	         se.printStackTrace();
	    }catch(Exception e){
	    	//Handle errors for Class.forName
	    	System.out.println(" Exception occurred! ");
	         e.printStackTrace();
	    }finally{
	         //finally block used to close resources
			 try{
			    if(stmt!=null)
			       stmt.close();
			 }catch(SQLException se2){
				  System.out.println(" SQLException2 occurred! ");
			 }// nothing we can do
			 try{
			    if(conn!=null)
			    conn.close();
			 }catch(SQLException se){
				 System.out.println(" SQLException4 occurred! ");
				 se.printStackTrace();
			 }//end finally try
	    } //end try
		
	    response.setContentType("text/html");  
		PrintWriter out = response.getWriter();
		String title = "Your Outbox!";
		out.println( "<!DOCTYPE html> \n" +
					 "<html> \n" + 
					 "<head> \n" +
					 "<meta charset=\"ISO-8859-1\"> \n" +
					 "<title>" + title + "</title> \n" +
					 "</head> \n" +
					 "<body> \n" );
		
		
		out.println( "<h3> Your Outbox! </h3> \n" );
		
		out.println("<table width=\"75%\" border=\"1\"> \n");
		out.println("<tr> \n");
		out.println("<td> Date Sent </td> \n");
		out.println("<td> Sent To </td> \n");
		out.println("<td> Message Name </td> \n");
		out.println("<td> View Message </td> \n");
		out.println("</tr> \n");
		
		int i = 0;
		for( String s : messages){
			
			out.println("<tr> \n");
			out.println("<td> " + times.get(i) + " </td> \n");
			out.println("<td> " + names.get(i) + " </td> \n");
			out.println("<td> " + messageNames.get(i) + " </td> \n");
			out.println("<td> <form action=\"ViewMessageServlet\" method=\"post\" > \n");
			
			out.println("<input type=\"submit\" value=\"View Message!\"> \n");
			
			out.println("<input type=\"hidden\" name=\"DateSent\" value=\"" + times.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"NameTo\" value=\"" + names.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"NameFrom\" value=\"" + userName  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"MessageName\" value=\"" + messageNames.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Message\" value=\"" + messages.get(i)  + "\"> \n");
			

			out.println("<input type=\"hidden\" name=\"StudentID\" value=\"" + request.getParameter("StudentID")  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"TeacherID\" value=\"" + request.getParameter("TeacherID")  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
			
			out.println("<input type=\"hidden\" name=\"SenderUserType\" value=\"" + request.getParameter("UserType")  + "\"> \n");
			if( userType == 0 ){
				out.println("<input type=\"hidden\" name=\"SenderUserID\" value=\"" + request.getParameter("StudentID")  + "\"> \n");
			}
			else{
				out.println("<input type=\"hidden\" name=\"SenderUserID\" value=\"" + request.getParameter("TeacherID")  + "\"> \n");
			}
			
			out.println("<input type=\"hidden\" name=\"UserType\" value=\"" + userType  + "\"> \n");

			
			out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
			
			out.println("<input type=\"hidden\" name=\"StudentID\" value=\"" + request.getParameter("StudentID")  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"TeacherID\" value=\"" + request.getParameter("TeacherID")  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"UserName\" value=\"" + userName  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"UserID\" value=\"" + request.getParameter("UserID")  + "\"> \n");


			
			out.println("</form></td> \n");
			
			out.println("</tr> \n");
			
			
			i++;
		}
		out.println("</table> \n");
		
		
		out.println("<form action=\"MailboxServlet\" method=\"post\" > \n");
		
		if( userType == 0 ){
			out.println("<input type=\"hidden\" name=\"StudentID\" value=\"" + request.getParameter("UserID")  + "\"> \n");
		}
		else{
			out.println("<input type=\"hidden\" name=\"TeacherID\" value=\"" + request.getParameter("UserID")  + "\"> \n");

		}
		out.println("<input type=\"hidden\" name=\"UserName\" value=\"" + userName  + "\"> \n");
		
		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");

		out.println("<br><input type=\"submit\" value=\"Return to Mailbox!\"> \n");
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
