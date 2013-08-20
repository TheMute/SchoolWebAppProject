

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CreateClassServlet
 */
@WebServlet("/CreateClassServlet")
public class CreateClassServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateClassServlet() {
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
		
		String className = request.getParameter("ClassName");
		String subjectID = request.getParameter("SubjectsDropdown");
		String teacherID = request.getParameter("TeacherID");
		
		System.out.println(className + " " + subjectID + " " + teacherID);
		
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


		     stmt = conn.prepareStatement( "INSERT INTO CLASS VALUES(NULL, ?, ?, ? )" );
	     
	         stmt.setString(1, className);
	         stmt.setString(2, subjectID);
	         stmt.setString(3, teacherID);
	          
	         stmt.executeUpdate();
	         
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
		
		
		out.println( "Class " + className + " created!" );
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
		
	}

}
