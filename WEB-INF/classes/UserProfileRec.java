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
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
	
import java.util.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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

@WebServlet("/UserProfileRec")

public class UserProfileRec extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		String TOMCAT_HOME = System.getProperty("catalina.home");
		String filepath=TOMCAT_HOME+"\\webapps\\sportewa\\WEB-INF\\classes\\python\\";
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        String name = null;
        String filter = request.getParameter("filter");
        System.out.println("filter..............."+filter);
		Date date= new Date();
		long time1 = date.getTime();
		Timestamp ts = new Timestamp(time1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(ts);
		cal.add(Calendar.DAY_OF_WEEK, 7);
		ts.setTime(cal.getTime().getTime()); 
		ts = new Timestamp(cal.getTime().getTime());
		String time=ts.toString();
		time=time.replace(time.substring(19),"");
		time=(time+"Z").trim().replace(' ','T');
		String num_rec=request.getParameter("rec_num");
		System.out.println(num_rec);
        String favsportevent = request.getParameter("favsportevent");
        System.out.println("favsportevent........."+favsportevent);
        String cityevent = request.getParameter("cityevent");
        System.out.println("cityEvent........."+cityevent);
        String postcodeevent = request.getParameter("postcodeevent");
        System.out.println("postcodeevent........."+postcodeevent);
        HttpSession session = request.getSession(true);

        if(favsportevent == null || favsportevent.isEmpty()){
            favsportevent ="N";
        }

        if(cityevent == null || cityevent.isEmpty()){
            cityevent ="N";
        }

        if(postcodeevent == null || postcodeevent.isEmpty()){
            postcodeevent ="N";
        }
        try
        {
        response.setContentType("text/html");
         pw = response.getWriter();
        Utilities utility = new Utilities(request,pw);
        if(!utility.isLoggedin())
        {
            session = request.getSession(true);             
            session.setAttribute("login_msg", "Please Login ");
            response.sendRedirect("Login");
            return;
        }
    }catch(Exception e){
        System.out.println("Login Error");
    }

        String eventUserName = session.getAttribute("username").toString();
        String s = null; // fetch response from python print()
        Utilities utility = new Utilities(request, pw);
        utility.printHtml("Header.html");
        pw.print("<div id='body'>");
        pw.print("<div style='display: inline'>");
        pw.print("<section id='content'>");
        pw.print("<br>");
	   try {
			if(filter == null || filter.equals("favsport")){
				String[] cmd = { "python",
                filepath+"recombee13_UsertoItemRec.py",
                eventUserName,"Y","N","N","N",num_rec,time};
				pw.print("<div><a style='font-size: 24px;'>" + "Recommendation Based On Favourite Sport" + " </a></div><hr>");
				Process p = Runtime.getRuntime().exec(cmd);
				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
				s = in.readLine();
				readDataAndDisplay(s,request,response,eventUserName);
			}     
			else if(filter.equals("upcomingweek")){
				String[] cmd = { "python",
                filepath+"recombee13_UsertoItemRec.py",
                eventUserName,"N","Y","N","N",num_rec,time};
				pw.print("<div><a style='font-size: 24px;'>" + "Recommendation Based On Upcoming Week" + " </a></div><hr>");
				Process p = Runtime.getRuntime().exec(cmd);
				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
				s = in.readLine();
				readDataAndDisplay(s,request,response,eventUserName);
				}
			
			else if(filter.equals("postcode")){
				String[] cmd = { "python",
                filepath+"recombee13_UsertoItemRec.py",
                eventUserName,"N","N","N","Y",num_rec,time};
				pw.print("<div><a style='font-size: 24px;'>" + "Recommendation Based On Postcode" + " </a></div><hr>");
				Process p = Runtime.getRuntime().exec(cmd);
				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
				s = in.readLine();
				readDataAndDisplay(s,request,response,eventUserName);
				
			}else if(filter.equals("city")){
				String[] cmd = { "python",
                filepath+"recombee13_UsertoItemRec.py",
                eventUserName,"N","N","Y","N",num_rec,time};
				pw.print("<div><a style='font-size: 24px;'>" + "Recommendation Based On City" + " </a></div><hr>");
				Process p = Runtime.getRuntime().exec(cmd);
				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
				s = in.readLine();
				readDataAndDisplay(s,request,response,eventUserName);
			}else if(filter.equals("allfilter")){
				String[] cmd = { "python",
                filepath+"recombee13_UsertoItemRec.py",
                eventUserName,favsportevent,"N",cityevent.replace(" ","-"),postcodeevent,"10",time};
				System.out.println(cmd[0]+" "+cmd[1]+" "+cmd[2]+" "+cmd[3]+" "+cmd[4]+" "+cmd[5]+" "+cmd[6]+" "+cmd[7]);
				pw.print("<div><a style='font-size: 24px;'>" + "Recommendation Based On MultiFilter" + " </a></div><hr>");
				Process p = Runtime.getRuntime().exec(cmd);
				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
				s = in.readLine();
				readDataAndDisplay(s,request,response,eventUserName);
				}
			else if(filter.equals("dist")){
				String lat=session.getAttribute("latitude").toString();
				String longi=session.getAttribute("longitude").toString();
				System.out.println("Printing: "+lat+":"+longi);
				String[] cmd_distance = { "python",
                filepath+"recombee_dist.py",
                eventUserName,lat.replace("'",""),longi.replace("'",""),request.getParameter("dist")};
				pw.print("<div><a style='font-size: 24px;'>" + "Recommendations within "+request.getParameter("dist")+" Miles" + " </a></div><hr>");
				Process p = Runtime.getRuntime().exec(cmd_distance);
				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
				s = in.readLine();
				readDataAndDisplay(s,request,response,eventUserName);
			}

	}
         catch (Exception e) {
            System.out.println("exception:");
			e.printStackTrace();
        }
        pw.print("</section>");

        utility.printHtml("LeftNavigationBar.html");
        utility.printHtml("Footer.html");

    }
	
	
	public void readDataAndDisplay(String s, HttpServletRequest request, HttpServletResponse response,String eventUserName){
		try{
			PrintWriter pw=response.getWriter();
			Gson gson = new Gson();
			JsonObject body = gson.fromJson(s, JsonObject.class);
			JsonArray recomms = body.get("recomms").getAsJsonArray();
			int i = 1;
			int size = recomms.size();
			if (recomms.size() > 0) {
				for (int j = 0; j < recomms.size(); j++) {
					JsonObject recommen_obj = recomms.get(j).getAsJsonObject();
					JsonElement id1 = recommen_obj.get("id");
					JsonObject values = recommen_obj.get("values").getAsJsonObject();
					JsonElement city = values.get("city");
					JsonElement postcode = values.get("postcode");
					String cityZip = city.toString().replace("\"", "")+"-"+ postcode.toString().replace("\"", "");
					JsonElement eventImage = values.get("eventImage");
					String eventImagestr = eventImage.toString();
					JsonElement steventDate = values.get("steventDate");
					JsonElement generName = values.get("generName");
					JsonElement ticketPrice = values.get("ticketPrice");
					JsonElement address=values.get("address");
					JsonElement state=values.get("state");
					JsonElement venue=values.get("venueName");
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
					String ticketPricest= ticketPrice.toString();

					if (i % 3 == 1)
						pw.print("<table style='width:100%; display: inline; float: auto;'>");
					pw.print("<tr><td><div style='width:300px; text-align:center;'><h3>"
							+ id1.toString().replace('-', ' ').replace("\"", "") + "</h3><strong>" + generName.toString().replace("\"", "")
							+ " </strong></div></td></tr>");

					pw.print("<tr><td><div style='width:300px'><ul style='list-style-type:none;'>"
							+ "<li id='item'>" + "<img style='width:220px; height:150px;' src=" + eventImagestr
							+ " alt='' /></li>" + "<li>" + "</li>" + "</br>" );
					
					 pw.print("<li><div style='width:250px; text-align:center;'><strong>"
							+ cityZip + "</strong></br><strong>$" + ticketPricest
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
							+ "<input type='submit' value='ViewEvent' class='btnreview'>" + "</div>"
							+ "</form></li>" + "</ul>" + "</div>" + "</td>" + "</div>" + "</td>" + "</tr>");
					pw.print("</table>");
					if (i % 3 == 0 || i == size)
						i++;
				}
			}
			else {
				System.out.println("zero recommendation");
				pw.print("<div><a style='font-size: 24px; color:red;'>" + "No Recommendation Found" + " </a></div>");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
}
}
