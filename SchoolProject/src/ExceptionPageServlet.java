

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ExceptionPageServlet
 */
@WebServlet("/ExceptionPageServlet")
public class ExceptionPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExceptionPageServlet() {
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
		SQLException e = (SQLException) request.getAttribute("Exception");
		
		response.setContentType("text/html");  
		PrintWriter out = response.getWriter();
		String title = "Exception Occurred!";
		out.println( "<!DOCTYPE html> \n" +
				 "<html> \n" + 
				 "<head> \n" +
				 "<meta charset=\"ISO-8859-1\"> \n" +
				 "<title>" + title + "</title> \n" +
				 "</head> \n" +
				 "<body> \n" );
	
		out.println("<h1>Exception Occurred!</h1>");
		out.println("<h4>Error Message</h4>");
		out.println(e.getMessage());
		out.println("<h4>Stack Trace</h4>");
		out.println(e.getStackTrace() + "<br>");
		out.println(displayErrorForWeb(e));
		
		out.println( "<br><a href=\"http://localhost:8080/SchoolProject/\">Return to Homepage</a> \n");
		
		out.println( "</body> \n" );
		out.println( "</html>\n" );

		out.close();	
	}
	
	public String displayErrorForWeb(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		String stackTrace = sw.toString();
		return stackTrace.replace(System.getProperty("line.separator"), "<br/>\n");
	}


}

