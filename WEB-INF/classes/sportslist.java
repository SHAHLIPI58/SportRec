import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.io.*;
import java.sql.*;
import org.json.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.*;
import java.nio.charset.StandardCharsets;

@WebServlet("/SportsList")


public class sportslist extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		String TOMCAT_HOME = System.getProperty("catalina.home");
		String filepath=TOMCAT_HOME+"\\webapps\\sportewa\\WEB-INF\\classes\\python\\";
		response.setContentType("text/html");
		Utilities utility = new Utilities(request, pw);
		try
         {  
			   response.setContentType("text/html");
				if(!utility.isLoggedin())
				{
					HttpSession session = request.getSession(true);				
					session.setAttribute("login_msg", "Please Login");
					response.sendRedirect("Login");
					return;
				}
				HttpSession session=request.getSession(); 	
				utility.printHtml("Header.html");

				pw.print("<div id='body'><div >");
				pw.print("<section id='content'>");
				pw.print("<div id=''>");
				pw.print("<div class='post'>");
				pw.print("<div class='entry'><table>");
				User user=utility.getUser();
				ArrayList<String> names=MySqlDataStoreUtilities.getSports();
				for(int i=0;i<names.size()-1;i+=2)
				{
					pw.println("<tr><td><a href='EventList?filter="+names.get(i)+"'>"+names.get(i)+"</a></td>");
					pw.println("<td><a href='EventList?filter="+names.get(i+1)+"'>"+names.get(i+1)+"</a></td></tr>");
				}
				pw.print("</table></div>");
				pw.print("</div>");
				pw.print("</section>");
				utility.printHtml("LeftNavigationBar.html");	
				utility.printHtml("Footer.html");
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
	}
}