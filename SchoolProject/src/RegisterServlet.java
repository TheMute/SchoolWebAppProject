

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost( request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	      response.setContentType("text/html");
/*
	      // New location to be redirected
	      String site = new String("http://localhost:8080/SchoolProject/register.jsp");

	      response.setStatus(response.SC_MOVED_TEMPORARILY);
	      response.setHeader("Location", site);  
*/
	      response.setContentType("text/html");  
	      PrintWriter out = response.getWriter();
	      String title = "Register";
	      out.println( "<!DOCTYPE html> \n" +
	    		  		"<html> \n" + 
						"<head> \n" +
						"<meta charset=\"ISO-8859-1\"> \n" +
						"<title>" + title + "</title> \n" +
						"</head> \n" +
						"<body> \n" );
	      
	      out.println( "<script type = \"text/javascript\"> \n" );
	      out.println( "function checkForm(form) " );
	      out.println( "{ \n");
	      
	      out.println( " if( form.FirstName.value == \"\" ){ \n ");
	      out.println( "alert(\"Error: First Name cannont be blank!\" ); \n");
	      out.println( "form.FirstName.focus() \n" );
	      out.println( "return false; \n");
	      out.println( "} \n" );
	      
	      out.println( " if( form.LastName.value == \"\" ){ \n ");
	      out.println( "alert(\"Error: Last Name cannont be blank!\" ); \n");
	      out.println( "form.LastName.focus() \n" );
	      out.println( "return false; \n");
	      out.println( "} \n" );
	      
	      out.println( " if( form.Email.value == \"\" ){ \n ");
	      out.println( "alert(\"Error: Email cannont be blank!\" ); \n");
	      out.println( "form.Email.focus() \n" );
	      out.println( "return false; \n");
	      out.println( "} \n" );
	      
	      out.println( " if( form.Password.value == \"\" ){ \n ");
	      out.println( "alert(\"Error: Password cannont be blank!\" ); \n");
	      out.println( "form.Password.focus() \n" );
	      out.println( "return false; \n");
	      out.println( "} \n" );
	      
	      out.println( " if( form.Password.value != form.ConfirmPassword.value ){ \n ");
	      out.println( "alert(\"Error: Password and Confirmation Password do not match!\" ); \n");
	      out.println( "form.Password.focus() \n" );
	      out.println( "return false; \n");
	      out.println( "} \n" );
	      
	      
	      out.println( "return true; \n");
	      out.println( "} \n" );
	      
	      out.println("</script> \n");

	      out.println( "Fill out the following form to register \n" );
	      out.println( "<form action=\"CreateUser\" method=\"post\" onsubmit=\"return checkForm(this)\" > \n" );
	      out.println( "<table> \n" );
	      out.println( "<tr> \n" );
	      out.println( "<td><font face=\"verdana\" size=\"2px\">First Name:</font></td> \n" );
	      out.println( "<td><input type=\"text\" name=\"FirstName\"></td> \n" );
		  out.println( "</tr> \n" );
		  out.println( "<tr> \n" );
		  out.println( "<td><font face=\"verdana\" size=\"2px\">Last Name:</font></td> \n" );
		  out.println( "<td><input type=\"text\" name=\"LastName\"></td> \n" );
		  out.println( "</tr> \n" );
		  out.println( "<tr> \n" );
		  out.println( "<td><font face=\"verdana\" size=\"2px\">Email:</font></td> \n" );
		  out.println( "<td><input type=\"text\" name=\"Email\"></td> \n" );
		  out.println( "</tr> \n" );
		  out.println( "<tr> \n" );
		  out.println( "<td><font face=\"verdana\" size=\"2px\">Password:</font></td> \n" );
		  out.println( "<td><input type=\"password\" name=\"Password\"></td> \n" );
		  out.println( "</tr> \n" );
		  out.println( "<tr> \n" );
		  out.println( "<td><font face=\"verdana\" size=\"2px\">Confirm Password:</font></td> \n" );
		  out.println( "<td><input type=\"password\" name=\"ConfirmPassword\"></td> \n" );
		  out.println( "</tr> \n" );
		  out.println( "</table> \n" );
		  out.println( "<INPUT id=\"Student\" type=\"radio\" value=\"Student\" name=\"user\" CHECKED> \n" );
		  out.println( "<td><font face=\"verdana\" size=\"2px\">Student</font></td> \n" );
		  out.println( "<INPUT id=\"Teacher\" type=\"radio\" value=\"Teacher\" name=\"user\"> \n" );
		  out.println( "<td><font face=\"verdana\" size=\"2px\">Teacher</font></td> \n" );
		  out.println( "<br> \n" );
			    		   
		  out.println( "<input type=\"submit\" value=\"Create User!\">  \n" );
		  out.println( "</form> \n" );

	      out.println( "</body> \n" );
	      out.println( "</html> \n" );
	}

}
