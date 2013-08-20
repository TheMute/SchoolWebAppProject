

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TeacherHomeServlet
 */
@WebServlet("/TeacherHomeServlet")
public class TeacherHomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static String email;
	private static String password;
	private static String user;
	private static boolean student;
	private static String radio;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TeacherHomeServlet() {
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
	    
	    String firstName = null, lastName = null;
	    /*user = request.getParameter("user");
	    
	    System.out.println("static: " + email);
	    System.out.println("user: " + user);
	    if( user.equals("Student") )
	    	student = true;
	    else
	    	student = false;
	    
	    radio = request.getParameter("user");	
	    */
	  
		// JDBC driver name and database URL
		final String JDBC_DRIVER="com.mysql.jdbc.Driver";  
		final String DB_URL="jdbc:mysql://localhost:3306/SchoolProject";
		
		//  Database credentials
		final String USER = "cleung";
		final String PASS = "cleung";
		  
		Connection conn = null;
		Statement stmt = null;
		String queryEmail = null;
		String queryPassword = null;
		
		Vector<Integer> subjectIDs = new Vector<Integer>();
		Vector<String> subjectNames = new Vector<String>();
		int teacherID = 0;
		
		Vector<Integer> classIDs = new Vector<Integer>();
		Vector<String> classNames = new Vector<String>();
		Vector<Integer> classSubjectIDs = new Vector<Integer>();
		Vector<String> classSubjectNames = new Vector<String>();
		
		Vector<Integer> studentCount = new Vector<Integer>();
	      
		try{
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			//net.sourceforge.jtds.jdbc.Driver
			//com.microsoft.sqlserver.jdbc.SQLServerDriver
			// Open a connection
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			
			// Execute SQL query
			/*
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
			*/
			stmt = conn.createStatement();
			String sql = "SELECT * FROM SUBJECT";
			ResultSet subjectsRS = stmt.executeQuery(sql);
	
	        while( subjectsRS.next() ){
	        	subjectIDs.add( subjectsRS.getInt( "SubjectID" ));
	        	subjectNames.add( subjectsRS.getString("SubjectName"));
	        }
	        
	        sql = "SELECT * FROM TEACHER WHERE EMAIL = '" + email + "'";
	        ResultSet teacherIDRS = stmt.executeQuery(sql);
	        while( teacherIDRS.next() ){
	        	teacherID = teacherIDRS.getInt( "TeacherID" );
	        	firstName = teacherIDRS.getString( "FirstName" );
	        	lastName = teacherIDRS.getString( "LastName" );
	        	
	        	break;
	        }
	        
	        sql = "SELECT * FROM CLASS WHERE TeacherID = '" + teacherID + "' ORDER BY ClassName";
	        ResultSet classesRS = stmt.executeQuery(sql);
	        while( classesRS.next() ){
	        	classIDs.add( classesRS.getInt( "ClassID" ));
	        	classNames.add( classesRS.getString("ClassName"));
	        	classSubjectIDs.add( classesRS.getInt("SubjectID"));
	        }
	        
	        ResultSet subjectNamesRS;
	        for( int i : classSubjectIDs){
	        	sql = "SELECT * FROM SUBJECT WHERE SubjectID = " + i;
	        	subjectNamesRS = stmt.executeQuery(sql);
	        	subjectNamesRS.next();
	        	classSubjectNames.add( subjectNamesRS.getString("SubjectName"));
	        }
	        
	        ResultSet studentsRS;
	        for( int i : classIDs ){
	        	sql = "SELECT COUNT(A.FirstName) as Count FROM Student A JOIN STUDENTCLASSREL C on (A.StudentID = C.StudentID) JOIN CLASS B on (C.ClassID = B.ClassID) WHERE B.TeacherID = " + teacherID + " AND C.ClassID = " + i;
	        	studentsRS = stmt.executeQuery(sql);
		        while( studentsRS.next() ){
		        	studentCount.add( studentsRS.getInt("Count"));
		        	break;
		        }
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
		String title = "Teacher Home Page";
		out.println( "<!DOCTYPE html> \n" +
					 "<html> \n" + 
					 "<head> \n" +
					 "<meta charset=\"ISO-8859-1\"> \n" +
					 "<title>" + title + "</title> \n" +
					 "</head> \n" +
					 "<body> \n" );
		
		out.println("<h1>Teacher Home Page </h1>\n");
		out.println("<h2>Welcome " + firstName + " " + lastName + "!</h2>\n");

		out.println("<b>Create a Class </b>\n");
		out.println("<br>Type a name and select a subject from the dropdown to create a class\n");
		
		out.println("<form action=\"CreateClassServlet\" method=\"post\" > \n");
		out.println("<table> \n");
		out.println("<tr> \n");
		out.println("<td><font face=\"verdana\" size=\"2px\">Class Name:</font></td> \n");
		out.println("<td><input type=\"text\" name=\"ClassName\"></td> \n");
		out.println("</tr> \n");
		out.println("</table> \n");
		out.println("<td><font face=\"verdana\" size=\"2px\">Subject Name:</font></td> \n");
		out.println("<select name=\"SubjectsDropdown\"> \n");
		
		int subjectID = 1;
		for( String s : subjectNames ){
			out.println("<option value=\"" + subjectID + "\">" + s + "</option> \n");
			subjectID++;
		}
		out.println("</select> \n");
		out.println("<input type=\"hidden\" name=\"TeacherID\" value=\"" + teacherID  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + email  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Paswword\" value=\"" + password  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"user\" value=\"" + user  + "\"> \n");
		
		out.println("<br><input type=\"submit\" value=\"Create the Class!\"> \n");
		out.println("</form> \n");
		
		out.println("<h4>View Your Classes </h4>\n");
		out.println("<table width=\"50%\" border=\"1\"> \n");
		out.println("<tr> \n");
		out.println("<td> Class Name </td> \n");
		out.println("<td> Subject Name </td> \n");
		out.println("<td> Number of Students </td> \n");
		out.println("<td> Grade Assignments </td> \n");
		
		out.println("</tr> \n");
		for( int i = 0; i < classIDs.size(); i++){
			out.println("<tr> \n");
			out.println("<td> " + classNames.get(i) + " </td> \n");
			out.println("<td> " + classSubjectNames.get(i) + " </td> \n");
			out.println("<td> " + studentCount.get(i) + " </td> \n");
			
			out.println("<td> \n");
			
			out.println("<form action=\"ViewAssignmentsServlet\" method=\"post\" > \n");
			out.println("<input type=\"hidden\" name=\"TeacherID\" value=\"" + teacherID  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"ClassID\" value=\"" + classIDs.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"ClassName\" value=\"" + classNames.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"SubjectName\" value=\"" + classSubjectNames.get(i)  + "\"> \n");
			//out.println("<input type=\"hidden\" name=\"TeacherName\" value=\"" + enrolledTeacherNames.get(i)  + "\"> \n");
			
			out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
			
			out.println("<br><input type=\"submit\" value=\"View Assignments!\"> \n");
			out.println("</form> \n");
			
			out.println("</td> \n");
			
			out.println("</tr> \n");
		}
		out.println("</table> \n");
		
		out.println("<h4>Create an Assignment </h4>\n");
		out.println("<form action=\"CreateAssignmentServlet\" method=\"post\" > \n");
		out.println("<td><font face=\"verdana\" size=\"2px\">Class Name:</font></td> \n");
		out.println("<select name=\"ClassesDropdown\"> \n");
		int j = 0;
		for( String s : classNames ){
			out.println("<option value=\"" + classIDs.get(j) + "\">" + s + "</option> \n");
			j++;
		}
		out.println("</select> \n");
		
	    out.println( "<br><font face=\"verdana\" size=\"2px\">Assignment Name:</font> \n" );
	    out.println( "<input type=\"text\" name=\"AssignmentName\"> \n" );

		out.println( " <br><font face=\"verdana\" size=\"2px\">Question 1:</font> \n ");
	    out.println( "<br><textarea name = \"Question1\" rows=\"4\" cols=\"75\"></textarea> \n ");
	    out.println( " <br><font face=\"verdana\" size=\"2px\">Question 2:</font> \n ");
	    out.println( "<br><textarea name = \"Question2\" rows=\"4\" cols=\"75\"></textarea> \n ");
	    out.println( " <br><font face=\"verdana\" size=\"2px\">Question 3:</font> \n ");
	    out.println( "<br><textarea name = \"Question3\" rows=\"4\" cols=\"75\"></textarea> \n ");
	    out.println( " <br><font face=\"verdana\" size=\"2px\">Question 4:</font> \n ");
	    out.println( "<br><textarea name = \"Question4\" rows=\"4\" cols=\"75\"></textarea> \n ");
	    out.println( " <br><font face=\"verdana\" size=\"2px\">Question 5:</font> \n ");
	    out.println( "<br><textarea name = \"Question5\" rows=\"4\" cols=\"75\"></textarea> \n ");
	    
		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + email  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Password\" value=\"" + password  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"user\" value=\"" + user  + "\"> \n");
		
		out.println("<br><input type=\"submit\" value=\"Create the Assignment!\"> \n");

		out.println("</form> \n");
		
		out.println( "</body> \n" );
		out.println( "</html>\n" );
	 
	
	    out.close();	
	}

}
