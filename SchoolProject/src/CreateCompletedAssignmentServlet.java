

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

/**
 * Servlet implementation class CreateCompletedAssignmentServlet
 */
@WebServlet("/CreateCompletedAssignmentServlet")
public class CreateCompletedAssignmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateCompletedAssignmentServlet() {
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
		
		String studentIDstr = request.getParameter("StudentID");
		String assignmentIDstr = request.getParameter("AssignmentID");
		int studentID = Integer.parseInt(studentIDstr);
		int assignmentID = Integer.parseInt(assignmentIDstr);
		
		String assignmentName = request.getParameter("AssignmentName");
		String answer1 = request.getParameter("Answer1");
		String answer2 = request.getParameter("Answer2");
		String answer3 = request.getParameter("Answer3");
		String answer4 = request.getParameter("Answer4");
		String answer5 = request.getParameter("Answer5");
		
		String completed = request.getParameter("completed");
		
		final String JDBC_DRIVER="com.mysql.jdbc.Driver";  
		final String DB_URL="jdbc:mysql://localhost:3306/SchoolProject";

		//  Database credentials
		final String USER = "cleung";
		final String PASS = "cleung";
		
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    
	    try{
	         // Register JDBC driver
	         Class.forName("com.mysql.jdbc.Driver");
	    
	         // Open a connection
	         conn = DriverManager.getConnection(DB_URL,USER,PASS);

	         if( completed.equals("0")){
			     stmt = conn.prepareStatement( "INSERT INTO CompletedAssignment VALUES(NULL, ?, ?, ?, ?, ?, ?, ? )" );
		         stmt.setInt(1, studentID);
		         stmt.setInt(2, assignmentID);
		         stmt.setString(3,  answer1 );
		         stmt.setString(4,  answer2 );
		         stmt.setString(5,  answer3 );
		         stmt.setString(6,  answer4 );
		         stmt.setString(7,  answer5 );
		         stmt.executeUpdate();
	         }
	         else{
			     stmt = conn.prepareStatement( "UPDATE CompletedAssignment SET Answer1 = ?, Answer2 = ?, Answer3 = ?, Answer4 = ?, Answer5 = ? WHERE studentID = ? AND assignmentID = ?" );

		         stmt.setString(1,  answer1 );
		         stmt.setString(2,  answer2 );
		         stmt.setString(3,  answer3 );
		         stmt.setString(4,  answer4 );
		         stmt.setString(5,  answer5 );
		         stmt.setInt(6, studentID);
		         stmt.setInt(7, assignmentID);
		         stmt.executeUpdate();
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
		String title = "Class Created!";
		out.println( "<!DOCTYPE html> \n" +
					 "<html> \n" + 
					 "<head> \n" +
					 "<meta charset=\"ISO-8859-1\"> \n" +
					 "<title>" + title + "</title> \n" +
					 "</head> \n" +
					 "<body> \n" );
	    
		out.println( "Assignment " + assignmentName + " commpleted! <br> \n" );

		out.println("<form action=\"StudentHomeServlet\" method=\"post\" > \n");
		
		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
		
		out.println("<br><input type=\"submit\" value=\"Return to Student Home Page!\"> \n");
		out.println("</form> \n");
		
		out.println( "</body> \n" );
		out.println( "</html>\n" );
	}

}
