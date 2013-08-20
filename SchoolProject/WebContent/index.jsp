<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>School Web App Project</title>




</head>
<body>

<form action="RegisterServlet" method="post">
      <input type="submit" value="Register">
</form>

<form action="LoginServlet" method="post" >
   <table>
   <tr>
      <td><font face="verdana" size="2px">Email:</font></td>
      <td><input type="text" name="Email"></td>
   </tr>
   <tr>
      <td><font face="verdana" size="2px">Password:</font></td>
      <td><input type="password" name="Password"></td>
   </tr>
   </table>

		
		<INPUT id="Student" type="radio" value="Student" name="user" CHECKED>
		<td><font face="verdana" size="2px">Student</font></td>
		<INPUT id="Teacher" type="radio" value="Teacher" name="user">
		<td><font face="verdana" size="2px">Teacher</font></td>
		
		<input type="hidden" value="yolo" name="yolo">
		
		<br>
		<input type="submit" value="Login">
</form>

<br>

<form action="HelloWorld" method="post">
      <input type="submit" value="Hello World!"> 
</form>

<!--  
          <select name="Feedback" multiple="multiple">  
          <option> "Poor"</option>  
          <option>"Good"</option>  
          <option>"Very Good"</option>  
          <option>"Excellent"</option>  
          </select>  
-->

</body>
</html>