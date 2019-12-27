import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

@WebServlet("/Registration")
@SuppressWarnings("unchecked")

public class Registration extends HttpServlet {
	private String error_msg;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		displayRegistration(request, response, pw, false);
	}

	/*   Username,Password,Usertype information are Obtained from HttpServletRequest variable and validates whether
		 the User Credential Already Exists or else User Details are Added to the Users HashMap */

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String TOMCAT_HOME = System.getProperty("catalina.home");
		String filepath=TOMCAT_HOME+"\\webapps\\sportewa\\WEB-INF\\classes\\python\\";
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String repassword = request.getParameter("repassword");
		String usertype = "customer";
		
		 //String userName = "Test-4";
	     String userCity = request.getParameter("userCity");
	     String favSport = request.getParameter("favSport");
	     String postcode = request.getParameter("postcode");


	     System.out.println("postcode ............."+postcode);

	     String latitude = request.getParameter("latitude");

	     String longitude = request.getParameter("longitude");
		if(!utility.isLoggedin())
			usertype = request.getParameter("usertype");

		//if password and repassword does not match show error message

		if(!password.equals(repassword))
		{
			error_msg = "Passwords doesn't match!";
		}
		else
		{
			HashMap<String, User> hm=new HashMap<String, User>();
			//String TOMCAT_HOME = System.getProperty("catalina.home");

			//get the userdata from sql database to hashmap

			try
			{
 			//  FileInputStream fileInputStream = new FileInputStream(new File(TOMCAT_HOME+"\\webapps\\csj\\UserDetails.txt"));
			 // ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);	      
			 // hm= (HashMap)objectInputStream.readObject();

			 hm=MySqlDataStoreUtilities.selectUser();
			}
			catch(Exception e)
			{
				
			}

			// if the user already exist show error that already exist

			if(hm.containsKey(username))
				error_msg = "Username already exist as " + usertype;
			else
			{

				User user = new User(username,password,usertype);
				MySqlDataStoreUtilities.insertUser(username,password,repassword,usertype);
				System.out.println(userCity);

			   String[] cmd = {
			      "python",
			      filepath+"recombee12_add_user_profile.py",
			      username,userCity,favSport,postcode,latitude,longitude
			    };


			    try {
			    Process p = Runtime.getRuntime().exec(cmd);
			   
			  	}catch(IOException ioe)
			    {
			        ioe.printStackTrace();
			    }
				HttpSession session = request.getSession(true);				
				session.setAttribute("login_msg", "Your "+usertype+" account has been created. Please login");
				if(!utility.isLoggedin()){
					response.sendRedirect("Login"); return;
				} else {
					response.sendRedirect("Account"); return;
				}
			}
		}

		//display the error message
		if(utility.isLoggedin()){
			HttpSession session = request.getSession(true);				
			session.setAttribute("login_msg", error_msg);
			response.sendRedirect("Account"); return;
		}
		displayRegistration(request, response, pw, true);
		
	}

	/*  displayRegistration function displays the Registration page of New User */
	
	protected void displayRegistration(HttpServletRequest request,
			HttpServletResponse response, PrintWriter pw, boolean error)
			throws ServletException, IOException {
		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		pw.print("<div id='body'><div >");
		pw.print("<section id='content'>");
		pw.print("<div id=''>");
		pw.print("<div class='post'>");
		pw.print("<h2 class='title meta'>");
		pw.print(" <a style='font-size: 24px;'>&nbsp Registration</a>"+
                "</h2><br>");
		pw.print("<div class='entry'>");
		if (error)
			pw.print("<h4 style='color:red'>&nbsp"+error_msg+"</h4>");
		pw.print("<form method='post' action='Registration'>"
				+ "<table class='gridtable'><tr style='border-bottom: none;'><td style='border-bottom: none;'>"
				+ "Username</td><td style='border-bottom: none;'><input type='text' name='username' value='' class='input' required></input>"
				+ "</td></tr><tr style='border-bottom: none;'><td style='border-bottom: none;'>"
				+ "Password</td><td style='border-bottom: none;'><input type='password' name='password' value='' class='input' required></input>"
				+ "</td></tr>"
				+"<tr style='border-bottom: none;'><td style='border-bottom: none;'>"
				+ "Re-Password</td><td style='border-bottom: none;'><input type='password' name='repassword' value='' class='input' required></input>"
				+ "</td></tr>"

				+"<tr style='border-bottom: none;'><td style='border-bottom: none;'>"
				+ "City</td><td style='border-bottom: none;'><input type='userCity' name='userCity' id='userCity' value='' class='input' required></input>"
				+ "</td></tr>"

				+"<tr style='border-bottom: none;'><td style='border-bottom: none;'>"
				+ "Favourite Sport</td><td style='border-bottom: none;'><input type='favSport' name='favSport'  value='' class='input' required></input>"
				+ "</td></tr>"

				+"<tr style='border-bottom: none;'><td style='border-bottom: none;'>"
				+ "Postcode</td><td style='border-bottom: none;'><input type='postcode' name='postcode' id='postcode'  value='' class='input' required></input>"
				+ "</td></tr>"


				+"<tr style='border-bottom: none;'><td style='border-bottom: none;'>"
				+ "Latitude</td><td style='border-bottom: none;'><input type='latitude' name='latitude' id='latitude' value='' style='background-color:#D3D3D3; color: red;' class='input' required readonly ></input>"
				+ "</td></tr>"

				+"<tr style='border-bottom: none;'><td style='border-bottom: none;'>"
				+ "Longitude</td><td style='border-bottom: none;'><input type='longitude' name='longitude' id='longitude' value='' style='background-color:#D3D3D3; color: red;' class='input' required readonly></input>"
				+ "</td></tr>"

				// +"<tr><td>"
				// + "User Type</td><td><select type='hidden' name='usertype' class='input'><option value='customer' selected>Customer</option><option value='retailer'>Store Manager</option><option value='manager'>Salesman</option></select>"
				// + "</td></tr>"
				+"<input type='hidden' id='usertype' name='usertype' value='customer'>"
				+"<tr style='border-bottom: none;'><td style='border-bottom: none;'></td>"+
				"<td style='border-bottom: none;'><input type='submit' class='btnbuy' name='ByUser' value='Create User' style='background-color: white;'></input></td></tr></table>"
				+ "</form>" + "</div></div></div><br>");
		pw.print("</section>");
		utility.printHtml("LeftNavigationBar.html");
		utility.printHtml("Footer.html");
	}
}
