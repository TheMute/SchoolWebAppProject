

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class ReportCardServlet
 */
@WebServlet("/ReportCardServlet")
public class ReportCardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger( ReportCardServlet.class.getName() );

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportCardServlet() {
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
		String studentIDstr = request.getParameter("StudentID");
		int studentID = Integer.parseInt(studentIDstr);
		
		Vector<String> classNames = new Vector<String>();
		Vector<Float> counts = new Vector<Float>();
		Vector<Float> checkOffs = new Vector<Float>();
		
		
		final String JDBC_DRIVER="com.mysql.jdbc.Driver";  
		final String DB_URL="jdbc:mysql://localhost:3306/SchoolProject";

		//  Database credentials
		final String USER = "cleung";
		final String PASS = "cleung";
		
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
		RequestDispatcher disp = null;
		
	    
	    try{
	         // Register JDBC driver
	         Class.forName("com.mysql.jdbc.Driver");
	    
	         // Open a connection
	         conn = DriverManager.getConnection(DB_URL,USER,PASS);


		     stmt = conn.prepareStatement( "SELECT ClassName, Count(*) as count, SUM(CheckOff1+CheckOff2+CheckOff3+CheckOff4+CheckOff5) as checkoffs FROM STUDENT A JOIN STUDENTCLASSREL B ON A.STUDENTID=B.STUDENTID "
		    		 +"JOIN CLASS C ON B.CLASSID=C.CLASSID JOIN ASSIGNMENT D ON C.CLASSID=D.CLASSID "
		    		 +"JOIN COMPLETEDASSIGNMENT E ON D.ASSIGNMENTID=E.ASSIGNMENTID "
		    		 +"JOIN GRADEDASSIGNMENT F ON E.COMPLETEDASSIGNMENTID=F.COMPLETEDASSIGNMENTID "
		    		 +"WHERE A.STUDENTID=? GROUP BY CLASSNAME;");
		     stmt.setInt(1, studentID);
		     rs = stmt.executeQuery();
		     while( rs.next() ){
		    	 classNames.add( rs.getString("ClassName"));
		    	 counts.add( rs.getFloat("count"));
		    	 checkOffs.add(rs.getFloat("checkoffs"));
		     }
	     
	         
	    }catch(SQLException se){
	         //Handle errors for JDBC
	    	  System.out.println(" SQLException occurred! ");
	    	  logger.error( "SQL Exception ocurred", se);
	         se.printStackTrace();
	         request.setAttribute("SQLException", se);
	         disp = request.getRequestDispatcher("/SQLExceptionPageServlet"); 
	         disp.forward(request, response);  
	    }catch(Exception e){
	         //Handle errors for Class.forName
	    	  System.out.println("Exception occurred! ");
	    	  logger.error( "Exception ocurred", e);
	         e.printStackTrace();
	         request.setAttribute("Exception", e);
	         disp = request.getRequestDispatcher("/ExceptionPageServlet"); 
	         disp.forward(request, response);  	    
	    }finally{
	         //finally block used to close resources
			 try{
			    if(stmt!=null)
			       stmt.close();
			 }catch(SQLException se2){
				  System.out.println(" SQLException2 occurred! ");
		    	  logger.error( "SQL Exception ocurred", se2);
		    	  se2.printStackTrace();
		         request.setAttribute("SQLException", se2);
		         disp = request.getRequestDispatcher("/SQLExceptionPageServlet"); 
		         disp.forward(request, response);  			 }// nothing we can do
			 try{
			    if(conn!=null)
			    conn.close();
			 }catch(SQLException se){
				 System.out.println(" SQLException4 occurred! ");
		    	 logger.error( "SQL Exception ocurred", se);
				 se.printStackTrace();
		         request.setAttribute("SQLException", se);
		         disp = request.getRequestDispatcher("/SQLExceptionPageServlet"); 
		         disp.forward(request, response);  
			 }//end finally try
	    } //end try
		/*
	    System.out.println("Class Names");
	    for( String s : classNames){
	    	System.out.println(s);
	    }
	    System.out.println("counts");
	    for( Float s : counts){
	    	System.out.println(s);
	    }
	    System.out.println("Check Offs");
	    for( float s : checkOffs){
	    	System.out.println(s);
	    }
	    */
	    
	    
		
		response.setContentType("text/html");  
		PrintWriter out = response.getWriter();
		String title = "Your Report Card!";
		out.println( "<!DOCTYPE html> \n" +
					 "<html> \n" + 
					 "<head> \n" +
					 "<meta charset=\"ISO-8859-1\"> \n" +
					 "<title>" + title + "</title> \n" +
					 "</head> \n" +
					 "<body> \n" );
		
		
		out.println( "<h3> Your Report Card! </h3> \n" );
		
		out.println("<br><table width=\"50%\" border=\"1\"> \n");
		out.println("<tr> \n");
		out.println("<td> Class Name </td> \n");
		out.println("<td> Number of Assignments </td> \n");
		
		out.println("<td> Grade(%) </td> \n");
		out.println("</tr> \n");
		float f = 0;
		for( int i = 0; i < classNames.size(); i++){
			out.println("<tr> \n");
			
			out.println("<td> " + classNames.get(i) + " </td> \n");
			out.println("<td> " + counts.get(i) + " </td> \n");
			
			f = (float) (checkOffs.get(i) / ( 5.00 * counts.get(i) ));
			f *= 100;
			out.println("<td> " + Math.round(f*100.0)/100.0 + "% </td> \n");
			
			out.println("</tr> \n");
		}
		out.println("</table> \n");
		
		out.println("<br><form action=\"StudentHomeServlet\" method=\"post\" > \n");
		
		out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
		out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
		
		out.println("<br><input type=\"submit\" value=\"Return to Student Home Page!\"> \n");
		out.println("</form> \n");
		
		out.println( "</body> \n" );
		out.println( "</html>\n" );	
	}

}
