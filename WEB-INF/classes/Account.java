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

@WebServlet("/Account")
@SuppressWarnings("unchecked")

public class Account extends HttpServlet {
	private String error_msg;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		displayAccount(request, response);
	}

	protected void displayAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String TOMCAT_HOME = System.getProperty("catalina.home");
		String filepath=TOMCAT_HOME+"\\webapps\\sportewa\\WEB-INF\\classes\\python\\";
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
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
				pw.print("<div class='entry'>");
				User user=utility.getUser();

				
				pw.print("<table  class='gridtable'style='table-layout: fixed;' >");

				pw.print("<tr>");
				pw.print("<th>User Name:</th><th>" +user.getName()+ "</th>");
				pw.print("<td></td><td></td>");
				pw.print("<td></td><td></td>");
				pw.print("</tr>");

				pw.print("<tr>");
				// pw.print("<th>User Type:</th><th>" +user.getUsertype()+ "</th>");
				pw.print("<td></td><td></td>");
				pw.print("<td></td><td></td>");
				pw.print("</tr>");

				String s = null; 
				String[] cmd = { "python",
                filepath+"recombee_getPurchases.py",user.getName()};
				Process p = Runtime.getRuntime().exec(cmd);
				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
				s = in.readLine();
				if (s == null) {

					System.out.println("came here: if exception occurs sometimes ");

				}else {
					
					HashMap<Integer,Event> orders=MySqlDataStoreUtilities.selectOrder(user.getName());
					int num_orders=orders.size();
					if(num_orders==0){
						//no orders code
					
					}
					else{
						Set<Integer> orderlist=orders.keySet();
						Iterator list_it=orderlist.iterator();
						pw.print("<tr>");
                        pw.print("<td>Order Number:</td>");
                        pw.print("<td>Purchase Date:</td><td colspan=2>Event Name:</td>");
                        pw.print("<td>Price:</td><td></td>");
                        pw.print("</tr>");
						while(list_it.hasNext()){
							Event eve=(Event)orders.get((int)list_it.next());
							
							///Put getter methods in event
							String eventname=eve.getName();
							String eventzip=eve.getPost();
							String sportname=eve.getSport();
							String eventaddress=eve.getAddress();
							int order_num=eve.getOrderNum();
							String time=eve.getPurchaseTime();
							double price=eve.getPrice();
							String eventstate=eve.getState();
							String eventcity=eve.getcity();
							String eventvenue=eve.getVenue();
							
							pw.print("<form method='post' action='WriteReview'>");
							pw.print("<tr>");			
							pw.print("<td>"+order_num+".</td>");
							pw.print("<td>"+time+"</td><td colspan=2>"+eventname+"</td><td>$"+price+"</td>"); 
							pw.print("<input type='hidden' name='eventvenue' value='"+eventvenue+"'>"
                                + "<input type='hidden' name='eventzip' value='"+eventzip+"'>"
                                + "<input type='hidden' name='sportname' value='"+sportname+"'>"
                                + "<input type='hidden' name='eventname' value='"+eventname+"'>"
                                + "<input type='hidden' name='eventstate' value='"+eventstate+"'>"
                                + "<input type='hidden' name='eventaddress' value='"+eventaddress+"'>"
                                + "<input type='hidden' name='ticketprice' value='"+price+"'>"
                                + "<input type='hidden' name='eventcity' value='"+eventcity+"'>");
							pw.print("<td><input type='submit' name='Order' value='Write Review' class='btnbuy'></td>");
							pw.print("</tr>");
							pw.print("</form>");
						}
					}
						
				}	
									
					pw.print("</table>");
					pw.print("</div>");
					pw.print("</div>");
					pw.print("</section>");


			utility.printHtml("LeftNavigationBar.html");	
			utility.printHtml("Footer.html");	        	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}
}
