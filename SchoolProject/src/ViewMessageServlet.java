

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class ViewMessageServlet
 */
@WebServlet("/ViewMessageServlet")
public class ViewMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger( ViewMessageServlet.class.getName() );

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewMessageServlet() {
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
		
		String date = request.getParameter("DateSent");
		String nameTo = request.getParameter("NameTo");
		String nameFrom = request.getParameter("NameFrom");
		String messageName = request.getParameter("MessageName");
		String message = request.getParameter("Message");


		String sendUserType = request.getParameter("SendUserType");
		String sendUserID = request.getParameter("SendUserID");
		String receiveUserType = request.getParameter("ReceiveUserType");
		String receiveUserID = request.getParameter("ReceiveUserID");
		
		String userTypeSTR = request.getParameter("UserType");
		int userType = Integer.parseInt(userTypeSTR);
		
	    response.setContentType("text/html");  
		PrintWriter out = response.getWriter();
		String title = "View Message!";
		out.println( "<!DOCTYPE html> \n" +
					 "<html> \n" + 
					 "<head> \n" +
					 "<meta charset=\"ISO-8859-1\"> \n" +
					 "<title>" + title + "</title> \n" );
		
		out.println( "<script type = \"text/javascript\"> \n" );

		
	      out.println( "function checkFormCreateMessage(form) " );
	      out.println( "{ \n");
	      
	      out.println( " if( form.MessageName.value == \"\" ){ \n ");
	      out.println( "alert(\"Error: Message Name cannot be blank!\" ); \n");
	      out.println( "form.MessageName.focus() \n" );
	      out.println( "return false; \n");
	      out.println( "} \n" );
	      
	      out.println( " if( form.Message.value == \"\" ){ \n ");
	      out.println( "alert(\"Error: Message cannot be blank!\" ); \n");
	      out.println( "form.Message.focus() \n" );
	      out.println( "return false; \n");
	      out.println( "} \n" );
	      
	      out.println( "return true; \n");
	      out.println( "} \n" );
	      
	      
		
	      out.println("</script> \n");

		out.println("</head> \n" +
				 "<body> \n" );
		
		out.println( "<h3> Your Message! </h3> \n" );
		
		out.println("<table> \n");
		out.println("<tr><td>Date Sent: </td><td>" + date + "</td></tr> \n");
		out.println("<tr><td>Sent To: </td><td>" + nameTo + "</td></tr> \n");
		out.println("<tr><td>Sent From: </td><td>" + nameFrom + "</td></tr> \n");
		out.println("<tr><td>Message Name: </td><td>" + messageName + "</td></tr> \n");
		out.println("<tr><td>Message: </td><td>" + message + "</td></tr> \n");
		out.println("</table> \n");
		
		out.println("<h4> Reply to the Message! </h4> \n");
		
		

		

		
		out.println("<form action=\"CreateMessageServlet\" method=\"post\" onsubmit=\"return checkFormCreateMessage(this)\" > \n");
		
		out.println("<table> \n");
		
		out.println( "<tr><td><font face=\"verdana\" size=\"2px\">Send Message To: </font></td> \n" );
		out.println( "<td><font face=\"verdana\" size=\"2px\">" + nameFrom + "</font></td></tr> \n" );

	    out.println( "<tr><td><font face=\"verdana\" size=\"2px\">Message Name: </font></td> \n" );
	    out.println( "<td><input type=\"text\" name=\"MessageName\" value=\"RE: " + messageName +  "\"></td></tr> \n" );
	    

	    
		out.println("</table> \n");
		
		out.println( " <font face=\"verdana\" size=\"2px\">Message:</font> \n ");
	    out.println( "<br><textarea name = \"Message\" rows=\"4\" cols=\"75\"></textarea> \n ");
		
		out.println("<input type=\"hidden\" name=\"SenderUserType\" value=\"" + sendUserType  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"SenderUserID\" value=\"" + sendUserID  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"ReceiverDropdown\" value=\"" + receiveUserType + " " + receiveUserID  + "\">< \n");

	
		
		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
		
		if( userType == 0 ){
			out.println("<input type=\"hidden\" name=\"StudentID\" value=\"" + request.getParameter("UserID")  + "\"> \n");
		}
		else{
			out.println("<input type=\"hidden\" name=\"TeacherID\" value=\"" + request.getParameter("UserID")  + "\"> \n");

		}
		out.println("<input type=\"hidden\" name=\"UserName\" value=\"" + request.getParameter("UserName")  + "\"> \n");

		
	    out.println("<br><input type=\"submit\" value=\"Send Your Message!\"> \n");
		out.println("</form> \n");
		
		out.println("<form action=\"MailboxServlet\" method=\"post\" > \n");
		
		if( userType == 0 ){
			out.println("<input type=\"hidden\" name=\"StudentID\" value=\"" + request.getParameter("UserID")  + "\"> \n");
		}
		else{
			out.println("<input type=\"hidden\" name=\"TeacherID\" value=\"" + request.getParameter("UserID")  + "\"> \n");

		}
		out.println("<input type=\"hidden\" name=\"UserName\" value=\"" + request.getParameter("UserName")  + "\"> \n");

		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
		
		out.println("<br><input type=\"submit\" value=\"Return to Mailbox!\"> \n");
		out.println("</form> \n");
		
		
		if( userType == 0){
			out.println("<br><form action=\"StudentHomeServlet\" method=\"post\" > \n");
			
			out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
			
			out.println("<br><input type=\"submit\" value=\"Return to Student Home Page!\"> \n");
			out.println("</form> \n");	
		}
		else if( userType == 1 ){
			out.println("<br><form action=\"TeacherHomeServlet\" method=\"post\" > \n");
			
			out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
			out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
			
			out.println("<br><input type=\"submit\" value=\"Return to Teacher Home Page!\"> \n");
			out.println("</form> \n");	
		}

		out.println( "</body> \n" );
		out.println( "</html>\n" );	
		
	}

}
