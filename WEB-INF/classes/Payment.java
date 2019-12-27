import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Random;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/Payment")

public class Payment extends HttpServlet {
	
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String TOMCAT_HOME = System.getProperty("catalina.home");
		String filepath=TOMCAT_HOME+"\\webapps\\sportewa\\WEB-INF\\classes\\python\\";
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);
		if(!utility.isLoggedin())
		{
			HttpSession session = request.getSession(true);				
			session.setAttribute("login_msg", "Please Login to Pay");
			response.sendRedirect("Login");
			return;
		}


		String eventUserName=request.getParameter("eventUserName");
		String ticketPricest=request.getParameter("ticketPricest");
		String eventname=request.getParameter("eventname");
		String city=request.getParameter("city");
		String sport=request.getParameter("generName");
		String postcode=request.getParameter("postcode");
		String state=request.getParameter("state");
		String addr=request.getParameter("address");
		String venue=request.getParameter("venue");

            //calculation of date
            

            Date current=new Date();
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			
            String datestr = dateFormat.format(current);
			//Convert to date format
			int order_num=MySqlDataStoreUtilities.getOrderNum()+1;
            MySqlDataStoreUtilities.insertOrder2(order_num, eventUserName,eventname,sport,datestr,venue,addr,city,postcode,state,Double.parseDouble(ticketPricest));
			utility.printHtml("Header.html");
        	pw.print("<div id='body'><div >");
			pw.print("<section id='content'>");
			pw.print("<div id=''>");
			pw.print("<div class='post'>");
			
			pw.print("<div class='entry'>");
            pw.print("<h4>Your Order</h4>");

			pw.print("<table  class='gridtable'>");
			pw.print("<tr>");
			pw.print("<td > <h4 style='color:red'>Your Order is placed.</h4></td>");
			pw.print("</tr>");


			pw.print("</table>");
			pw.print("</div>");
			pw.print("</div>");
			pw.print("</section>");
			utility.printHtml("LeftNavigationBar.html");
			utility.printHtml("Footer.html");


			 String s = null; //fetch response from python print()
		  	 String[] cmd = {
		      "python",
		      filepath+"recombee14_user_payment.py",
		      eventUserName,eventname.replace(' ','-'),datestr,ticketPricest
		    };
			
		     try {
		    Process p = Runtime.getRuntime().exec(cmd);
		   
		    
		  }catch(IOException ioe)
		    {
		        ioe.printStackTrace();
		    }


		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);
		
		
	}
}
