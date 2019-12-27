import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/SubmitReview")

public class SubmitReview extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
	    Utilities utility= new Utilities(request, pw);
		storeReview(request, response);
	}
	protected void storeReview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
		{
		try
		{
			String TOMCAT_HOME = System.getProperty("catalina.home");
			String filepath=TOMCAT_HOME+"\\webapps\\sportewa\\WEB-INF\\classes\\python\\";
			response.setContentType("text/html");
			PrintWriter pw = response.getWriter();
			Utilities utility = new Utilities(request,pw);
			String userid=request.getParameter("userid");
			String eventname=request.getParameter("eventname");
			String eventcity=request.getParameter("eventcity");
			String eventvenue=request.getParameter("eventvenue");
			String eventzip=request.getParameter("eventzip");
			String eventaddress=request.getParameter("eventaddress");
			String eventstate=request.getParameter("eventstate");
			String sportname=request.getParameter("sportname");
			String ticketprice=request.getParameter("ticketprice");
			String userage=request.getParameter("userage");
			String gender=request.getParameter("gender");
			String occupation=request.getParameter("occupation");
			String reviewdate=request.getParameter("reviewdate");
			String reviewrating=request.getParameter("reviewrating");
			String reviewtext=request.getParameter("reviewtext");
			
			Review review=new Review(userid,eventname,sportname,Double.parseDouble(ticketprice),eventvenue,eventaddress,eventcity,eventstate,eventzip,userage,gender,occupation,reviewrating,reviewdate,reviewtext);
			String message=MongoDBDataStoreUtilities.insertReview(review);		
			String[] cmd = { "python",
			filepath+"recombee_addRating.py",
			userid,eventname.replace(' ','-'),(reviewrating)};
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			utility.printHtml("Header.html");
			
			pw.print("<div id='body'>");
			pw.print("<div style='display: inline'>");
			pw.print("<section id='content'>");
			pw.print("<div class='post'><h2 class='title meta'>");
			pw.print("<a style='font-size: 24px;'>Review</a>");
			pw.print("</h2><div class='entry'>");
			if(message.equals("Successfull"))
			pw.print("<h2>Review for &nbsp"+eventname+" Stored </h2>");
			else
			pw.print("<h2>Mongo Db is not up and running </h2>");

			pw.print("</div></div></div>");		
			utility.printHtml("LeftNavigationBar.html");
			utility.printHtml("Footer.html");
					
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}  			
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
    }
}
