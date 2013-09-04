

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class ExceptionHandlerServlet
 */
@WebServlet("/ExceptionHandlerServlet")
public class ExceptionHandlerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger( ExceptionHandlerServlet.class.getName() );

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExceptionHandlerServlet() {
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
		/*
		  // Analyze the servlet exception       
	      Throwable throwable = (Throwable)
	      request.getAttribute("javax.servlet.error.exception");
	      Integer statusCode = (Integer)
	      request.getAttribute("javax.servlet.error.status_code");
	      String servletName = (String)
	      request.getAttribute("javax.servlet.error.servlet_name");
	      if (servletName == null){
	         servletName = "Unknown";
	      }
	      String requestUri = (String)
	      request.getAttribute("javax.servlet.error.request_uri");
	      if (requestUri == null){
	         requestUri = "Unknown";
	      }

	      // Set response content type
	      response.setContentType("text/html");
	 
	      PrintWriter out = response.getWriter();
		  String title = "Error/Exception Information";
	      String docType =
	      "<!doctype html public \"-//w3c//dtd html 4.0 " +
	      "transitional//en\">\n";
	      out.println(docType +
	        "<html>\n" +
	        "<head><title>" + title + "</title></head>\n" +
	        "<body bgcolor=\"#f0f0f0\">\n");
	        */

	      /*
	      if (throwable == null && statusCode == null){
	         out.println("<h2>Error information is missing</h2>");
	         out.println("Please return to the <a href=\"" + 
	           response.encodeURL("http://localhost:8080/MyFirstServlet/index.jsp") + 
	           "\">Home Page</a>.");
	      }else if (statusCode != null){
	         out.println("The status code : " + statusCode);
	      }else{
	         out.println("<h2>Error information</h2>");
	         out.println("Servlet Name : " + servletName + 
	                             "</br></br>");
	         out.println("Exception Type : " + 
	                             throwable.getClass( ).getName( ) + 
	                             "</br></br>");
	         out.println("The request URI: " + requestUri + 
	                             "<br><br>");
	         out.println("The exception message: " + 
	                                 throwable.getMessage( ));
	      }
	      */
	      
	      /*
	      out.println("Exception Occurred!");
	      out.println( "<br><a href=\"http://localhost:8080/SchoolProject/\">Return to Homepage</a> \n");
  		
	      out.println("</body>");
	      out.println("</html>");	
	      */
		processError(request, response);
	   
	}
	private void processError(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// Analyze the servlet exception
		Throwable throwable = (Throwable) request
				.getAttribute("javax.servlet.error.exception");
		Integer statusCode = (Integer) request
				.getAttribute("javax.servlet.error.status_code");
		String servletName = (String) request
				.getAttribute("javax.servlet.error.servlet_name");
		if (servletName == null) {
			servletName = "Unknown";
		}
		String requestUri = (String) request
				.getAttribute("javax.servlet.error.request_uri");
		if (requestUri == null) {
			requestUri = "Unknown";
		}

		// Set response content type
	      response.setContentType("text/html");

	      PrintWriter out = response.getWriter();
	      out.write("<html><head><title>Exception/Error Details</title></head><body>");
	      if(statusCode != 500){
	    	  out.write("<h3>Error Details</h3>");
	    	  out.write("<strong>Status Code</strong>:"+statusCode+"<br>");
	    	  out.write("<strong>Requested URI</strong>:"+requestUri);
	      }else{
	    	  out.write("<h3>Exception Details</h3>");
	    	  out.write("<ul><li>Servlet Name:"+servletName+"</li>");
	    	  out.write("<li>Exception Name:"+throwable.getClass().getName()+"</li>");
	    	  out.write("<li>Requested URI:"+requestUri+"</li>");
	    	  out.write("<li>Exception Message:"+throwable.getMessage()+"</li>");
	    	  out.write("</ul>");
	      }

	      out.write("<br><br>");
	      out.write("<a href=\"http://localhost:8080/SchoolProject/\">Home Page</a>");
	      out.write("</body></html>");
	}

}
