

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
 * Servlet implementation class CreateGradedAssignment
 */
@WebServlet("/CreateGradedAssignment")
public class CreateGradedAssignment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateGradedAssignment() {
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
		String completedAssignmentIDstr = request.getParameter("CompletedAssignmentID");
		int completedAssignmentID = Integer.parseInt(completedAssignmentIDstr);
		
		int checkOff1, checkOff2, checkOff3, checkOff4, checkOff5;
		
		if( request.getParameter("CheckOff1") != null){
			checkOff1 = 1;
		}
		else{
			checkOff1 = 0;
		}
		if( request.getParameter("CheckOff2") != null){
			checkOff2 = 1;
		}
		else{
			checkOff2 = 0;
		}
		if( request.getParameter("CheckOff3") != null){
			checkOff3 = 1;
		}
		else{
			checkOff3 = 0;
		}
		if( request.getParameter("CheckOff4") != null){
			checkOff4 = 1;
		}
		else{
			checkOff4 = 0;
		}
		if( request.getParameter("CheckOff5") != null){
			checkOff5 = 1;
		}
		else{
			checkOff5 = 0;
		}
		
		boolean graded = false;
		
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
			//net.sourceforge.jtds.jdbc.Driver
			//com.microsoft.sqlserver.jdbc.SQLServerDriver
			// Open a connection
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			
			
			stmt = conn.prepareStatement( "SELECT CompletedAssignmentID FROM GradedAssignment WHERE CompletedAssignmentID = ?" );
			stmt.setInt(1, completedAssignmentID);
			rs = stmt.executeQuery();
			
			while( rs.next()){
				graded = true;
			}

			if( graded ){
				stmt = conn.prepareStatement( "UPDATE GradedAssignment SET CheckOff1=?, CheckOff2=?, CheckOff3=?, CheckOff4=?, CheckOff5=? WHERE CompletedAssignmentID=?" );
				stmt.setInt(1, checkOff1);
				stmt.setInt(2, checkOff2);
				stmt.setInt(3, checkOff3);
				stmt.setInt(4, checkOff4);
				stmt.setInt(5, checkOff5);
				stmt.setInt(6, completedAssignmentID);
			}
			else{
				stmt = conn.prepareStatement( "INSERT INTO GradedAssignment VALUES (NULL, ?, ?, ?, ?, ?, ? )" );
				stmt.setInt(1, completedAssignmentID);
				stmt.setInt(2, checkOff1);
				stmt.setInt(3, checkOff2);
				stmt.setInt(4, checkOff3);
				stmt.setInt(5, checkOff4);
				stmt.setInt(6, checkOff5);
			}

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
		String title = "Create Graded Assignment!";
		out.println( "<!DOCTYPE html> \n" +
					 "<html> \n" + 
					 "<head> \n" +
					 "<meta charset=\"ISO-8859-1\"> \n" +
					 "<title>" + title + "</title> \n" +
					 "</head> \n" +
					 "<body> \n" );
		
		out.println( "<h4> Graded assignment created! </h4> <br> \n"  );

		
		out.println("<br><form action=\"TeacherHomeServlet\" method=\"post\" > \n");
		
		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
		
		out.println("<br><input type=\"submit\" value=\"Return to Teacher Home Page!\"> \n");
		out.println("</form> \n");
		
		out.println( "</body> \n" );
		out.println( "</html>\n" );
	}

}
