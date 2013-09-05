

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class CreateCompletedAssignmentServlet
 */
@WebServlet("/CreateCompletedAssignmentServlet")
public class CreateCompletedAssignmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger( CreateCompletedAssignmentServlet.class.getName() );

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
		
		int teacherID = 0;
		String className = null;
		String studentName = null;
		
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
	    ResultSet rs = null;
		RequestDispatcher disp = null;
	    try{
	         // Register JDBC driver
	         Class.forName("com.mysql.jdbc.Driver");
	    
	         // Open a connection
	         conn = DriverManager.getConnection(DB_URL,USER,PASS);

	         if( completed.equals("0")){
			     stmt = conn.prepareStatement( "INSERT INTO CompletedAssignment VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, CURDATE() )" );
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
			     stmt = conn.prepareStatement( "UPDATE CompletedAssignment SET Answer1 = ?, Answer2 = ?, Answer3 = ?, Answer4 = ?, Answer5 = ?, DueDate = CURDATE() WHERE studentID = ? AND assignmentID = ?" );

		         stmt.setString(1,  answer1 );
		         stmt.setString(2,  answer2 );
		         stmt.setString(3,  answer3 );
		         stmt.setString(4,  answer4 );
		         stmt.setString(5,  answer5 );
		         stmt.setInt(6, studentID);
		         stmt.setInt(7, assignmentID);
		         stmt.executeUpdate();
	         }
	         
	         stmt = conn.prepareStatement("SELECT * FROM CLASS A JOIN Assignment B ON A.ClassID = B.ClassID WHERE B.AssignmentID=?");
	         stmt.setInt(1, assignmentID);
	         rs = stmt.executeQuery();
	         while(rs.next()){
	        	 teacherID = rs.getInt("TeacherID");
	        	 className = rs.getString("ClassName");
	         }
	         
	         stmt = conn.prepareStatement("INSERT INTO Message VALUES( NULL, ?, ?, ?, ?, ?, ?, CURDATE(), NOW() )");
	         stmt.setString(1, "Assignment '" + assignmentName + "' completed for " + className + "!");
	         stmt.setInt(2, 0);
	         stmt.setInt(3, studentID);
	         stmt.setInt(4, 1);
	         stmt.setInt(5, teacherID);
	         stmt.setString(6, "Hello teacher!\n Your student has completed the assignment '" + assignmentName + "' for the class " + className + "!\n");
	         stmt.executeUpdate();
	         
	         stmt = conn.prepareStatement("SELECT * FROM STUDENT WHERE StudentID=?");
	         stmt.setInt(1, studentID);
	         rs = stmt.executeQuery();
	         while( rs.next()){
	        	 studentName = rs.getString("FirstName") + " " + rs.getString("LastName");
	         }
	         
	         String description = "Assignment '" + assignmentName + "' completed!";
	         String notes = "Assignment '" + assignmentName + "' completed for the class " + className + "!";
	         stmt = conn.prepareStatement("INSERT INTO EVENTS VALUES(NULL, NOW(), NOW(), ?, ?, ?, ?)");
	         stmt.setString(1, description);
	         stmt.setString(2, notes);
	         stmt.setString(3, "S"+studentID);
	         stmt.setInt(4, 1);
	         stmt.executeUpdate();
	         
	         description = "Assignment '" + assignmentName + "' completed by " + studentName + "!";
	         notes = "Assignment '" + assignmentName + "' completed for the class " + className + " by your student " + studentName + "!";
	         stmt = conn.prepareStatement("INSERT INTO EVENTS VALUES(NULL, NOW(), NOW(), ?, ?, ?, ?)");
	         stmt.setString(1, description);
	         stmt.setString(2, notes);
	         stmt.setString(3, "T"+teacherID);
	         stmt.setInt(4, 1);
	         stmt.executeUpdate();
	    
	         
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
		String title = "Completed Assignment Created!";
		out.println( "<!DOCTYPE html> \n" +
					 "<html> \n" + 
					 "<head> \n" +
					 "<meta charset=\"ISO-8859-1\"> \n" +
					 "<title>" + title + "</title> \n" +
					 "</head> \n" +
					 "<body> \n" );
	    
		out.println( "Assignment \"" + assignmentName + "\" commpleted! <br> \n" );

		out.println("<form action=\"StudentHomeServlet\" method=\"post\" > \n");
		
		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
		
		out.println("<br><input type=\"submit\" value=\"Return to Student Home Page!\"> \n");
		out.println("</form> \n");
		
		out.println( "</body> \n" );
		out.println( "</html>\n" );
	}

}
