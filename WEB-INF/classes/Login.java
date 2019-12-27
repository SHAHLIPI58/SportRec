import java.io.*;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Login")
@SuppressWarnings("unchecked")

public class Login extends HttpServlet {

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();

		/* User Information(username,password,usertype) is obtained from HttpServletRequest,
		Based on the Type of user(customer,retailer,manager) respective hashmap is called and the username and 
		password are validated and added to session variable and display Login Function is called */

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String usertype = request.getParameter("usertype");
		HashMap<String, User> hm=new HashMap<String, User>();
		//String TOMCAT_HOME = System.getProperty("catalina.home");
		//user details are validated using a file 
		//if the file containts username and passoword user entered user will be directed to home page
		//else error message will be shown
		try
		{		
    //       FileInputStream fileInputStream = new FileInputStream(new File(TOMCAT_HOME+"\\webapps\\csj\\UserDetails.txt"));
    //       ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);	      
		  // hm = (HashMap)objectInputStream.readObject();


			hm=MySqlDataStoreUtilities.selectUser();
			System.out.println(hm);
		}
		catch(Exception e)
		{
				
		}
		User user = hm.get(username);
		System.out.println("user object:"+user);
		//System.out.println("user's password:"+user.getPassword());
		if(user!=null)
		{
		 String user_password = user.getPassword();
		 String user_type = user.getUsertype();
		 if (password.equals(user_password) && usertype.equals(user_type)) 
			{
			HttpSession session = request.getSession(true);
			session.setAttribute("username", user.getName());
			//session.setAttribute("usertype", user.getUsertype());
			session.setAttribute("usertype", usertype);
			//response.sendRedirect("Home");

			if(session.getAttribute("usertype").equals("manager"))
			{
			System.out.println("Salesman");
			response.sendRedirect("SalesManager");
			}else
			if(session.getAttribute("usertype").equals("retailer"))
			{
				System.out.println("Manager View");
			response.sendRedirect("ManagerHome");
			}
			
			else{
				System.out.println("Customer");
			//response.sendRedirect("UserProfileRec");
				response.sendRedirect("Home");
			}
			return;
			}
		}
		displayLogin(request, response, pw, true);
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		displayLogin(request, response, pw, false);
	}


	/*  Login Screen is Displayed, Registered User specifies credentials and logins into the Game Speed Application. */
	protected void displayLogin(HttpServletRequest request,
			HttpServletResponse response, PrintWriter pw, boolean error)
			throws ServletException, IOException {

		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		pw.print("<div id='body'><div >");
		pw.print("<section id='content'>");
		pw.print("<div id=''>");
		pw.print("<div class='post'>");
		pw.print("<h2 class='title meta'>");
		pw.print(" <a style='font-size: 24px;'>&nbspLogin</a>"+
                "</h2><br>");
		pw.print("<div class='entry'>");
		if (error)
			pw.print("<br><h4 style='color:red'>Please check your username, password and user type!</h4>");
		HttpSession session = request.getSession(true);
		if(session.getAttribute("login_msg")!=null){			
			pw.print("<h4 style='color:red'>"+session.getAttribute("login_msg")+"</h4>");
			session.removeAttribute("login_msg");
		}
		pw.print("<form method='post' action='Login'>"
				+ "<table class='gridtable'> <br><tr style='border-bottom: none;'><td style='border-bottom: none;'>"
				+ "Username</td><td style='border-bottom: none;'><input type='text' name='username' value='' class='input' required></input>"
				+ "</td></tr><tr style='border-bottom: none;'><td style='border-bottom: none;'>"
				+ "Password</td><td style='border-bottom: none;'><input type='password' name='password' value='' class='input' required></input>"
				+ "</td></tr>"
				// +"<tr><td>"
				// + "User Type</td><td><select name='usertype' class='input'><option value='customer' selected>Customer</option><option value='retailer'>Store Manager</option><option value='manager'>Salesman</option></select>"
				// + "</td></tr>"
				 +"<input type='hidden' id='usertype' name='usertype' value='customer'>"

				+"<tr style='border-bottom: none;'><td style='border-bottom: none;'></td><td style='border-bottom: none;'>"
				+ "<input type='submit' class='btnbuy' value='Login' '></input>"
				+ "</td></tr><tr style='border-bottom: none;'><td style='border-bottom: none;'></td><td style='border-bottom: none;'>"
				+ "<strong><a class='' href='Registration' '>New User? Register here!</a></strong>"
				+ "</td></tr></table>"
				+ "</form>" + "</div></div></div>");
		pw.print("</section>");
		utility.printHtml("LeftNavigationBar.html");
		utility.printHtml("Footer.html");
	}

}
