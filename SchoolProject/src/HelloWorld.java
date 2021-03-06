

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class HelloWorld
 */
@WebServlet("/HelloWorld")
public class HelloWorld extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger( HelloWorld.class.getName() );

    /**
     * @see HttpServlet#HttpServlet()
     */
    public HelloWorld() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost( request, response );
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");  
		
		PrintWriter out = response.getWriter();
		
		try  
		{  
			BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\yc05cl1\\workspace\\SchoolProject\\WebContent\\helloworld.html"));  
			String str;  
			while ((str = in.readLine()) != null) {  
				out.println(str);  
			}  
			in.close();  
		}  
		catch(Exception e)  
		{  
			logger.error("Exception occurred! ", e);
			System.out.println( "cannot get reader: " + e );  
		}  
		
		
	}

}
