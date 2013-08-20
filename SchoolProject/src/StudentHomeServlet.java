

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class StudentHomeServlet
 */
@WebServlet("/StudentHomeServlet")
public class StudentHomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String email;
	private static String password;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentHomeServlet() {
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
		
		email = request.getParameter("Email");
		password = request.getParameter("Password");
		
		
		
		int studentID = 0;
		String firstName = null;
		String lastName = null;
        
		// JDBC driver name and database URL
		final String JDBC_DRIVER="com.mysql.jdbc.Driver";  
		final String DB_URL="jdbc:mysql://localhost:3306/SchoolProject";
		
		//  Database credentials
		final String USER = "cleung";
		final String PASS = "cleung";
		  
		Connection conn = null;
		PreparedStatement stmt = null;

		
		Vector<Integer> classIDs = new Vector<Integer>();
		Vector<String> classNames = new Vector<String>();
		Vector<Integer> subjectIDs = new Vector<Integer>();
		Vector<Integer> teacherIDs = new Vector<Integer>();
		
		Vector<String> enrolledClassIDs = new Vector<String>();
		Vector<String> enrolledClassNames = new Vector<String>();
		Vector<Integer> enrolledSubjectIDs = new Vector<Integer>();
		Vector<Integer> enrolledTeacherIDs = new Vector<Integer>();	
		
		Vector<String> enrolledSubjectNames = new Vector<String>();
		Vector<String> enrolledTeacherNames = new Vector<String>();	
		
	      
		try{
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			//net.sourceforge.jtds.jdbc.Driver
			//com.microsoft.sqlserver.jdbc.SQLServerDriver
			// Open a connection
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			
			stmt = conn.prepareStatement( "SELECT * FROM Student WHERE Email = ? AND Password = ?" );
			stmt.setString(1, email);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			
			rs.next();
	        studentID = rs.getInt("StudentID");
	        firstName = rs.getString("FirstName");
	        lastName = rs.getString("LastName");
			
	        stmt = conn.prepareStatement(" SELECT * FROM CLASS ");
	        rs = stmt.executeQuery();
	        while( rs.next() ){
	        	classIDs.add( rs.getInt("ClassID"));
	        	classNames.add( rs.getString("ClassName"));
	        	subjectIDs.add( rs.getInt("SubjectID"));
	        	teacherIDs.add(rs.getInt("TeacherID"));
	        }
	        
	        stmt = conn.prepareStatement(" SELECT a.ClassID, a.ClassName, a.SubjectID, a.TeacherID FROM CLASS a, STUDENTCLASSREL b WHERE  b.StudentID = ? AND a.ClassID = b.ClassID ORDER BY a.ClassName");
	        stmt.setInt(1, studentID);
	        rs = stmt.executeQuery();
	        while( rs.next()){
	        	enrolledClassIDs.add( rs.getString("ClassID"));
	        	enrolledClassNames.add( rs.getString("ClassName"));
	        	enrolledSubjectIDs.add( rs.getInt("SubjectID"));
	        	enrolledTeacherIDs.add(rs.getInt("TeacherID"));
	        }
	        
	        for( int i : enrolledSubjectIDs ){
	        	
	        	stmt = conn.prepareStatement(" SELECT * FROM SUBJECT WHERE SubjectID = ? ");
	        	stmt.setInt(1, i);
	        	rs = stmt.executeQuery();
	        	rs.next();
	        	enrolledSubjectNames.add( rs.getString("SubjectName"));
	        }

	        for( int i : enrolledTeacherIDs ){
	        	
	        	stmt = conn.prepareStatement(" SELECT * FROM Teacher WHERE TeacherID = ? ");
	        	stmt.setInt(1, i);
	        	rs = stmt.executeQuery();
	        	rs.next();
	        	enrolledTeacherNames.add( rs.getString("FirstName") + " " + rs.getString("LastName"));
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
		out.println( "<!DOCTYPE html> \n" +
					 "<html> \n" + 
					 "<head> \n" +
					 "<meta charset=\"ISO-8859-1\"> \n" +
					 "<title>" + title + "</title> \n" +
					 "</head> \n" +
					 "<body> \n" );
		
		out.println("<h1>Student Home Page </h1>\n");
		out.println("<h2>Welcome " + firstName + " " + lastName + "!</h2>\n");
		
		out.println("<b>Enroll in a class</b> \n");
		out.println("<br>Select a class from the dropdown to enroll in the class \n");
		out.println("<form action=\"EnrollClassServlet\" method=\"post\" > \n");

		out.println("<td><font face=\"verdana\" size=\"2px\">Class Name:</font></td> \n");
		out.println("<select name=\"ClassDropdown\"> \n");
		int j = 0;
		for( String s : classNames ){
			out.println("<option value=\"" + classIDs.get(j) + "\">" + s + "</option> \n");
			j++;
		}
		out.println("</select> \n");
		
		out.println("<input type=\"hidden\" name=\"StudentID\" value=\"" + studentID  + "\"> \n");
		
		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + email  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Password\" value=\"" + password  + "\"> \n");
		
		out.println("<br><input type=\"submit\" value=\"Enroll in the Class!\"> \n");
		out.println("</form> \n");
		
		
		out.println("<h4>View your classes</h4>\n");
		
		out.println("<table width=\"75%\" border=\"1\"> \n");
		out.println("<tr> \n");
		out.println("<td> Class Name </td> \n");
		out.println("<td> Subject Name </td> \n");
		out.println("<td> Teacher Name </td> \n");
		out.println("<td> Complete Your Assignments </td> \n");
		out.println("<td> View Graded Assignments </td> \n");
		out.println("<td> Drop this Class </td> \n");
		
		
		out.println("</tr> \n");
		
		for( int i = 0; i < enrolledClassNames.size(); i++){
			out.println("<tr> \n");
			out.println("<td> " + enrolledClassNames.get(i) + " </td> \n");
			out.println("<td> " + enrolledSubjectNames.get(i) + " </td> \n");
			out.println("<td> " + enrolledTeacherNames.get(i) + " </td> \n");
			
			out.println("<td> \n");
			
			out.println("<form action=\"ViewCompleteAssignmentServlet\" method=\"post\" > \n");
			out.println("<input type=\"hidden\" name=\"StudentID\" value=\"" + studentID  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"ClassID\" value=\"" + enrolledClassIDs.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"ClassName\" value=\"" + enrolledClassNames.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"SubjectName\" value=\"" + enrolledSubjectNames.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"TeacherName\" value=\"" + enrolledTeacherNames.get(i)  + "\"> \n");
			
			out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
			
			out.println("<br><input type=\"submit\" value=\"View Assignments!\"> \n");
			out.println("</form> \n");
			
			out.println("</td> \n");
			
			out.println("<td> \n");
			
			out.println("<form action=\"ListStudentsGradedAssignmentServlet\" method=\"post\" > \n");
			out.println("<input type=\"hidden\" name=\"StudentID\" value=\"" + studentID  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"FirstName\" value=\"" + firstName  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"LastName\" value=\"" + lastName  + "\"> \n");
			
			out.println("<input type=\"hidden\" name=\"ClassID\" value=\"" + enrolledClassIDs.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"ClassName\" value=\"" + enrolledClassNames.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"SubjectName\" value=\"" + enrolledSubjectNames.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"TeacherName\" value=\"" + enrolledTeacherNames.get(i)  + "\"> \n");
			
			out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
			
			out.println("<br><input type=\"submit\" value=\"View Graded Assignments!\"> \n");
			out.println("</form> \n");
			
			out.println("</td> \n");
			
			out.println("<td> \n");
			
			out.println("<form action=\"DropClassServlet\" method=\"post\" > \n");


			out.println("<input type=\"hidden\" name=\"StudentID\" value=\"" + studentID  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"ClassID\" value=\"" + enrolledClassIDs.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"ClassName\" value=\"" + enrolledClassNames.get(i)  + "\"> \n");

			
			out.println("<input type=\"hidden\" name=\"Email\" value=\"" + email  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Password\" value=\"" + password  + "\"> \n");
			
			out.println("<br><input type=\"submit\" value=\"Drop this Class!\"> \n");
			out.println("</form> \n");
			
			out.println("</td> \n");
			

			out.println("</tr> \n");
		}
		out.println("</table> \n");
		
		out.println( "</body> \n" );
		out.println( "</html>\n" );
 

        out.close();	
	}

}
