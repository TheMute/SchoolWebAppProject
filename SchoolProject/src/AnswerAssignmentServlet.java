

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AnswerAssignmentServlet
 */
@WebServlet("/AnswerAssignmentServlet")
public class AnswerAssignmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AnswerAssignmentServlet() {
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
		String assignmentIDstr = request.getParameter("AssignmentID");
		String assignmentName = request.getParameter("AssignmentName");
		String question1 = request.getParameter("Question1");
		String question2 = request.getParameter("Question2");
		String question3 = request.getParameter("Question3");
		String question4 = request.getParameter("Question4");
		String question5 = request.getParameter("Question5");
		
		String answer1 = request.getParameter("Answer1");
		String answer2 = request.getParameter("Answer2");
		String answer3 = request.getParameter("Answer3");
		String answer4 = request.getParameter("Answer4");
		String answer5 = request.getParameter("Answer5");
		String completed = request.getParameter("completed");
		
		
		int studentID = Integer.parseInt(studentIDstr);
		int assignmentID = Integer.parseInt(assignmentIDstr);
		
		response.setContentType("text/html");  
		PrintWriter out = response.getWriter();
		String title = "Complete Assignment!";
		out.println( "<!DOCTYPE html> \n" +
					 "<html> \n" + 
					 "<head> \n" +
					 "<meta charset=\"ISO-8859-1\"> \n" +
					 "<title>" + title + "</title> \n" +
					 "</head> \n" +
					 "<body> \n" );
		
		
		out.println( "Here are the questions for the assignment " + assignmentName + "<br> \n" );
		
		
		out.println("<form action=\"CreateCompletedAssignmentServlet\" method=\"post\" > \n");
		out.println("<br><font face=\"verdana\" size=\"2px\">" + question1 + ":</font> \n");
	    out.println( "<br><textarea name = \"Answer1\" rows=\"4\" cols=\"75\">" + answer1  + "</textarea> \n ");
		out.println("<br><font face=\"verdana\" size=\"2px\">" + question2 + ":</font> \n");
	    out.println( "<br><textarea name = \"Answer2\" rows=\"4\" cols=\"75\">" + answer2  + "</textarea> \n ");
		out.println("<br><font face=\"verdana\" size=\"2px\">" + question3 + ":</font> \n");
	    out.println( "<br><textarea name = \"Answer3\" rows=\"4\" cols=\"75\">" + answer3  + "</textarea> \n ");
		out.println("<br><font face=\"verdana\" size=\"2px\">" + question4 + ":</font> \n");
	    out.println( "<br><textarea name = \"Answer4\" rows=\"4\" cols=\"75\">" + answer4  + "</textarea> \n ");
		out.println("<br><font face=\"verdana\" size=\"2px\">" + question5 + ":</font> \n");
	    out.println( "<br><textarea name = \"Answer5\" rows=\"4\" cols=\"75\">" + answer5  + "</textarea> \n ");
	    out.println("<br><input type=\"submit\" value=\"Complete this Assignment!\"> \n");
	    
		out.println("<input type=\"hidden\" name=\"StudentID\" value=\"" + studentID  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"AssignmentID\" value=\"" + assignmentID  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"AssignmentName\" value=\"" + assignmentName  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"completed\" value=\"" + completed  + "\"> \n");

		
		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
		
		out.println("</form> \n");
		
		out.println( "</body> \n" );
		out.println( "</html>\n" );	
		
	}

}
