

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
 * Servlet implementation class ViewCompleteAssignmentServlet
 */
@WebServlet("/ViewCompleteAssignmentServlet")
public class ViewCompleteAssignmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewCompleteAssignmentServlet() {
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
		String classIDstr = request.getParameter("ClassID");
		String className = request.getParameter("ClassName");
		String subjectName = request.getParameter("SubjectName");
		String teacherName = request.getParameter("TeacherName");
		int studentID = Integer.parseInt(studentIDstr);
		int classID = Integer.parseInt(classIDstr);
		
		final String JDBC_DRIVER="com.mysql.jdbc.Driver";  
		final String DB_URL="jdbc:mysql://localhost:3306/SchoolProject";

		//  Database credentials
		final String USER = "cleung";
		final String PASS = "cleung";
		
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    
	    Vector<Integer> assignmentIDs = new Vector<Integer>();
	    Vector<String> assignmentNames = new Vector<String>();
	    Vector<String> question1s = new Vector<String>();
	    Vector<String> question2s = new Vector<String>();
	    Vector<String> question3s = new Vector<String>();
	    Vector<String> question4s = new Vector<String>();
	    Vector<String> question5s = new Vector<String>();
	    
	    int assignmentCount = 0;

	    Vector<Integer> completed = new Vector<Integer>();
	    
	    Vector<String> answer1s = new Vector<String>();
	    Vector<String> answer2s = new Vector<String>();
	    Vector<String> answer3s = new Vector<String>();
	    Vector<String> answer4s = new Vector<String>();
	    Vector<String> answer5s = new Vector<String>();
	    
	    try{
	         // Register JDBC driver
	         Class.forName("com.mysql.jdbc.Driver");
	    
	         // Open a connection
	         conn = DriverManager.getConnection(DB_URL,USER,PASS);


		     stmt = conn.prepareStatement( "SELECT * FROM ASSIGNMENT WHERE ClassID = ?" );
	     
	         stmt.setInt(1, classID);
	         ResultSet rs = stmt.executeQuery();
	         while(rs.next()){
	        	 assignmentCount++;
	        	 assignmentIDs.add( rs.getInt("AssignmentID"));
	        	 assignmentNames.add(  rs.getString("AssignmentName"));
	        	 question1s.add(  rs.getString("Question1"));
	        	 question2s.add(  rs.getString("Question2"));
	        	 question3s.add(  rs.getString("Question3"));
	        	 question4s.add(  rs.getString("Question4"));
	        	 question5s.add(  rs.getString("Question5"));
	        	 
	         }
	         
	         for( int i = 0; i < assignmentCount; i++ ){
			     stmt = conn.prepareStatement( "SELECT * FROM COMPLETEDASSIGNMENT WHERE StudentID = ? AND AssignmentID = ?" );
			     stmt.setInt(1, studentID);
			     stmt.setInt(2,  assignmentIDs.get(i));
			     rs = stmt.executeQuery();
			     
			     if( !rs.next() ){
			    	 completed.add( 0 );
			    	 answer1s.add( "" );
			    	 answer2s.add( "" );
			    	 answer3s.add( "" );
			    	 answer4s.add( "" );
			    	 answer5s.add( "" );
			     }
			     else{
			    	 completed.add( 1 );
			    	 
			    	 answer1s.add( rs.getString("Answer1"));
			    	 answer2s.add( rs.getString("Answer2"));
			    	 answer3s.add( rs.getString("Answer3"));
			    	 answer4s.add( rs.getString("Answer4"));
			    	 answer5s.add( rs.getString("Answer5"));
			    	 
			    	 
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
		String title = "Your Assignments!";
		out.println( "<!DOCTYPE html> \n" +
					 "<html> \n" + 
					 "<head> \n" +
					 "<meta charset=\"ISO-8859-1\"> \n" +
					 "<title>" + title + "</title> \n" +
					 "</head> \n" +
					 "<body> \n" );
		
		
		out.println( "There are " + assignmentCount + " assignments for the class " + className + "<br> \n" );
		
		out.println("<br><table width=\"50%\" border=\"1\"> \n");
		out.println("<tr> \n");
		out.println("<td> Assignment Name </td> \n");
		out.println("<td> Complete Your Assignments </td> \n");
		out.println("</tr> \n");
		for( int i = 0; i < assignmentCount; i++){
			out.println("<tr> \n");
			out.println("<td> " + assignmentNames.get(i) + " </td> \n");
			
			out.println("<td> \n");
			
			out.println("<form action=\"AnswerAssignmentServlet\" method=\"post\" > \n");
			out.println("<input type=\"hidden\" name=\"StudentID\" value=\"" + studentID  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"AssignmentID\" value=\"" + assignmentIDs.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"AssignmentName\" value=\"" + assignmentNames.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Question1\" value=\"" + question1s.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Question2\" value=\"" + question2s.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Question3\" value=\"" + question3s.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Question4\" value=\"" + question4s.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Question5\" value=\"" + question5s.get(i)  + "\"> \n");
			
			out.println("<input type=\"hidden\" name=\"Answer1\" value=\"" + answer1s.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Answer2\" value=\"" + answer2s.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Answer3\" value=\"" + answer3s.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Answer4\" value=\"" + answer4s.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Answer5\" value=\"" + answer5s.get(i)  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"completed\" value=\"" + completed.get(i)  + "\"> \n");
			
			
			
			out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
			
			out.println("<br><input type=\"submit\" value=\"Complete this Assignment!\"> \n");
			out.println("</form> \n");
			
			out.println("</td> \n");

			out.println("</tr> \n");


			
		}
		out.println("</table> \n");

		out.println("<br><form action=\"StudentHomeServlet\" method=\"post\" > \n");
		
		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
		
		out.println("<br><input type=\"submit\" value=\"Return to Student Home Page!\"> \n");
		out.println("</form> \n");
		
		out.println( "</body> \n" );
		out.println( "</html>\n" );	}

}
