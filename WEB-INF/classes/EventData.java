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
import java.util.*;

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


@WebServlet("/EventData")

public class EventData extends HttpServlet {

	/* Phone Page Displays all the phones and their Information in Game Speed */

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filepath="C:\\tomcat-7.0.34-preconfigured\\apache-tomcat-7.0.34\\webapps\\EWA_PROJECT\\recombee\\";
		Event eve=(Event)request.getAttribute("data");
		response.setContentType("text/html");
		//Carousel carousel=new Carousel();
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request,pw);
		//utility.printTop();
		utility.printHtml("Header.html");
		HttpSession session = request.getSession(true);
		//utility.printHtml("LeftNavigationBar.html");
		if(!utility.isLoggedin())
        {
            session = request.getSession(true);             
            session.setAttribute("login_msg", "Please Login");
            response.sendRedirect("Login");
            return;
        }
		
		String eventUserName=session.getAttribute("username").toString();
		String ticketPricest=String.valueOf(eve.getPrice());
		String eventname=eve.getName();
		String city=eve.getcity();
		String eventDate=eve.getDate();
		String image=eve.getImage();
		String sport=eve.getSport();
		String venue=eve.getVenue();
		String addr=eve.getAddress();
		String state=eve.getState();
		String postcode=eve.getPost();
		String manufacturerrebate="No";
		
		pw.print("<div id='body'>");
        pw.print("<div style='display: inline'>");
        pw.print("<section id='content'>");
		pw.print("<div id=''>");
		pw.print("<div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size:20px; text-align:center; '>"+eventname+"</a>");
		pw.print("</h2><br/><div class='entry'><table id='bestseller'>");
		pw.print("<tr><td colspan=2 align='center'><ul>");
		pw.print("<li style='list-style-type: none;'><h3 style='color:black;'><img src="+image+" alt='' style='height:300px;width:300px;'/></li>");
		pw.print("<li style='list-style-type: none;'><form method='post' action='Payment'>" 
								+"<input type='hidden' name='eventname' value='" +eventname + "'>"
                                + "<input type='hidden' name='eventUserName' value='"+eventUserName+"'>"
                                + "<input type='hidden' name='city' value='"+city+"'>"
                                + "<input type='hidden' name='eventDate' value='"+eventDate+"'>"
                                + "<input type='hidden' name='generName' value='"+sport+"'>"
								+ "<input type='hidden' name='venue' value='"+venue+"'>"
                                + "<input type='hidden' name='address' value='"+addr+"'>"
                                + "<input type='hidden' name='state' value='"+state+"'>"
                                + "<input type='hidden' name='eventImagestr' value='"+image+"'>"
                                + "<input type='hidden' name='postcode' value='"+postcode+"'>"
                                + "<input type='hidden' name='ticketPricest' value='"+ticketPricest+"'>"
								+"<input style='background-color: #779AC0; width: 100px; font-size:20px; height:40px;' type='submit' value='Buy Now'/></td></tr></form></li>"
								+"<td colspan=2 align='center'><li style='list-style-type: none;'><form method='post' action='ViewReview'>");	

		pw.print("<tr><td colspan=2><li style='list-style-type: none;'><h2><strong> $"+ticketPricest+"</strong><h2></li>");
		pw.print("<li style='list-style-type: none;'><h3 style='color:black;'><strong>"+eventname+"</strong></h3></li>");
		pw.print("<li style='list-style-type: none;'><h4 style='color:black;'><strong>Genre : </strong>"+sport+"</h4></li>");
		pw.print("<li style='list-style-type: none;'><h4 style='color:black;'><strong>Address : </strong>"+city+"</h4></li>");
		pw.print("<li style='list-style-type: none;'><h4 style='color:black;'><strong>Date : </strong>"+eventDate+"</h4></li>");
		
		pw.print("<tr><td><div><a style='font-size: 24px;'>Reviews for "+eventname+"</a></div><hr></td></tr>");	 
		HashMap<String, ArrayList<Review>> hm= MongoDBDataStoreUtilities.selectReview(eventname);
		
		if(hm==null)
		{
			pw.println("<tr><td><h2>Mongo Db server is not up and running</h2></td></tr>");
		}
		else if(hm.size()==0)
			pw.println("<tr><td><h2>There are no reviews for this product.</h2></td></tr>");
		else
		{
			System.out.println();
			for (Review r : hm.get(eventname)) {	
			pw.println("<tr rowspan=3><td colspan=3><strong>"+r.getUserId()+"</strong> ("+r.geteventcity()+")-(Rating:"+r.getReviewRating().toString()+")<br/>");
			pw.println("<i>"+r.getReviewDate()+"</i><br/>");
			pw.println(r.getReviewText()+"</td></tr>");
			}
		}
			
		pw.print("</ul></td></tr>");
		String s = null; // fetch response from python print()

        String[] cmd = { "python",
                filepath+"recombee_item2item.py",
                eventname.replace(' ','-'),eventUserName};
				
		 try {
			 pw.print("<tr></tr>");
            pw.print("<tr><td><div><a style='font-size: 30px;'>" + "Similar Events For You" + " </a></div><hr></td></tr>");
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            s = in.readLine();
            

            if (s == null) {

                System.out.println("came here: if exception occurs sometimes ");

            }else {
				
				JSONObject body=new JSONObject(s);
				JSONArray recomms=body.getJSONArray("recomms");

                int i = 1;
                int size = recomms.length();
                if (size > 0) {

                    for (int j = 0; j < size; j++) {
						JSONObject recommen_obj=recomms.getJSONObject(j);
						String id1=recommen_obj.getString("id");
						JSONObject values=recommen_obj.getJSONObject("values");
						String rcity=values.getString("city");
						String rpostcode=values.getString("postcode");
						String eventImage=values.getString("eventImage");
						String steventDate=values.getString("steventDate");
						String generName=values.getString("generName");
						String ticketPrice=values.getString("ticketPrice");
						String raddress=values.getString("address");
						String rstate=values.getString("state");
						String rvenue=values.getString("venueName");
						String cityZip = city.toString().replace("\"", "")+"-"+ postcode.toString().replace("\"", "");
						String eventImagestr = eventImage.toString();
                                               
						
						String rsport=generName.toString().replace("\"", "");
						String revenue=rvenue.toString().replace("\"", "");
						String readdr=raddress.toString().replace("\"", "");
						String recity=rcity.toString().replace("\"", "");
						String restate=rstate.toString().replace("\"", "");
						String repostcode=rpostcode.toString().replace("\"", "");
						recity=revenue+" , "+readdr+" , "+recity+" , "+restate+" - "+repostcode;
						
						String reventDate=steventDate.toString().replace("\"", "");
						reventDate=reventDate.replace('T',' ');
						reventDate=reventDate.replace('Z',' ');
						reventDate=reventDate.trim();
						reventDate=reventDate.concat("GMT");
						String reventname=id1.toString().replace("\"", "");
						String rticketPricest= ticketPrice.toString();
                       


                        if (i % 3 == 1)
                            pw.print("<table style='width:100%; display: inline; float: auto;'>");
                        pw.print("<tr><td><div style='width:300px; text-align:center;'><h3>"
                                + id1.toString().replace('-', ' ').replace("\"", "") + "</h3><strong>" + generName.toString().replace("\"", "")
                                + " </strong></div></td></tr>");

                        pw.print("<tr><td><div style='width:300px'><ul style='list-style-type:none;'>"
                                + "<li id='item'>" + "<img style='width:220px; height:150px;' src=" + eventImagestr
                                + " alt='' /></li>" + "<li>" + "</li>" + "</br>" );
                        
                         pw.print("<li><div style='width:250px; text-align:center;'><strong>"
                                + rcity + "</strong></br><strong>$" + rticketPricest
                                + " </strong></br><strong>" + reventDate
                                + " </strong></div></li>");

                        pw.print("<li><form method='post' action='ViewItem'>"
                                + "<input type='hidden' name='eventUserName' value='"+eventUserName+"'>"
                                + "<input type='hidden' name='eventImagestr' value='"+eventImagestr+"'>"
                                + "<input type='hidden' name='generName' value='"+rsport+"'>"
                                + "<input type='hidden' name='eventname' value='"+reventname+"'>"
								+ "<input type='hidden' name='venue' value='"+revenue+"'>"
								+ "<input type='hidden' name='address' value='"+readdr+"'>"
								+ "<input type='hidden' name='state' value='"+restate+"'>"
                                + "<input type='hidden' name='postcode' value='"+repostcode+"'>"
                                + "<input type='hidden' name='ticketPricest' value='"+rticketPricest+"'>"
                                + "<input type='hidden' name='city' value='"+recity+"'>"
                                + "<input type='hidden' name='eventDate' value='"+reventDate+"'>"
                                + "<div style='text-align:center'>" 
                                + "<input  type='submit' value='ViewEvent' class='btnreview'>" + "</div>"
                                + "</form></li>" + "</ul>" + "</div>" + "</td>" + "</div>" + "</td>" + "</tr>");
                        pw.print("</table>");
                        if (i % 3 == 0 || i == size)
                            i++;

                    }
                } else {
                    System.out.println("zero recommendation");
                    pw.print("<div><a style='font-size: 24px; color:red;'>" + "No Recommendation Found" + " </a></div>");
                }
				
				String[] cmd1 = { "python",
                filepath+"recombee_add_detail_view.py",
               eventUserName, eventname.replace(' ','-')};
				
            p = Runtime.getRuntime().exec(cmd1);
			
			}
		 }			catch (Exception e) {
            System.out.println("exception");
			e.printStackTrace();
        }
		
		pw.print("</ul></td></tr>");
		
		pw.print("</table></div></div></div>");
		pw.print("</section>");

        utility.printHtml("LeftNavigationBar.html");
		utility.printHtml("Footer.html");
		}
}