

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class EditAssignmentServlet
 */
@WebServlet("/EditAssignmentServlet")
public class EditAssignmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditAssignmentServlet() {
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
		
		String assignmentName = request.getParameter("AssignmentName");
		String assignmentID = request.getParameter("AssignmentID");
		String className = request.getParameter("ClassName");
		String classID = request.getParameter("ClassID");
		
		String question1 = request.getParameter("Question1");
		String question2 = request.getParameter("Question2");
		String question3 = request.getParameter("Question3");
		String question4 = request.getParameter("Question4");
		String question5 = request.getParameter("Question5");
		
		response.setContentType("text/html");  
		PrintWriter out = response.getWriter();
		String title = "Edit Assignment!";
		out.println( "<!DOCTYPE html> \n" +
					 "<html> \n" + 
					 "<head> \n" +
					 "<meta charset=\"ISO-8859-1\"> \n" +
					 "<title>" + title + "</title> \n" +
					 "</head> \n" +
					 "<body> \n" );
		
		out.println("<h4>Edit Assignment: " + assignmentName + "</h4>\n");
		
		out.println("<form action=\"UpdateAssignmentServlet\" method=\"post\" > \n");
		out.println("<td><font face=\"verdana\" size=\"2px\">Class Name: " + className + "</font></td> \n");

		
	    out.println( "<br><font face=\"verdana\" size=\"2px\">Assignment Name:</font> \n" );
	    out.println( "<input type=\"text\" name=\"AssignmentName\" value=\"" + assignmentName  +   "\"> \n" );

		out.println( " <br><font face=\"verdana\" size=\"2px\">Question 1:</font> \n ");
	    out.println( "<br><textarea name = \"Question1\" rows=\"4\" cols=\"75\">" + question1  + "</textarea> \n ");
	    out.println( " <br><font face=\"verdana\" size=\"2px\">Question 2:</font> \n ");
	    out.println( "<br><textarea name = \"Question2\" rows=\"4\" cols=\"75\">" + question2  + "</textarea> \n ");
	    out.println( " <br><font face=\"verdana\" size=\"2px\">Question 3:</font> \n ");
	    out.println( "<br><textarea name = \"Question3\" rows=\"4\" cols=\"75\">" + question3  + "</textarea> \n ");
	    out.println( " <br><font face=\"verdana\" size=\"2px\">Question 4:</font> \n ");
	    out.println( "<br><textarea name = \"Question4\" rows=\"4\" cols=\"75\">" + question4  + "</textarea> \n ");
	    out.println( " <br><font face=\"verdana\" size=\"2px\">Question 5:</font> \n ");
	    out.println( "<br><textarea name = \"Question5\" rows=\"4\" cols=\"75\">" + question5  + "</textarea> \n ");
	    
		out.println("<input type=\"hidden\" name=\"AssignmentName\" value=\"" + assignmentName  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"AssignmentID\" value=\"" + assignmentID  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"ClassID\" value=\"" + classID  + "\"> \n");


	    
		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"user\" value=\"" + request.getParameter("User")  + "\"> \n");
		
		out.println("<br><input type=\"submit\" value=\"Update the Assignment!\"> \n");

		out.println("</form> \n");
		
		
		
		out.println( "</body> \n" );
		out.println( "</html>\n" );
	 
	
	    out.close();	
		
	}

}
