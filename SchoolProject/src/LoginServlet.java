

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
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

 
        String email = request.getParameter("Email");
        String password = request.getParameter("Password");
        boolean student;
        if( request.getParameter("user").equals("Student") )
        	student = true;
        else
        	student = false;
        
        String radio = request.getParameter("user");
        System.out.println(radio);
        
        String yolo = request.getParameter("yolo");
        System.out.println("IS YOLO HERE: " + yolo);
        
		// JDBC driver name and database URL
		final String JDBC_DRIVER="com.mysql.jdbc.Driver";  
		final String DB_URL="jdbc:mysql://localhost:3306/SchoolProject";
		
		//  Database credentials
		final String USER = "cleung";
		final String PASS = "cleung";
		  
		Connection conn = null;
		PreparedStatement stmt = null;
		String queryEmail = null;
		String queryPassword = null;
	      
		try{
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			//net.sourceforge.jtds.jdbc.Driver
			//com.microsoft.sqlserver.jdbc.SQLServerDriver
			// Open a connection
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			
			// Execute SQL query
			if( student )
				stmt = conn.prepareStatement( "SELECT Email, Password FROM Student WHERE Email = ? AND Password = ?" );
			else
				stmt = conn.prepareStatement( "SELECT Email, Password FROM Teacher WHERE Email = ? AND Password = ?" );

	        stmt.setString(1, email);
	        stmt.setString(2, password);
	        ResultSet rs = stmt.executeQuery();
	        
	        while( rs.next()){
		        queryEmail = rs.getString("Email");
		        queryPassword = rs.getString("Password");
		        
		        System.out.println(queryEmail);
		        System.out.println(queryPassword);
		        break;
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
		
		
		response.setContentType("text/html");  
		PrintWriter out = response.getWriter();
		String title = "Login";
		if( email.equals( queryEmail )&& password.equals(queryPassword) ){ 
			
			
			RequestDispatcher disp;
			
			if( student ){
				disp = request.getRequestDispatcher("/StudentHomeServlet");  
			}
			else{
				disp = request.getRequestDispatcher("/TeacherHomeServlet");  

			}
				
				
			disp.forward(request, response);  
	        
			//response.sendRedirect("/SchoolProject/TeacherHomeServlet");
			/*
	        String site;
	        
	        if( student )
	        	site = new String("http://localhost:8080/SchoolProject/StudentHomeServlet");
	        else
	        	site = new String("http://localhost:8080/SchoolProject/TeacherHomeServlet");
	        
	        response.setStatus(response.SC_MOVED_TEMPORARILY);
	        response.setHeader("Location", site);  
	        */
		}
		else{
    		out.println( "<!DOCTYPE html> \n" +
					 "<html> \n" + 
					 "<head> \n" +
					 "<meta charset=\"ISO-8859-1\"> \n" +
					 "<title>" + title + "</title> \n" +
					 "</head> \n" +
					 "<body> \n" );
		
    		out.println("Login Failed...!");
    		out.println( "<br><a href=\"http://localhost:8080/SchoolProject/\">Return to Homepage</a> \n");
    		
			out.println( "</body> \n" );
			out.println( "</html>\n" );

			out.close();
		}
		
		
		
	

	}

}
