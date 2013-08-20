

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CreateUser
 */
@WebServlet("/CreateUser")
public class CreateUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static int studentID = 0;
	private static int teacherID = 0;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateUserServlet() {
        super();
        
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

		String firstName = request.getParameter("FirstName").toString();
		String lastName = request.getParameter("LastName").toString();
		String email = request.getParameter("Email").toString();
		String password = request.getParameter("Password").toString();
		
		boolean student;
		if( request.getParameter("user").equals("Student"))
			student = true;
		else
			student = false;
		
		/*
		String student;
		if( request.getParameter("Student") == null )
			student = "off";
		else
			student = request.getParameter("Student").toString();  //on or null
		String teacher;
		if( request.getParameter("Teacher") == null )
			teacher = "off";
		else
			teacher = request.getParameter("Teacher").toString();  //on or null
		*/
		
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
	         //net.sourceforge.jtds.jdbc.Driver
	         //com.microsoft.sqlserver.jdbc.SQLServerDriver

	         // Open a connection
	         conn = DriverManager.getConnection(DB_URL,USER,PASS);

	         // Execute SQL query
	         //stmt = conn.createStatement();
	         /*
	         String sql;
	         if( student.equals("on") ){
	        	 
	        	 sql = "INSERT INTO STUDENT VALUES(?, ?, ?, ?, ? )";
	        	 
	        	 
	        	 sql = "INSERT INTO STUDENT VALUES( " + studentID++ + "," + 
	        			 								"'" + firstName + "'," +
	        			 								"'" + lastName + "'," +
									        			 "'" + email + "'," +
									        			 "'" + password + "' )";
									        			 
	         }
	         else{
	        	 sql = "INSERT INTO STUDENT VALUES( " + teacherID++ + "," + 
														"'" + firstName + "'," +
														"'" + lastName + "'," +
														"'" + email + "'," +
														"'" + password + "' )";
	         }
	         */
	         
	         if( student ){
		         stmt = conn.prepareStatement( "INSERT INTO STUDENT (StudentID, FirstName, LastName, Email, Password) VALUES(NULL, ?, ?, ?, ? )" );
	         }
	         else{
		         stmt = conn.prepareStatement( "INSERT INTO TEACHER (TeacherID, FirstName, LastName, Email, Password) VALUES(NULL, ?, ?, ?, ? )" );
	         }

	         stmt.setString(1, firstName);
	         stmt.setString(2, lastName);
	         stmt.setString(3, email);
	         stmt.setString(4, password);
	          

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
	    	  System.out.println(" In finally");
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
		
	    String userType;
	    if( student ){
	    	userType = "Student";
	    }
	    else{
	    	userType = "Teacher";
	    }
	    
		response.setContentType("text/html");  
		PrintWriter out = response.getWriter();
		String title = "User Created!";
		out.println( "<!DOCTYPE html> \n" +
					 "<html> \n" + 
					 "<head> \n" +
					 "<meta charset=\"ISO-8859-1\"> \n" +
					 "<title>" + title + "</title> \n" +
					 "</head> \n" +
					 "<body> \n" );
		
		
		out.println( userType + " user created! \n");
		out.println( "<br> \n" );
		out.println( "<a href=\"http://localhost:8080/SchoolProject/\">Return to Homepage</a> \n");
		
		out.println( "</body> \n" );
		out.println( "</html>\n" );
		/*
		try  
		{  
			BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\yc05cl1\\workspace\\SchoolProject\\WebContent\\createuser.jsp"));  
			String str;  
			while ((str = in.readLine()) != null) {  
				out.println(str);  
			}  
			in.close();  
		}  
		catch(Exception e)  
		{  
			System.out.println( "cannot get reader: " + e );  
		}  
		*/
		

		
					
		
	}

}
