import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/WriteReview")
public class WriteReview extends HttpServlet {

 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	response.setContentType("text/html");
	PrintWriter pw = response.getWriter();
	Utilities utility = new Utilities(request, pw);
	if (!utility.isLoggedin()) 
	{
		HttpSession session = request.getSession(true);
		session.setAttribute("login_msg", "Please Login to Write a Review");
		response.sendRedirect("Login");
		return;
	}

	String eventname = request.getParameter("eventname");
	String eventcity = request.getParameter("eventcity");
	String eventvenue = request.getParameter("eventvenue");
	String eventzip = request.getParameter("eventzip");
	String eventaddress = request.getParameter("eventaddress");
	String eventstate = request.getParameter("eventstate");
	String sportname = request.getParameter("sportname");
	String ticketprice = request.getParameter("ticketprice");
	utility.printHtml("Header.html");
	pw.print("<form name ='WriteReview' action='SubmitReview' method='post'>");
	pw.print("<div id='body'>");
	pw.print("<div style='display: inline'>");
	pw.print("<section id='content'>");
	pw.print("<div class='post'><h2 class='title meta'>");
	pw.print("<a style='font-size: 24px;'>Write Review for " + eventname + "</a>");
	pw.print("</h2><div class='entry'>");
	pw.print("<table class='gridtable'>");

	pw.print("<tr><td> User Id: </td><td>");
	pw.print(utility.username());
	pw.print("<input type='hidden' name='userid' value='" + utility.username() + "'>");
	pw.print("</td></tr>");
	pw.print("<tr><td> Age: </td><td>");
	pw.print("<input type='text' name='age' >");
	pw.print("</td></tr>");
	pw.print("<tr><td> Occupation: </td><td>");
	pw.print("<input type='text' name='occupation'>");
	pw.print("</td></tr>");
	pw.print("<tr><td> Gender: </td><td>");
	pw.print("<input type='radio' name='gender' value='male'/>Male<br>");
	pw.print("<input type='radio' name='gender' value='female'/>Female<br>");
	pw.print("</td></tr>");

	pw.print("<tr><td> Event Name: </td><td>");
	pw.print(eventname);
	pw.print("<input type='hidden' name='eventname' value='" + eventname + "'>");
	pw.print("</td></tr>");
	pw.print("<tr><td> Sport Name:</td><td>");
	pw.print(sportname);
	pw.print("<input type='hidden' name='sportname' value='" + sportname + "'>");
	pw.print("</td></tr>");

	if (ticketprice == "null")
	ticketprice = "0.0";
	else {
	pw.print("<tr><td> Ticket Price:</td><td>$");
	pw.print(ticketprice);
	pw.print("</td><td><input type='hidden' name='ticketprice' value='" + ticketprice + "'>");
	pw.print("</td></tr>");
	}
	if (eventvenue == "null")
	eventvenue = "Not Found";
	else {
	pw.print("<tr>");
	pw.print("<td> Event Venue: </td><td>");
	pw.print(eventvenue);
	pw.print("</td><td> <input type='hidden' name='eventvenue' value='" + eventvenue + "'> </td>");
	pw.print("</tr>");
	}
	if (eventaddress == "null")
	eventaddress = "Not Found";
	else {
	pw.print("<tr>");
	pw.print("<td> Event Address: </td><td>");
	pw.print(eventaddress);
	pw.print("</td><td> <input type='hidden' name='eventaddress' value='" + eventaddress + "'> </td>");
	pw.print("</tr>");
	}
	if (eventcity == "null")
	eventcity = "Not Found";
	else {
	pw.print("<tr>");
	pw.print("<td> Event City: </td><td>");
	pw.print(eventcity);
	pw.print("</td><td> <input type='hidden' name='eventcity' value='" + eventcity + "'> </td>");
	pw.print("</tr>");
	}
	if (eventstate == "null")
	eventstate = "Not Found";
	else {
	pw.print("<tr>");
	pw.print("<td> Event State: </td><td>");
	pw.print(eventstate);
	pw.print("</td><td> <input type='hidden' name='eventstate' value='" + eventstate + "'> </td>");
	pw.print("</tr>");
	}
	if (eventzip == "null")
	eventzip = "Not Found";
	else {
	pw.print("<tr>");
	pw.print("<td> Event Zip Code: </td><td>");
	pw.print(eventzip);
	pw.print("</td><td> <input type='hidden' name='eventzip' value='" + eventzip + "'> </td>");
	pw.print("</tr>");
	}
	pw.print("<tr>");
	pw.print("<td> Review Date: </td>");
	pw.print("<td> <input type='date' name='reviewdate'> </td>");
	pw.print("</tr>");
	pw.print("<tr></tr><tr></tr><tr><td> Review Rating: </td>");
	pw.print("<td>");
	pw.print("<select name='reviewrating'>");
	pw.print("<option value='1' selected>1</option>");
	pw.print("<option value='2'>2</option>");
	pw.print("<option value='3'>3</option>");
	pw.print("</td></tr>");
	pw.print("<tr>");
	pw.print("<td> Review Text: </td>");
	pw.print("<td><textarea name='reviewtext' rows='4' cols='50'> </textarea></td></tr>");
	pw.print("<tr><td colspan='2'><input type='submit' class='btnbuy' name='SubmitReview' value='SubmitReview'></td></tr></table>");
	pw.print("</form></li></ul></td>");
	pw.print("</tr></td>");
	pw.print("</table></div></div></div>");
	pw.print("</h2></section>");
	utility.printHtml("LeftNavigationBar.html");
	utility.printHtml("Footer.html");
 }
}