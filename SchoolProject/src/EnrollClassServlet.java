

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
 * Servlet implementation class EnrollClassServlet
 */
@WebServlet("/EnrollClassServlet")
public class EnrollClassServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger( EnrollClassServlet.class.getName() );

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EnrollClassServlet() {
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
		String classIDstr = request.getParameter("ClassDropdown");
		String fullName = request.getParameter("FullName");
		String className = null;
		
		
		int studentID = Integer.parseInt(studentIDstr);
		int classID = Integer.parseInt(classIDstr);
		int teacherID = 0;
		
		
		//System.out.println("student and class ID: " + studentID + "  " + classID);
		
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


		     stmt = conn.prepareStatement( "INSERT INTO StudentClassRel VALUES(?, ? )" );
	         stmt.setInt(1, studentID);
	         stmt.setInt(2, classID);
	         stmt.executeUpdate();
	         
	         stmt = conn.prepareStatement( "SELECT * FROM CLASS WHERE ClassID = ?" );
	         stmt.setInt(1, classID);
	         ResultSet rs = stmt.executeQuery();
	         rs.next();
	         className = rs.getString("ClassName");
	         teacherID = rs.getInt("TeacherID");
	         
	         stmt = conn.prepareStatement("INSERT INTO Message VALUES( NULL, ?, ?, ?, ?, ?, ?, CURDATE(), NOW() )");
	         stmt.setString(1, "Welcome to " + className + "!");
	         stmt.setInt(2, 1);
	         stmt.setInt(3, teacherID);
	         stmt.setInt(4, 0);
	         stmt.setInt(5, studentID);
	         stmt.setString(6, "Hi  " + fullName + " !\n You have enrolled in the class " + className + "!\n");
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
		String title = "Class Enrolled!";
		out.println( "<!DOCTYPE html> \n" +
					 "<html> \n" + 
					 "<head> \n" +
					 "<meta charset=\"ISO-8859-1\"> \n" +
					 "<title>" + title + "</title> \n" +
					 "</head> \n" +
					 "<body> \n" );
		
		
		out.println( "Class " + className + " enrolled in!" );
		out.println( "<br> \n" );
		//out.println( "<a href=\"http://localhost:8080/SchoolProject/TeacherHomeServlet\">Return to Teacher Home Page</a> \n");
		//out.println(" <A HREF=\"javascript:history.back()\">Go Back to Student Home Page</A> \n ");
		
		out.println("<form action=\"StudentHomeServlet\" method=\"post\" > \n");
		
		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
		
		out.println("<br><input type=\"submit\" value=\"Return to Student Home Page!\"> \n");
		out.println("</form> \n");
		
		out.println( "</body> \n" );
		out.println( "</html>\n" );
		
		
	}

}
