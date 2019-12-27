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

import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.*;
import java.io.File;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@WebServlet("/EventList")

public class EventList extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		String TOMCAT_HOME = System.getProperty("catalina.home");
		String filepath=TOMCAT_HOME+"\\webapps\\sportewa\\WEB-INF\\classes\\python\\";
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        String name = null;
        String filter = request.getParameter("filter");
        HttpSession session = request.getSession(true);
        try
        {
        response.setContentType("text/html");
        pw = response.getWriter();
        Utilities utility = new Utilities(request,pw);
        if(!utility.isLoggedin())
        {
            session = request.getSession(true);             
            session.setAttribute("login_msg", "Please Login");
            response.sendRedirect("Login");
            return;
        }
        String eventUserName = session.getAttribute("username").toString();
        String s = null; // fetch response from python print()
        String[] cmd = { "python",
                filepath+"recombee16_EventList.py",
                filter.trim().replace(' ','-')};
        utility.printHtml("Header.html");
        pw.print("<div id='body'>");
        pw.print("<div style='display: inline'>");
        pw.print("<section id='content'>");
        pw.print("<br>");
		pw.print("<div><a style='font-size: 24px;'>" + filter+" Events" + " </a></div><hr>");
		Process p = Runtime.getRuntime().exec(cmd);
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		s = in.readLine();
		readDataAndDisplay(s,request,response,eventUserName);
        pw.print("</section>");
        utility.printHtml("LeftNavigationBar.html");
        utility.printHtml("Footer.html");
		}
		catch(Exception e){
        System.out.println("Exception:");
		e.printStackTrace();
    }
    }
	
	public void readDataAndDisplay(String s, HttpServletRequest request, HttpServletResponse response,String eventUserName){
		try{
			PrintWriter pw=response.getWriter();
			Gson gson = new Gson();
                JsonArray jsonarraylist = gson.fromJson(s, JsonArray.class);
                System.out.println(jsonarraylist.size());
                int i = 1;
                int size = jsonarraylist.size();
                if (jsonarraylist.size() > 0) {

                    for (int j = 0; j < jsonarraylist.size(); j++) {

                        JsonObject even_obj = jsonarraylist.get(j).getAsJsonObject();
                        
                        JsonElement id1 = even_obj.get("itemId");
                        
                        JsonElement city = even_obj.get("city");
                       
                        JsonElement postcode = even_obj.get("postcode");
                        System.out.println(postcode);
                       
                       JsonElement eventImage = even_obj.get("eventImage");

                       String eventImagestr = eventImage.toString();


                        JsonElement steventDate = even_obj.get("steventDate");
  
                         JsonElement generName = even_obj.get("generName");

                        JsonElement ticketPrice = even_obj.get("ticketPrice");
                        String ticketPricest= ticketPrice.toString();

						
						JsonElement address=even_obj.get("address");
						JsonElement state=even_obj.get("state");
						JsonElement venue=even_obj.get("venueName");
                        
						
						String sport=generName.toString().replace("\"", "");
						String evenue=venue.toString().replace("\"", "");
						String eaddr=address.toString().replace("\"", "");
						String ecity=city.toString().replace("\"", "");
						String estate=state.toString().replace("\"", "");
						String epostcode=postcode.toString().replace("\"", "");
						
						String eventDate=steventDate.toString().replace("\"", "");
						eventDate=eventDate.replace('T',' ');
						eventDate=eventDate.replace('Z',' ');
						eventDate=eventDate.trim();
						eventDate=eventDate.concat("");
						String eventname=id1.toString().replace('-', ' ').replace("\"", "");


                        if (i % 3 == 1)
                            pw.print("<table style='width:100%; display: inline; float: auto;'>");
                        pw.print("<tr><td><div style='width:300px; text-align:center;'><h3>"
                                + id1.toString().replace('-', ' ').replace("\"", "") + "</h3><strong>" + generName.toString().replace("\"", "")
                                + " </strong></div></td></tr>");

                        pw.print("<tr><td><div style='width:300px'><ul style='list-style-type:none;'>"
                                + "<li id='item'>" + "<img style='width:220px; height:150px;' src=" + eventImagestr
                                + " alt='' /></li>" + "<li>" + "</li>" + "</br>" );
                        
                         pw.print("<li><div style='width:250px; text-align:center;'><strong>"
                                + city+"-"+epostcode + "</strong></br><strong>$" + ticketPricest
                                + " </strong></br><strong>" + eventDate
                                + " </strong></div></li>");              
                        pw.print("<li><form method='post' action='ViewItem'>"
                                + "<input type='hidden' name='eventUserName' value='"+eventUserName+"'>"
								+ "<input type='hidden' name='eventImagestr' value='"+eventImagestr+"'>"
								+ "<input type='hidden' name='generName' value='"+sport+"'>"
								+ "<input type='hidden' name='eventname' value='"+eventname+"'>"
								+ "<input type='hidden' name='venue' value='"+evenue+"'>"
								+ "<input type='hidden' name='address' value='"+eaddr+"'>"
								+ "<input type='hidden' name='state' value='"+estate+"'>"
								+ "<input type='hidden' name='postcode' value='"+epostcode+"'>"
								+ "<input type='hidden' name='ticketPricest' value='"+ticketPricest+"'>"
								+ "<input type='hidden' name='city' value='"+ecity+"'>"
								+ "<input type='hidden' name='eventDate' value='"+eventDate+"'>"
                                + "<div style='text-align:center'>" 
                                + "<input type='submit' value='View Event' class='btnreview'>" + "</div>"
                                + "</form></li>" + "</ul>" + "</div>" + "</td>" + "</div>" + "</td>" + "</tr>");
                        pw.print("</table>");
                        if (i % 3 == 0 || i == size)
                            i++;
				}
			}
			else {
				System.out.println("No Events found");
				pw.print("<div><a style='font-size: 24px; color:red;'>" + "No Recommendation Found" + " </a></div>");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
}
}
