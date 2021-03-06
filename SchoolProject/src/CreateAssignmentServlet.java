

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class CreateAssignmentServlet
 */
@WebServlet("/CreateAssignmentServlet")
public class CreateAssignmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger( CreateAssignmentServlet.class.getName() );

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateAssignmentServlet() {
        super();
        //adding some code to see if this commit works
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
		String [] classesDropdown = request.getParameter("ClassesDropdown").split(":");
		
		
		String classID = classesDropdown[0]; //request.getParameter("ClassesDropdown");
		int classIDint = Integer.parseInt(classID);
		String className = classesDropdown[1];
		String assignmentName = request.getParameter("AssignmentName");
		String dueDate = request.getParameter("DueDate");
		
		System.out.println("classesdropdown: " + classID + " " + className);
		
		String teacherIDstr = request.getParameter("TeacherID");
		int teacherID = Integer.parseInt(teacherIDstr);
		
		
		String question1 = request.getParameter("Question1");
		String question2 = request.getParameter("Question2");
		String question3 = request.getParameter("Question3");
		String question4 = request.getParameter("Question4");
		String question5 = request.getParameter("Question5");
		
		Vector<Integer> studentIDs = new Vector<Integer>();
		
		/*
		System.out.println("Question1: " + question1);
		System.out.println("Question2: " + question2);
		System.out.println("Question3: " + question3);
		System.out.println("Question4: " + question4);
		System.out.println("Question5: " + question5);
		*/
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

		     stmt = conn.prepareStatement( "INSERT INTO Assignment VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ? )" );
	         stmt.setString(1, assignmentName);
	         stmt.setInt(2, classIDint);
	         stmt.setString(3, question1);
	         stmt.setString(4, question2);
	         stmt.setString(5, question3);
	         stmt.setString(6, question4);
	         stmt.setString(7, question5);
	         stmt.setString(8, dueDate);
	         
	         stmt.executeUpdate();
	         
	         
	         stmt = conn.prepareStatement( "SELECT * FROM STUDENTCLASSREL WHERE ClassID = ?");
	         stmt.setInt(1, classIDint);
	         rs = stmt.executeQuery();
	         while(rs.next()){
	        	 studentIDs.add( rs.getInt("StudentID"));
	         }
	         
	         for( int i : studentIDs ){
		         stmt = conn.prepareStatement("INSERT INTO Message VALUES( NULL, ?, ?, ?, ?, ?, ?, CURDATE(), NOW() )");
		         stmt.setString(1, "Assignment created for " + className + "!");
		         stmt.setInt(2, 1);
		         stmt.setInt(3, teacherID);
		         stmt.setInt(4, 0);
		         stmt.setInt(5, i);
		         stmt.setString(6, "Hello student!\n A new assignment called '" + assignmentName + "' has been created for the class " + className + "!\n  It is due on " + dueDate + "! \n");
		         stmt.executeUpdate();
	         }
	         
	         String description = "Assignment '" + assignmentName + "' due!";
	         String notes = "Assignment '" + assignmentName + "' due for the class " + className + "!";
	         stmt = conn.prepareStatement("INSERT INTO EVENTS VALUES(NULL, ?, ?, ?, ?, ?, ?)");
	         stmt.setString(1, dueDate);
	         stmt.setString(2, dueDate);
	         
	         stmt.setString(3, description);
	         stmt.setString(4, notes);
	         stmt.setString(5, "T"+teacherID);
	         stmt.setInt(6, 1);
	         stmt.executeUpdate();
	         
	         for( int i : studentIDs){
	        	 stmt = conn.prepareStatement("INSERT INTO EVENTS VALUES(NULL, ?, ?, ?, ?, ?, ?)");
		         stmt.setString(1, dueDate);
		         stmt.setString(2, dueDate);
		         
		         stmt.setString(3, description);
		         stmt.setString(4, notes);
		         stmt.setString(5, "S"+i);
		         stmt.setInt(6, 1);
		         stmt.executeUpdate();
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
		String title = "Assignment Created!";
		out.println( "<!DOCTYPE html> \n" +
					 "<html> \n" + 
					 "<head> \n" +
					 "<meta charset=\"ISO-8859-1\"> \n" +
					 "<title>" + title + "</title> \n" +
					 "</head> \n" +
					 "<body> \n" );
		
		
		out.println( "Assignment \"" + assignmentName + "\" created!" );
		out.println( "<br> \n" );
		//out.println( "<a href=\"http://localhost:8080/SchoolProject/TeacherHomeServlet\">Return to Teacher Home Page</a> \n");
		//out.println(" <A HREF=\"javascript:history.back()\">Go Back</A> \n ");
		
		out.println("<form action=\"TeacherHomeServlet\" method=\"post\" > \n");
		
		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"user\" value=\"" + request.getParameter("user")  + "\"> \n");
		
		out.println("<br><input type=\"submit\" value=\"Return to Teacher Home Page!\"> \n");
		out.println("</form> \n");
		
		out.println( "</body> \n" );
		out.println( "</html>\n" );
		
		out.close();
		
		
	}

}
