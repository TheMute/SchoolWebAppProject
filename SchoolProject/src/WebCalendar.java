import java.io.*;
import java.sql.*;
import java.util.*;
import java.text.*;
import java.lang.Integer;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class WebCalendar
 */
@WebServlet("/WebCalendar")
public class WebCalendar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	// these parameters may be changed to suit the owner/user:
	  protected boolean viewOnly = false; // true to disallow changes (for public viewing only); false to allow users to add/modify/delete events
	  protected String eventsDatabase = "jdbc:mysql://localhost:3306/SchoolProject"; // location of the MySQL database file containing the events
	  protected String mySQLUser = "cleung";
	  protected String mySQLPass = "cleung";
	  protected String secretPassPhrase = "KilroyWasHere";
	  protected String authMessage = "Please see the owner of this calendar to get the secret phrase.";
	  protected int cookieMaxAge = 2; // sets lifetime of cookie in weeks
	  protected String titleBar = "Your Calendar "; // label shown on the top bar of the Calendar
	  protected String cTFontsColor = "#FFFF00";  // yellow
	  protected String cTopBarColor = "#0000AA";  // dark blue
	  protected String cDayBarColor = "#0000FF";  // blue
	  protected String cOutDayColor = "#0000AA";  // dark blue
	  protected String cTodaysColor = "#E0E0E0";  // light gray
	  protected String cBGCellColor = "#FFFFFF";  // white
	  protected String cDtFontColor = "#000000";  // black
	  protected SimpleDateFormat dfTimeField = new SimpleDateFormat("HH:mm"); // use "h:mma" for AM/PM format
	  protected SimpleDateFormat dfDateField = new SimpleDateFormat("M/d/yy");  // use "dd-MM-yyyy" for European format
	  protected String time0000 = dfTimeField.format(new java.util.Date(25200000));  // 00:00 (beginning of day in local time format)
	  protected String time2359 = dfTimeField.format(new java.util.Date(111540000)); // 23:59 (end of day in local time format)
	  protected SimpleDateFormat dfMonthYear = new SimpleDateFormat("MMMM yyyy");  // used for the title bar
	  protected SimpleDateFormat dfMonthName = new SimpleDateFormat("MMMM");       // used for next/last month navigation

	  // these parameters should not be changed, or things may break:
	  String thisServletURI = null;
	  protected String mySqlJdbcDriver = "org.gjt.mm.mysql.Driver"; // location of jdbc driver in the classpath
	  protected SimpleDateFormat dfMySQLDate = new SimpleDateFormat("yyyy-MM-dd"); // matches format used by MySQL database
	  protected SimpleDateFormat dfMySQLTime = new SimpleDateFormat("HH:mm");       //format used by MySQL database
	  protected SimpleDateFormat dfDayOfMonth = new SimpleDateFormat("d");         // used for writing date numerals in the calendar
	  
	  
	  static String userID;
	  static String user;
	  static String code;
	  	  
	//*******************
	//default entry point
	//*******************
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WebCalendar() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void doGet(HttpServletRequest request,HttpServletResponse response)
    	    throws ServletException, IOException {
    	
    		userID = request.getParameter("UserID");
    		user = request.getParameter("user");
    		code = request.getParameter("code");
    		
    		System.out.println("IN DOGET()");
    		//System.out.println("userid: " + userID);
    		System.out.println("user static: " + this.user);
    		System.out.println("user param: " + request.getParameter("user"));
    		System.out.println("userid static: " + this.userID);
    		System.out.println("userid param: " + request.getParameter("UserID"));
    		System.out.println("email: " + request.getParameter("Email"));
    		System.out.println("password: " + request.getParameter("Password"));
    		
    		
    	    response.setContentType("text/html");
    	    PrintWriter out = response.getWriter();
    	    thisServletURI = "/SchoolProject" + request.getServletPath();


    	    Calendar thisMonth = Calendar.getInstance();
    	    java.util.Date thisDate = new java.util.Date();
    	    
    	    // look for an input date request (default = today)
    	    if (request.getParameter("date") != null) {
    	      try {
    	        thisDate = dfDateField.parse(request.getParameter("date"));
    	        thisMonth.setTime(thisDate);
    	      } catch (ParseException e) {
    	        out.println("Unable to parse date parameter. Using date=" + dfDateField.format(thisMonth.getTime()) + "<br>");
    	      }
    	      
    	    }
    	    
    	    // set the calendar to the first day of the month
    	    thisMonth.set(Calendar.DAY_OF_MONTH,1);
    	    
    	    // select the correct type of output and send it:
    	    out.println(htmlHeader());
    	    
    	    if (request.getParameter("form") == null) {
    	      out.println(tableHeader(thisMonth, request.getParameter("user"), request.getParameter("UserID"), request.getParameter("Email"), request.getParameter("Password")));
    	      out.println(tableCells(thisMonth, request.getParameter("user"), request.getParameter("UserID"), request.getParameter("Email"), request.getParameter("Password")));
    	      out.println(tableFooter(thisMonth, request.getParameter("user"), request.getParameter("UserID"), request.getParameter("Email"), request.getParameter("Password")));
    	    }
    	    else if (request.getParameter("form").equals("help")) {
    	      out.println(instructions());
    	    }
    	    else if (viewOnly) {
    	      out.println(declineRequest());
    	    }    
    	    else if (allowEdit(request,response)) {  
    	      if (request.getParameter("form").equals("new")) {
    	        out.println(newEventForm(request.getParameter("value"), request.getParameter("user"),  request.getParameter("UserID"), request.getParameter("Email"), request.getParameter("Password")  ));
    	      }
    	      else if (request.getParameter("form").equals("revise")) {
    	        out.println(reviseEventForm(request.getParameter("value"), request.getParameter("user"),  request.getParameter("UserID"), request.getParameter("Email"), request.getParameter("Password") ));
    	      }
    	    }
    	    else out.println(promptForPassword(request));
    	    out.println(htmlFooter());
    	    
    		if( request.getParameter("user").charAt(0) == 'S'){
    			out.println("<br><form action=\"StudentHomeServlet\" method=\"post\" > \n");
    			
    			out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
    			out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
    			
    			out.println("<br><input type=\"submit\" value=\"Return to Student Home Page!\"> \n");
    			out.println("</form> \n");	
    		}
    		else if( request.getParameter("user").charAt(0) == 'T'){
    			out.println("<br><form action=\"TeacherHomeServlet\" method=\"post\" > \n");
    			
    			out.println("<input type=\"hidden\" name=\"Email\" value=\"" + request.getParameter("Email")  + "\"> \n");
    			out.println("<input type=\"hidden\" name=\"Password\" value=\"" + request.getParameter("Password")  + "\"> \n");
    			
    			out.println("<br><input type=\"submit\" value=\"Return to Teacher Home Page!\"> \n");
    			out.println("</form> \n");	
    		}
    	    
    	    return;
    	  }
    	  
    	  String htmlHeader() {
      		System.out.println("IN HTMLHEADER()");

    	    return "<HTML>"
    	     + "<head>"
    	     + "<meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1'>"
    	     + "<meta name='Author' content='Chuck Wight'>"
    	     + "<title>WebCalendar</title>"
    	     + "<SCRIPT LANGUAGE=JavaScript>"
    	     + "function PopupWindow(url,form,value,user,UserID,code,SetCookie,Email,Password)"
    	     + "{window.open(url+'?form='+form+'&value='+value+'&user='+user+'&UserID='+UserID+'&code='+code+'&SetCookie='+SetCookie+'&Email='+Email+'&Password='+Password,'EventControlPanel','width=370,height=370,dependent,resizable');}"
    	     + "</SCRIPT>"
    	     + "</head>";
    	  }
    	  
    	  String instructions() {
      		System.out.println("IN INSTRUCTIONS()");

    	    StringBuffer rv = new StringBuffer("<body>");
    	    rv.append("<h3>WebCalendar Program</h3>"
    	     + "<FONT SIZE=-1><OL><LI>To view the previous or next month, click the arrows in the corners of the calendar."
    	     + "<LI>To add a new event to the calendar, click the date numeral for the date desired. You may be prompted for a "
    	     + "username and password. A popup form should appear. All fields are required except 'Notes' and 'Description'. "
    	     + "Depending on your browser settings, you might have to 'Refresh' the main calendar page to see the change."
    	     + "<LI>If you schedule an event that conflicts with another event, you will receive a warning, but the conflicting event will be scheduled anyway. "
    	     + "It is the user's responsiblity to resolve conflicts."
    	     + "<LI>To modify or delete an event, click the blue time numerals for the event you want to change."
    	     + "</OL><HR>"
    	     + "Copyright &copy; 2001, <a href=mailto:Chuck.Wight@utah.edu>Chuck Wight</a>.<BR>"
    	     + "The source code for this Java servlet is distributed as a <a href=http://sourceforge.net/projects/javawebcalendar/>free download</a>, "
    	     + " with NO WARRANTY, under the terms of the "
    	     + "<a href='http://www.gnu.org/copyleft/gpl.html' onClick=javascript:opener.document.location='http://www.gnu.org/copyleft/gpl.html';window.close();>GNU General Public License</a>.<BR>"
    	     + "</FONT>");
    	    return rv.toString();
    	  }
    	  
    	  String declineRequest() {
      		System.out.println("IN DECLINEREQUEST()");

    	  StringBuffer rv = new StringBuffer("<body>");
    	  rv.append("<h3>Sorry!</h3>"
    	   + "The owner/administrator of this WebCalendar servlet has configured it so that you may "
    	   + "view the events, but not insert, edit or delete them from the database.  If you are "
    	   + "an authorized user of this calendar, then there is probably another version of the "
    	   + "servlet located in a password-protected directory.  You should use the other servlet "
    	   + "to make any desired changes.");
    	   rv.append("<FORM><INPUT TYPE=BUTTON VALUE='  OK  ' onClick=javascript:window.close();></FORM>");
    	   return rv.toString();
    	  }
    	  
    	  String tableHeader(Calendar thisMonth, String userArg, String userIDArg, String Email, String Password) {
      		System.out.println("IN TABLEHEADER()");

    	    Calendar nextMonth = (Calendar)thisMonth.clone(); nextMonth.add(Calendar.MONTH,+1);
    	    Calendar lastMonth = (Calendar)thisMonth.clone(); lastMonth.add(Calendar.MONTH,-1);
    	    return "<body>"
    	     + "<TABLE BORDER=1 CELLSPACING=0 CELLPADDING=0 WIDTH=100%>"
    	     + "  <TR>"
    	     + "  <TD BGCOLOR=" + cTopBarColor + " ALIGN=CENTER COLSPAN=7>"
    	     + "    <TABLE WIDTH=100% BORDER=0 CELLSPACING=0 CELLPADDING=0>"
    	     + "    <TR ALIGN=CENTER>"
    	     + "      <TD><A HREF=" + thisServletURI + "?date=" + dfDateField.format(lastMonth.getTime()) + "&user=" + userArg + "&UserID=" + userIDArg + "&code=" + secretPassPhrase + "&SetCookie=" + "true" + "&Email=" + Email + "&Password=" + Password + "><FONT SIZE=-1 COLOR=" + cTFontsColor + "><&#151;&nbsp;" + dfMonthName.format(lastMonth.getTime()) + "</FONT></A></TD>"
    	     + "      <TD><FONT SIZE=+1 COLOR=" + cTFontsColor + "><B>" + titleBar + dfMonthYear.format(thisMonth.getTime()) + "</B></FONT></TD>"
    	     + "      <TD><A HREF=" + thisServletURI + "?date=" + dfDateField.format(nextMonth.getTime()) + "&user=" + userArg + "&UserID=" + userIDArg + "&code=" + secretPassPhrase + "&SetCookie=" + "true" + "&Email=" + Email + "&Password=" + Password + "><FONT SIZE=-1 COLOR=" + cTFontsColor + ">" + dfMonthName.format(nextMonth.getTime()) + "&nbsp;&#151;></FONT></A></TD>"
    	     + "    </TR>"
    	     + "    </TABLE>"
    	     + "  </TD>"
    	     + "  </TR>"
    	     + "  <TR ALIGN=CENTER BGCOLOR=" + cDayBarColor + ">"
    	     + "  <TD WIDTH=14%><FONT COLOR=" + cTFontsColor + "><B>Sun</B></FONT></TD>"
    	     + "  <TD WIDTH=14%><FONT COLOR=" + cTFontsColor + "><B>Mon</B></FONT></TD>"
    	     + "  <TD WIDTH=14%><FONT COLOR=" + cTFontsColor + "><B>Tue</B></FONT></TD>"
    	     + "  <TD WIDTH=14%><FONT COLOR=" + cTFontsColor + "><B>Wed</B></FONT></TD>"
    	     + "  <TD WIDTH=14%><FONT COLOR=" + cTFontsColor + "><B>Thu</B></FONT></TD>"
    	     + "  <TD WIDTH=14%><FONT COLOR=" + cTFontsColor + "><B>Fri</B></FONT></TD>"
    	     + "  <TD WIDTH=14%><FONT COLOR=" + cTFontsColor + "><B>Sat</B></FONT></TD>"
    	     + "  </TR>";
    	  }
    	  
    	  String tableCells(Calendar thisMonth, String userArg, String userIDArg, String Email, String Password) {
      		System.out.println("IN TABLECELLS()");

    	    StringBuffer returnValue = new StringBuffer();
    	    
    	    // record the current value of the field Calendar.MONTH
    	    int iThisMonth = thisMonth.get(Calendar.MONTH);
    	    boolean firstCell = true;

    	    Calendar today = Calendar.getInstance(); //used to highlight today's date cell in the calendar
    	    Calendar endOfMonth = (Calendar)thisMonth.clone();
    	    endOfMonth.set(Calendar.DATE,endOfMonth.getActualMaximum(Calendar.DATE));
    	    endOfMonth.add(Calendar.DATE,7-endOfMonth.get(Calendar.DAY_OF_WEEK));

    	    // set calendar to first day of the first week (may move back 1 month)
    	    thisMonth.add(Calendar.DATE,1-thisMonth.get(Calendar.DAY_OF_WEEK));

    	    // get a recordset of this month's events, including extra days at beginning and end
    	    String startDate = dfMySQLDate.format(thisMonth.getTime());
    	    String endDate = dfMySQLDate.format(endOfMonth.getTime());
    	    ResultSet rsEvents = null;
    	    
    	    try {
   	          Class.forName("com.mysql.jdbc.Driver");
    	      Connection conn = DriverManager.getConnection(eventsDatabase,mySQLUser,mySQLPass);
    	      Statement stmt = conn.createStatement();
    	      
    	      // select only events for this calendar display:
    	      String sqlQueryString = "SELECT * FROM Events "
    	       + "WHERE TO_DAYS(Sdate) >= TO_DAYS('" + startDate + "')"
    	       + "  AND TO_DAYS(Sdate) <= TO_DAYS('" + endDate + "') AND User='" + userID + "'"
    	       + " ORDER BY Sdate,Edate";
    	      rsEvents = stmt.executeQuery(sqlQueryString);
    	      if (!rsEvents.next()) rsEvents = null;     // no events in the whole month's display
    	    } catch(Exception e) {
    	      return e.getMessage();
    	    }
    	    
    	      
    	    do {
    	      returnValue.append("<TR BORDER=1 VALIGN=TOP>"); 
    	      for (int i=0; i < 7; i++) {
    	        returnValue.append("<TD HEIGHT=100 WIDTH=\"14%\"");
    	        if (thisMonth.get(Calendar.MONTH) != iThisMonth) returnValue.append(" BGCOLOR=" + cOutDayColor);  // dark background for out-of-month days
    	        else if (dfDateField.format(thisMonth.getTime()).equals(dfDateField.format(today.getTime()))) returnValue.append(" BGCOLOR=" + cTodaysColor); // grey background for today's date
    	        else returnValue.append(" BGCOLOR=" + cBGCellColor);  // default white background
    	        returnValue.append("><A HREF=# onClick=javascript:PopupWindow('" + thisServletURI + "','new','" + dfDateField.format(thisMonth.getTime()) + "','" + userArg + "','"+ userIDArg + "','" + secretPassPhrase + "','" + "true','"+Email+"','"+Password+"');><FONT COLOR=" + cDtFontColor + " SIZE=-1><B>" + dfDayOfMonth.format(thisMonth.getTime()) + "</B></FONT></A>");
    	        //returnValue.append("><A HREF=# onClick=javascript:PopupWindow('" + thisServletURI + "','new','" + dfDateField.format(thisMonth.getTime()) + "', '99');><FONT COLOR=" + cDtFontColor + " SIZE=-1><B>" + dfDayOfMonth.format(thisMonth.getTime()) + "</B></FONT></A>");
  
    	        returnValue.append(todaysEvents(rsEvents,dfMySQLDate.format(thisMonth.getTime()), userArg, userIDArg, Email, Password )); // write out the cell contents
    	          
    	        if (firstCell) {
    	            returnValue.append("<CENTER><FORM><INPUT TYPE=BUTTON VALUE='Help' onClick=javascript:PopupWindow('" + thisServletURI + "','help','yes');></FORM></CENTER>");
    	          firstCell = false;
    	        }
    	        returnValue.append("</TD>");
    	        thisMonth.add(Calendar.DATE,1);
    	      }
    	      returnValue.append("</TR>");
    	    } while(thisMonth.getTime().before(endOfMonth.getTime()));

    	    // restore date to first of this month:
    	    thisMonth.add(Calendar.MONTH,-1);
    	    thisMonth.set(Calendar.DATE,1);
    
    	    
    	    return returnValue.toString();
    	  }
    	  
    	  String todaysEvents(ResultSet rsEvents, String today, String userArg, String userIDArg, String Email, String Password) {
      		//System.out.println("IN TODAYSEVENTS()");

    	    try {
    	      if (rsEvents==null || rsEvents.isAfterLast()) return "";   // no more events this month or empty ResultSet
    	      StringBuffer returnValue = new StringBuffer("<font size=-2>");
    	      while (dfMySQLDate.format(rsEvents.getDate("Sdate")).equals(today)) {
    	        java.util.Date start = rsEvents.getTime("Sdate");
    	        java.util.Date end = rsEvents.getTime("Edate");
    	        String timeSpan = null;
    	        if (dfTimeField.format(start).equals(time0000) && dfTimeField.format(end).equals(time0000)) timeSpan = "TODAY:";
    	        else if (dfTimeField.format(start).equals(time0000) && dfTimeField.format(end).equals(time2359)) timeSpan = "ALL DAY:";
    	        else timeSpan = dfTimeField.format(rsEvents.getTime("Sdate")) + "-" + dfTimeField.format(rsEvents.getTime("Edate"));

    	        returnValue.append("<br><A HREF=# onClick=javascript:PopupWindow('" + thisServletURI + "','revise','" + rsEvents.getString("EventID") + "','" + userArg + "','"+ userIDArg + "','" + secretPassPhrase + "','" + "true','"+Email+"','"+Password+"');><font color=#0000FF>" + timeSpan + "</font></a>&nbsp;");
    	        //returnValue.append("><A HREF=# onClick=javascript:PopupWindow('" + thisServletURI + "','new','" + dfDateField.format(thisMonth.getTime()) + "','" + userArg + "','"+ userIDArg + "','" + secretPassPhrase + "','" + "true','"+Email+"','"+Password+"');><FONT COLOR=" + cDtFontColor + " SIZE=-1><B>" + dfDayOfMonth.format(thisMonth.getTime()) + "</B></FONT></A>");

    	        if(rsEvents.getBoolean("Flagged")) returnValue.append("<FONT COLOR=#FF0000>"); // write Desription field in red if flagged
    	        returnValue.append(rsEvents.getString("Description"));
    	        if(rsEvents.getBoolean("Flagged")) returnValue.append("</FONT>"); // returns red font color to default
    	        if(!rsEvents.next()) return returnValue.append("</font>").toString(); // encountered end of events
    	      }
    	      return returnValue.append("</font>").toString();
    	    } 
    	    catch (SQLException e) {
    	      return e.getMessage();
    	    }
    	  }
    	      
    	  String tableFooter(Calendar thisMonth, String userArg, String userIDArg, String Email, String Password) {
      		System.out.println("IN TABLEFOOTER()");

    	    Calendar nextMonth = (Calendar)thisMonth.clone(); nextMonth.add(Calendar.MONTH,+1);
    	    Calendar lastMonth = (Calendar)thisMonth.clone(); lastMonth.add(Calendar.MONTH,-1);

    	    return "<TR>"
    	     + "  <TD BGCOLOR=" + cTopBarColor + " ALIGN=CENTER COLSPAN=7>"
    	     + "    <TABLE WIDTH=100% BORDER=0 CELLSPACING=0 CELLPADDING=0>"
    	     + "    <TR ALIGN=CENTER>"
    	     + "      <TD><A HREF=" + thisServletURI + "?date=" + dfDateField.format(lastMonth.getTime()) + "&user=" + userArg + "&UserID=" + userIDArg + "&code=" + secretPassPhrase + "&SetCookie=" + "true" + "&Email=" + Email + "&Password=" + Password + "><FONT SIZE=-1 COLOR=" + cTFontsColor + "><&#151;&nbsp;" + dfMonthName.format(lastMonth.getTime()) + "</FONT></A></TD>"
    	     + "      <TD><A HREF=" + thisServletURI + "?user=" + userArg + "&UserID=" + userIDArg + "&code=" + secretPassPhrase + "&SetCookie=" + "true" + "&Email=" + Email + "&Password=" + Password + "><FONT SIZE=-1 COLOR=" + cTFontsColor + "><B>Go To Current Month</B></FONT></A></TD>"
    	     + "      <TD><A HREF=" + thisServletURI + "?date=" + dfDateField.format(nextMonth.getTime()) + "&user=" + userArg + "&UserID=" + userIDArg + "&code=" + secretPassPhrase + "&SetCookie=" + "true" + "&Email=" + Email + "&Password=" + Password + "><FONT SIZE=-1 COLOR=" + cTFontsColor + ">" + dfMonthName.format(nextMonth.getTime()) + "&nbsp;&#151;></FONT></A></TD>"
    	     + "    </TR>"
    	     + "    </TABLE>"
    	     + "  </TD>"
    	     + "</TR>"
    	     + "<TR>"
    	     + "</TABLE>"
    	     + "<FONT SIZE=-2>Copyright &copy; 2001, <a href=mailto:Chuck.Wight@utah.edu>Chuck Wight</a>. "
    	     + "Distributed as a <a href=http://sourceforge.net/projects/javawebcalendar/>free download</a> "
    	     + "under the terms of the <a href=http://www.gnu.org/copyleft/gpl.html>GNU General Public License</a>.</FONT>";
    	  }
    	  
    	  String htmlFooter() {
      		System.out.println("IN HTMLFOOTER()");

    	    return "</body></HTML>";
    	  }
    	  
    	  boolean allowEdit(HttpServletRequest request, HttpServletResponse response) {
      		System.out.println("IN ALLOWEDIT()");
      		System.out.println("static: " + this.user + " " + this.userID);
      		
      		System.out.println(" user: " + request.getParameter("user"));
      		System.out.println(" code: " + request.getParameter("code"));
      		System.out.println(" SetCookie: " + request.getParameter("SetCookie"));
      		
    	    HttpSession session = request.getSession(true);
    	    String codePhrase = code;//(String)session.getAttribute("code");
    	    String user;
    	    if (codePhrase != null)
    	      if (codePhrase.equals(String.valueOf(secretPassPhrase.hashCode())))
    	        return true;

    	    Cookie cookies[] = request.getCookies();
    	    if (cookies != null) {
    	      for (int i = 0; i < cookies.length; i++) {

    	        codePhrase = cookies[i].getValue(); // hash code
    	        if (codePhrase.startsWith(Integer.toString(secretPassPhrase.hashCode()))) {
    	          user = codePhrase.substring(Integer.toString(secretPassPhrase.hashCode()).length());
    	          session.setAttribute("user",user);
    	          session.setAttribute("code",Integer.toString(secretPassPhrase.hashCode()));
    	          return true;
    	        }
    	      }
    	    }

    	    codePhrase = request.getParameter("code");
    	    user = request.getParameter("user");
    	    if (codePhrase != null)
    	      if (codePhrase.equals(secretPassPhrase) && user != null) {
    	        session.setAttribute("code",String.valueOf(codePhrase.hashCode()));
    	        session.setAttribute("user",user);
    	        if (request.getParameter("SetCookie") != null) {
    	          Cookie myCookie = new Cookie("WebCalendar",codePhrase.hashCode()+user);
    	          myCookie.setMaxAge(604800*cookieMaxAge);  // converts from weeks to seconds
    	          response.addCookie(myCookie);
    	        }
    	        return true;
    	      }
    	  
    	    return false;
    	  }

    	  String promptForPassword(HttpServletRequest request) {
      		System.out.println("IN PROMPTFORPASSWORD()");

    	    return "<h3>Authentication</h3>"
    	   + "<FORM METHOD=GET ACTION='" + thisServletURI + "'>"
    	         + authMessage + "<hr><br>"
    	   + "<input type=\"hidden\" name=\"user\" value=\"" + userID  + "\">"
    	   + "<input type=\"hidden\" name=\"code\" value=\"" + secretPassPhrase  + "\">"
    	   //+ "<input type=\"hidden\" name=\"value\" value=\"" + "true"  + "\">"
    	   //+ "Please type your name:<INPUT TYPE=TEXT SIZE=10 NAME='user'><BR>"
    	   //      + "Enter the secret phrase:<INPUT TYPE=PASSWORD NAME='code'><BR>" 
    	   + "<INPUT TYPE=CHECKBOX NAME='SetCookie' VALUE='true'>Check here to accept a cookie for automatic login from this location.<BR>"
    	         //+ "<INPUT TYPE=HIDDEN NAME='form' VALUE='" + request.getParameter("form") + "'>"
    	   + "<INPUT TYPE=HIDDEN NAME='value' VALUE='" + request.getParameter("value") + "'>"
    	   + "<INPUT TYPE=SUBMIT VALUE='Go to Your Calendar'>"
    	   + "</FORM>";
    	  }
    	  
    	  String newEventForm(String date, String userArg, String userIDArg, String Email, String Password) {
      		System.out.println("IN NEWEVENTFORM()");

      		System.out.println(userArg + " " + userIDArg);
      		
    	    StringBuffer returnValue = new StringBuffer();

    	    // print blank popup form for entering a new event:
    	    returnValue.append("<body onLoad=window.focus()>"
    	     + "<H3>Control Panel</H3>"
    	     + "<FORM METHOD=POST ACTION='" + thisServletURI + "'>"
    	     + "<INPUT TYPE=HIDDEN NAME='UserRequest' VALUE='New'>"
    	     + "  <TABLE BORDER=0 CELLSPACING=0>"
    	     + "  <TR><TD>Date:</TD><TD><INPUT TYPE=TEXT SIZE=8 NAME=EventDate VALUE=" + date + "></TD></TR>"
    	     + "  <TR><TD ROWSPAN=3 VALIGN=TOP>Time: <BR><FONT SIZE=-2>(e.g., " + dfTimeField.format(new java.util.Date(4800000)) + ")</FONT></TD>"
    	     + "  <TD><INPUT TYPE=RADIO NAME=EventType VALUE=REGULAR CHECKED>"
    	     + "    Start: <INPUT TYPE=TEXT SIZE=8 NAME=STime>"
    	     + "    End: <INPUT TYPE=TEXT SIZE=8 NAME=ETime></TD></TR>"
    	     + "  <TR><TD><INPUT TYPE=RADIO NAME=EventType VALUE=ALLDAY>All day (" + dfTimeField.format(new java.util.Date(25200000)) + " - " + dfTimeField.format(new java.util.Date(111540000)) + ")</TD></TR>"
    	     + "  <TR><TD><INPUT TYPE=RADIO NAME=EventType VALUE=SPECIAL>No specific time (e.g., birthday or holiday)</TD></TR>"
    	     + "  <TR><TD>Description:</TD><TD><INPUT TYPE=TEXT SIZE=34 NAME=Description></TD></TR>"
    	     + "  <TR><TD></TD><TD><INPUT TYPE=CHECKBOX NAME=Flagged VALUE='true'>Display the description in red.</TD></TR>"
    	     + "  <TR><TD VALIGN=TOP>Notes:<br><FONT SIZE=-2>(can only be viewed in this window)</FONT></TD><TD><TEXTAREA NAME=Notes ROWS=6 COLS=32 WRAP=SOFT></TEXTAREA></TD></TR>"
    	     + "  <TR><TD COLSPAN=2>"
    	     + "  <INPUT TYPE=SUBMIT VALUE='Create This Event'>"
    	     + "  </TD></TR><TR><TD COLSPAN=2>"    
    	     + "  <FONT SIZE=-2>You may have to click the \"Refresh\" button on your browser to view the changes.</FONT>"    
    	     + "  </TD></TR>"    
    	     + "  </TABLE>"
    	     + "<input type=\"hidden\" name=\"user\" value=\"" + userArg  + "\">"
      	     + "<input type=\"hidden\" name=\"code\" value=\"" + secretPassPhrase  + "\">"
      	     + "<input type=\"hidden\" name=\"UserID\" value=\"" + userIDArg  + "\">"
	  	     + "<input type=\"hidden\" name=\"SetCookie\" value=\"" + "true"  + "\">"
	  	     + "<input type=\"hidden\" name=\"Email\" value=\"" + Email  + "\">"
		     + "<input type=\"hidden\" name=\"Password\" value=\"" + Password  + "\">"
	     
      	     
    	     + "</FORM>");
    	    
    	    return returnValue.toString();
    	  }
    	  
    	  String reviseEventForm(String eventID, String userArg, String userIDArg, String Email, String Password) {
      		System.out.println("IN REVISEEVENTFORM()");

    	    String date = null;
    	    String sTime = null;
    	    String eTime = null;
    	    String description = null;
    	    String notes = null;
    	    String user = null;
    	    boolean flagged = false;
    	    
    	    // retrieve the existing data for the selected event
    	    try {
   	          Class.forName("com.mysql.jdbc.Driver");
    	      Connection conn = DriverManager.getConnection(eventsDatabase,mySQLUser,mySQLPass);
    	      Statement stmt = conn.createStatement();
    	      String sqlQueryString = "SELECT * FROM Events WHERE EventID=" + eventID;
    	      ResultSet rs = stmt.executeQuery(sqlQueryString);
    	      if (rs.next()) {
    	        date = dfDateField.format(rs.getDate("Sdate"));
    	        sTime = dfTimeField.format(rs.getTime("Sdate"));
    	        eTime = dfTimeField.format(rs.getTime("Edate"));
    	        description = rs.getString("Description");
    	        flagged = rs.getBoolean("Flagged");
    	        notes = rs.getString("Notes");
    	        user = rs.getString("User");
    	      }
    	    } catch (Exception e) {
    	      return e.getMessage();
    	    }
    	    
    	    // print a pre-loaded form for revising an existing event:
    	    StringBuffer returnValue = new StringBuffer();    
    	    returnValue.append("<body onLoad=window.focus()>"
    	     + "<H3>Control Panel</H3>"
    	     + "<FORM METHOD=POST ACTION='" + thisServletURI + "'>"
    	     + "<INPUT TYPE=HIDDEN NAME='UserRequest' VALUE='Revise'>"
    	     + "<INPUT TYPE=HIDDEN NAME='EventID' VALUE=" + eventID + ">"
    	     + "  <TABLE BORDER=0 CELLSPACING=0>"
    	     + "  <TR><TD>Date:</TD><TD>"
    	     + "    <TABLE WIDTH=100% BORDER=0><TR><TD>"
    	     + "      <INPUT TYPE=TEXT SIZE=8 NAME='EventDate' VALUE='" + date + "'></TD>"
    	     + "      <TD ALIGN=RIGHT><FONT SIZE=-2>This event was entered by " + user + "</FONT>"
    	     + "      </TD></TR></TABLE>"
    	     + "  </TD></TR>"
    	     + "<TR><TD ROWSPAN=3 VALIGN=TOP>Time: <BR>"
    	     + "<FONT SIZE=-2>(e.g., " + dfTimeField.format(new java.util.Date(91200000))
    	     + ")</FONT></TD>"
    	     + "<TD><INPUT TYPE=RADIO NAME=EventType VALUE=REGULAR CHECKED>"
    	     + "Start: <INPUT TYPE=TEXT SIZE=8 NAME=STime VALUE='" + sTime + "'>"
    	     + "End: <INPUT TYPE=TEXT SIZE=8 NAME=ETime VALUE='" + eTime + "'></TD></TR>"
    	     + "  <TR><TD><INPUT TYPE=RADIO NAME=EventType VALUE=ALLDAY>"
    	     + "All day (" + time0000 + " - " + time2359 + ")</TD></TR>"
    	     + "  <TR><TD><INPUT TYPE=RADIO NAME=EventType VALUE=SPECIAL>"
    	     + "No specific time (e.g., birthday or holiday)</TD></TR>"
    	     + "  <TR><TD>Description:</TD><TD><INPUT TYPE=TEXT SIZE=34 NAME=Description VALUE=\"" 
    	     + removeDoubleQuotes(description) + "\"><TD></TR>"
    	     + "<TR><TD></TD><TD><INPUT TYPE=CHECKBOX NAME=Flagged VALUE='true'");
    	    if (flagged) returnValue.append(" CHECKED><FONT COLOR=#FF0000");
    	    returnValue.append(">Display the description in red.</FONT></TD></TR>"
    	     + "<TR><TD VALIGN=TOP>Notes:<br><FONT SIZE=-2>(can only be viewed in this window)"
    	     + "</FONT></TD><TD><TEXTAREA NAME=Notes ROWS=6 COLS=32 WRAP=SOFT>" 
    	     + notes + "</TEXTAREA></TD></TR>"
    	     + "  </TD></TR><TR><TD COLSPAN=2>"
    	     + "  <TABLE BORDER=0 CELLSPACING=0><TR>"
    	     + "    <TD><INPUT TYPE=SUBMIT VALUE='Modify'></TD>"
    	     + "    <TD><INPUT TYPE=SUBMIT VALUE='Save New' OnClick=UserRequest.value='New'></TD>"
    	     + "    <TD><INPUT TYPE=SUBMIT VALUE='Delete' OnClick=UserRequest.value='Delete'></TD>"
    	     
    	     + "<input type=\"hidden\" name=\"user\" value=\"" + userArg  + "\">"
      	     + "<input type=\"hidden\" name=\"code\" value=\"" + secretPassPhrase  + "\">"
      	     + "<input type=\"hidden\" name=\"UserID\" value=\"" + userIDArg  + "\">"
    	     + "<input type=\"hidden\" name=\"SetCookie\" value=\"" + "true"  + "\">"
	  	     + "<input type=\"hidden\" name=\"Email\" value=\"" + Email  + "\">"
		     + "<input type=\"hidden\" name=\"Password\" value=\"" + Password  + "\">"
    	     
    	     + "</FORM>"
    	     + "  </TABLE>"
    	     + "  </TD></TR><TR><TD COLSPAN=2>"    
    	     + "  <FONT SIZE=-2>You may have to click the \"Refresh\" button on your browser "
    	     + "to view the changes.</FONT>"    
    	     + "  </TD></TR>"    
    	     + "  </TABLE>");
    	    
    	    return returnValue.toString();
    	  }

    	//***********************************
    	//entry point for changes to Calendar
    	//***********************************

    	  public void doPost(HttpServletRequest request,HttpServletResponse response)
    	    throws ServletException, IOException {
    		  System.out.println("IN POST()");
    		  
    		  System.out.println("userid: " + request.getParameter("UserID"));
    		  System.out.println("user: " + request.getParameter("user"));
    		  System.out.println("code: " + request.getParameter("code"));
    		  System.out.println("SetCookie: " + request.getParameter("SetCookie"));
      		System.out.println("email: " + request.getParameter("Email"));
      		System.out.println("password: " + request.getParameter("Password"));
    		  

    	    if (viewOnly) return;  // this servlet is set to disallow changes
    	    HttpSession session = request.getSession(true);
    	    String user = request.getParameter("user");//(String)session.getAttribute("user");

    	    response.setContentType("text/html");
    	    PrintWriter out = response.getWriter();
    	    thisServletURI = "/SchoolProject" + request.getServletPath();
    	  
    	    out.println("<HTML><head><title>Control Panel</title>");
    	    out.println("<SCRIPT Language=JavaScript>");
    	    out.println("function finish(how,date,UserID,user,code,SetCookie,Email,Password) {");
    	    out.println("  if (how == 'OK') {");
    	    out.println("    opener.document.location = '" + thisServletURI + "?date='+date+'" + "&UserID='+UserID+'" + "&user='+user+'" + "&code='+code+'" + "&SetCookie='+SetCookie+'" + "&Email='+Email+'" + "&Password='+Password;" );
    	    out.println("    window.close();");
    	    out.println("  } else if (how == 'conflict') {");
    	    out.println("    alert('Warning: this event conflicts with another event.');");
    	    out.println("    opener.document.location = '" + thisServletURI + "?date='+date+'" + "&UserID='+UserID+'" + "&user='+user+'" + "&code='+code+'" + "&SetCookie='+SetCookie+'" + "&Email='+Email+'" + "&Password='+Password;" );
    	    out.println("    window.close();");
    	    out.println("  } else if (how == 'dbError') {");
    	    out.println("    alert('An unexpected database error was encountered.');");
    	    out.println("    opener.document.location = '" + thisServletURI + "?date='+date+'" + "&UserID='+UserID+'" + "&user='+user+'" + "&code='+code+'" + "&SetCookie='+SetCookie+'" + "&Email='+Email+'" + "&Password='+Password;" );
    	    out.println("    history.go(-1);");
    	    out.println("  } else if (how == 'bad') {");
    	    out.println("    alert('Warning: some data values were missing or formatted incorrectly. Please try again.');");
    	    out.println("    history.go(-1);");
    	    out.println("  }");
    	    out.println("}");
    	    out.println("</SCRIPT></head>");
    	    
    	    String userRequest = request.getParameter("UserRequest");
    	    String eventID = request.getParameter("EventID");
    	    String sqlQuery = null;
    	    String sqlConflict = null;
    	      
    	    if (userRequest.equals("Delete"))
    	      sqlQuery = "DELETE FROM Events WHERE EventID=" + eventID;
    	    else {
    	      String sDate = null;
    	      String eDate = null;
    	      String description = removeSingleQuotes(request.getParameter("Description"));
    	      String notes = removeSingleQuotes(request.getParameter("Notes"));
    	      boolean flagged = Boolean.valueOf(request.getParameter("Flagged")).booleanValue();
    	      try {
    	        String eventType = request.getParameter("EventType");
    	        String date = dfMySQLDate.format(dfDateField.parse(request.getParameter("EventDate")));
    	        if (eventType.equals("REGULAR")) {
    	          String sTime = request.getParameter("STime");
    	          String eTime = request.getParameter("ETime");
    	          if (sTime.equals("")) {  // user may have forgotten to specify the time
    	            sDate = date + " 00:00"; // record the event anyway as SPECIAL
    	            eDate = date + " 00:00";
    	          }
    	          else {
    	            sDate = date + " " + dfMySQLTime.format(dfTimeField.parse(sTime));
    	            if (eTime.equals("")) eDate = sDate; // default ending time is same as starting time
    	            else eDate = date + " " + dfMySQLTime.format(dfTimeField.parse(eTime));
    	          }
    	        } else if (eventType.equals("SPECIAL")) {
    	          sDate = date + " 00:00";
    	          eDate = date + " 00:00";
    	        } else if (eventType.equals("ALLDAY")) {
    	          sDate = date + " 00:00";
    	          eDate = date + " 23:59";
    	        }
    	      } 
    	      catch (Exception e) {
    	          out.println("<body onLoad=finish('bad','" + request.getParameter("EventDate") + "','" + request.getParameter("UserID") + "','" + request.getParameter("user") + "','" + request.getParameter("code") + "','" + request.getParameter("SetCookie") + "','" + request.getParameter("Email") + "','" + request.getParameter("Password") + "');>");  
    	        out.println("</body></html>");
    	        return;  
    	      }
    	             
    	      if (userRequest.equals("New")) {
    	        sqlQuery = "INSERT INTO Events (Sdate,Edate,Flagged,User,Description,Notes) VALUES "
    	         + "('" + sDate + "','" + eDate + "','" + (flagged?1:0) + "','" + user + "','" 
    	         + description + "','" + notes + "')";
    	        sqlConflict = "SELECT * FROM Events WHERE NOT "
    	         + "(Edate<='" + sDate + "' OR Sdate>='" + eDate + "')";
    	      }
    	      else if (userRequest.equals("Revise")) {
    	        sqlQuery = "UPDATE Events SET "
    	         + "Sdate='" + sDate + "',Edate='" + eDate + "',Flagged='" + (flagged?1:0) 
    	         + "',User='" + user + "',Description='" + description + "',Notes='" + notes
    	         + "' WHERE EventID=" + eventID;
    	        sqlConflict = "SELECT * FROM Events WHERE "
    	         + "(NOT (Edate<='" + sDate + "' OR Sdate>='" + eDate + "') AND "
    	         + "EventID<>'" + eventID + "')";
    	      }
    	    }    
    	    ResultSet rsConflict = null;
    	    try {
    	      Class.forName("com.mysql.jdbc.Driver");
    	      Connection conn = DriverManager.getConnection(eventsDatabase,mySQLUser,mySQLPass);
    	      Statement stmt = conn.createStatement();

    	      if (userRequest.equals("New") | userRequest.equals("Revise")) {
    	      // check for conflicting events; if any records returned, a conflict exists.
    	        rsConflict = stmt.executeQuery(sqlConflict);
    	       
    	        /*if( rsConflict.next() )
    	        	System.out.println("true");
    	        else
    	        	System.out.println("false");
    	        */
    	        
    	        stmt = conn.createStatement();
    	        
    	        if (stmt.executeUpdate(sqlQuery) != 1) 
    	          out.println("Caution: possible database error " + stmt.executeUpdate(sqlQuery));
    	        
    	        if (rsConflict.next() ) {// conflict exists
      	          out.println("<body onLoad=finish('conflict','" + request.getParameter("EventDate") + "','" + request.getParameter("UserID") + "','" + request.getParameter("user") + "','" + request.getParameter("code") + "','" + request.getParameter("SetCookie") + "','" + request.getParameter("Email") + "','" + request.getParameter("Password") + "');>");  
    	          do {
    	            out.println(rsConflict.getString("Description") + "<br>");
    	          } while (rsConflict.next());
    	        }
    	        else  // no conflicts; normal termination
      	          out.println("<body onLoad=finish('OK','" + request.getParameter("EventDate") + "','" + request.getParameter("UserID") + "','" + request.getParameter("user") + "','" + request.getParameter("code") + "','" + request.getParameter("SetCookie") + "','" + request.getParameter("Email") + "','" + request.getParameter("Password") + "');>");  
    	        
    	        rsConflict.close();
    	      }
    	      else {  // usrRequest.equals("Delete")
    	        if (stmt.executeUpdate(sqlQuery) != 1) 
    	          out.println("Caution: possible database error " + stmt.executeUpdate(sqlQuery));
  	          out.println("<body onLoad=finish('OK','" + request.getParameter("EventDate") + "','" + request.getParameter("UserID") + "','" + request.getParameter("user") + "','" + request.getParameter("code") + "','" + request.getParameter("SetCookie") + "','" + request.getParameter("Email") + "','" + request.getParameter("Password") + "');>");  
    	      }
    	      out.println("</body></html>");
    	      stmt.close();
    	      conn.close();
    	      
    	    } 
    	    catch (Exception e) { // SqlExceptions caught here
    	    	e.printStackTrace();
    	    	System.out.println("EXCEPTION HERE: " + e.getMessage());
  	          out.println("<body onLoad=finish('dbError','" + request.getParameter("EventDate") + "','" + request.getParameter("UserID") + "','" + request.getParameter("user") + "','" + request.getParameter("code") + "','" + request.getParameter("SetCookie") + "','" + request.getParameter("Email") + "','" + request.getParameter("Password") + "');>");  
    	      out.println(e.getMessage());
    	      out.println("</body></html>");  
    	    }
    	  }
    	  
    	  String removeSingleQuotes(String inString) {
    		  
    	    int i = inString.indexOf('\'',0);
    	    return i<0?inString:removeSingleQuotes(new StringBuffer(inString).insert(i,'\\').toString(),i+2);
    	  }

    	  String removeSingleQuotes(String inString,int fromIndex) {
    	    int i = inString.indexOf('\'',fromIndex);
    	    return i<0?inString:removeSingleQuotes(new StringBuffer(inString).insert(i,'\\').toString(),i+2);
    	  }

    	  String removeDoubleQuotes(String inString) {
    	    int i = inString.indexOf('\"',0);
    	    return i<0?inString:removeDoubleQuotes(new StringBuffer(inString).replace(i,i+1,"&quot;").toString(),i+5);
    	  }

    	  String removeDoubleQuotes(String inString,int fromIndex) {
    	    int i = inString.indexOf('\"',fromIndex);
    	    return i<0?inString:removeDoubleQuotes(new StringBuffer(inString).replace(i,i+1,"&quot;").toString(),i+5);
    	  }

}
