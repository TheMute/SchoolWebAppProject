

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
 * Servlet implementation class ViewGradedAssignmentServlet
 */
@WebServlet("/ViewGradedAssignmentServlet")
public class ViewGradedAssignmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewGradedAssignmentServlet() {
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
		//String teacherIDstr = request.getParameter("TeacherID");
		//String classIDstr = request.getParameter("ClassID");
		//int teacherID = Integer.parseInt(teacherIDstr);
		//int classID = Integer.parseInt(classIDstr);
		
		//String className = request.getParameter("ClassName");
		//String subjectName = request.getParameter("SubjectgName");
		
		String assignmentIDstr = request.getParameter("AssignmentID");
		int assignmentID = Integer.parseInt(assignmentIDstr);
		
		String assignmentName = request.getParameter("AssignmentName");
		String question1 = request.getParameter("Question1");
		String question2 = request.getParameter("Question2");
		String question3 = request.getParameter("Question3");
		String question4 = request.getParameter("Question4");
		String question5 = request.getParameter("Question5");
		
		String studentIDstr = request.getParameter("StudentID");
		int studentID = Integer.parseInt(studentIDstr);
		
		String firstName = request.getParameter("FirstName");
		String lastName = request.getParameter("LastName");
		
		String answer1 = null;
		String answer2 = null;
		String answer3 = null;
		String answer4 = null;
		String answer5 = null;
		
		int completedAssignmentID = 0;
		
		boolean graded = false;
		int checkOff1 = 0;
		int checkOff2 = 0;
		int checkOff3 = 0;
		int checkOff4 = 0;
		int checkOff5 = 0;
		int totalCheckOffs = 5;
		
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
			
			stmt = conn.prepareStatement( "SELECT * FROM CompletedAssignment a WHERE StudentID = ? AND AssignmentID = ?" );
			stmt.setInt(1, studentID);
			stmt.setInt(2, assignmentID);
			rs = stmt.executeQuery();
			while(rs.next()){
				completedAssignmentID = rs.getInt("CompletedAssignmentID");
				answer1 = rs.getString("Answer1");
				answer2 = rs.getString("Answer2");
				answer3 = rs.getString("Answer3");
				answer4 = rs.getString("Answer4");
				answer5 = rs.getString("Answer5");
			}
			
			stmt = conn.prepareStatement( "SELECT * FROM GradedAssignment WHERE CompletedAssignmentID = ?");
			stmt.setInt(1, completedAssignmentID);
			rs = stmt.executeQuery();
			while( rs.next()){
				graded = true;
				checkOff1 = rs.getInt("CheckOff1");
				checkOff2 = rs.getInt("CheckOff2");
				checkOff3 = rs.getInt("CheckOff3");
				checkOff4 = rs.getInt("CheckOff4");
				checkOff5 = rs.getInt("CheckOff5");
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
		String title = "Grade Students' Assignments!";
		out.println( "<!DOCTYPE html> \n" +
					 "<html> \n" + 
					 "<head> \n" +
					 "<meta charset=\"ISO-8859-1\"> \n" +
					 "<title>" + title + "</title> \n" +
					 "</head> \n" +
					 "<body> \n" );
		
		
		out.println( "<h4> Graded Assignment: " + assignmentName + " for " + firstName + " " + lastName + " </h4> <br> \n"  );
		
		
		out.println("<br><font face=\"verdana\" size=\"2px\">Student Name: " + firstName + " " + lastName  +"</font> \n");
		out.println("<br><font face=\"verdana\" size=\"2px\">Assignment Name: " + assignmentName  +"</font> \n");
		
		//out.println("<form action=\"CreateGradedAssignment\" method=\"post\" > \n");
		
		out.println("<table width=\"50%\" border=\"1\"> \n");
		out.println("<tr> \n");
		out.println("<td> <font face=\"verdana\" size=\"2px\">Questions </font> </td> \n");
		out.println("<td> <font face=\"verdana\" size=\"2px\">Answers </font> </td> \n");
		out.println("<td> <font face=\"verdana\" size=\"2px\">Correct? </font> </td> \n");
		out.println("</tr> \n");	
		out.println("<tr> \n");
		out.println("<td> <font face=\"verdana\" size=\"2px\">" + question1 + "</font> </td> \n");
		out.println("<td> <font face=\"verdana\" size=\"2px\">" + answer1 + "</font> </td> \n");
		if( checkOff1 == 1 ){
			out.println("<td> <font face=\"verdana\" size=\"2px\">Yes </font> </td> \n");
		}
		else{
			out.println("<td> <font face=\"verdana\" size=\"2px\">No </font> </td> \n");
		}
		out.println("</tr> \n");
		out.println("<tr> \n");
		out.println("<td> <font face=\"verdana\" size=\"2px\">" + question2 + "</font> </td> \n");
		out.println("<td> <font face=\"verdana\" size=\"2px\">" + answer2 + "</font> </td> \n");
		if( checkOff2 == 1 ){
			out.println("<td> <font face=\"verdana\" size=\"2px\">Yes </font> </td> \n");
		}
		else{
			out.println("<td> <font face=\"verdana\" size=\"2px\">No </font> </td> \n");
		}		
		out.println("</tr> \n");
		out.println("<tr> \n");
		out.println("<td> <font face=\"verdana\" size=\"2px\">" + question3 + "</font> </td> \n");
		out.println("<td> <font face=\"verdana\" size=\"2px\">" + answer3 + "</font> </td> \n");
		if( checkOff3 == 1 ){
			out.println("<td> <font face=\"verdana\" size=\"2px\">Yes </font> </td> \n");
		}
		else{
			out.println("<td> <font face=\"verdana\" size=\"2px\">No </font> </td> \n");
		}		
		out.println("</tr> \n");
		out.println("<tr> \n");
		out.println("<td> <font face=\"verdana\" size=\"2px\">" + question4 + "</font> </td> \n");
		out.println("<td> <font face=\"verdana\" size=\"2px\">" + answer4 + "</font> </td> \n");
		if( checkOff4 == 1 ){
			out.println("<td> <font face=\"verdana\" size=\"2px\">Yes </font> </td> \n");
		}
		else{
			out.println("<td> <font face=\"verdana\" size=\"2px\">No </font> </td> \n");
		}	
		out.println("</tr> \n");
		out.println("<tr> \n");
		out.println("<td> <font face=\"verdana\" size=\"2px\">" + question5 + "</font> </td> \n");
		out.println("<td> <font face=\"verdana\" size=\"2px\">" + answer5 + "</font> </td> \n");
		if( checkOff5 == 1 ){
			out.println("<td> <font face=\"verdana\" size=\"2px\">Yes </font> </td> \n");
		}
		else{
			out.println("<td> <font face=\"verdana\" size=\"2px\">No </font> </td> \n");
		}	
		out.println("</tr> \n");
		
		
		
		out.println("</table> \n");
		
		double score = (checkOff1 + checkOff2 + checkOff3 + checkOff4 + checkOff5 ) / (double)totalCheckOffs * 100;
		
		if( graded ){ 
			out.println("<td> <font face=\"verdana\" size=\"2px\">Current Score: " + score + "%</font> </td> \n");

		}
		else{
			out.println("<td> <font face=\"verdana\" size=\"2px\">Current Score: Not Graded Yet </font> </td> \n");

		}


		/*
		out.println("<table width=\"50%\" border=\"1\"> \n");
		out.println("<tr> \n");
		out.println("<td> Student Name </td> \n");
		out.println("<td> Grade Assignment </td> \n");
		
		out.println("</tr> \n");
		for( int i = 0; i < studentIDs.size(); i++){
			out.println("<tr> \n");
			out.println("<td> " + firstName.get(i) + " " + lastName.get(i) + " </td> \n");
			
			out.println("<td> \n");
			
			out.println("<form action=\"GradeAssignmentServlet\" method=\"post\" > \n");
			out.println("<input type=\"hidden\" name=\"TeacherID\" value=\"" + teacherID  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"ClassID\" value=\"" + classID  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"ClassName\" value=\"" + className  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"SubjectName\" value=\"" + subjectName  + "\"> \n");
			
			out.println("<input type=\"hidden\" name=\"AssignmentID\" value=\"" + assignmentID  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"AssignmentName\" value=\"" + assignmentName  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Question1\" value=\"" + question1  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Question2\" value=\"" + question2  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Question3\" value=\"" + question3 + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Question4\" value=\"" + question4  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Question5\" value=\"" + question5  + "\"> \n");
			
			out.println("<input type=\"hidden\" name=\"StudentID\" value=\"" + studentID  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"FirstName\" value=\"" + firstName  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"LastName\" value=\"" + lastName  + "\"> \n");
			
			
			//out.println("<input type=\"hidden\" name=\"TeacherName\" value=\"" + enrolledTeacherNames.get(i)  + "\"> \n");
			
			out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
			
			out.println("<br><input type=\"submit\" value=\"Grade Assignments!\"> \n");
			out.println("</form> \n");
			
			out.println("</td> \n");
			
			out.println("</tr> \n");
		}
		out.println("</table> \n");
		*/
		
		//out.println("<input type=\"hidden\" name=\"CompletedAssignmentID\" value=\"" + completedAssignmentID  + "\"> \n");
		//out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
		//out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
	
		//out.println("<br><input type=\"submit\" value=\"Grade the Assignment!\"> \n");

		//out.println("</form> \n");

		
		out.println("<br><form action=\"StudentHomeServlet\" method=\"post\" > \n");
		
		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
		
		out.println("<br><input type=\"submit\" value=\"Return to Student Home Page!\"> \n");
		out.println("</form> \n");
		
		out.println( "</body> \n" );
		out.println( "</html>\n" );	}

}
