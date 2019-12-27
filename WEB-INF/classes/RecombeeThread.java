import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.*;
import org.json.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class RecombeeThread implements Runnable{
	private Thread t;
	public void run(){
		try{
		String TOMCAT_HOME = System.getProperty("catalina.home");
		String filepath=TOMCAT_HOME+"\\webapps\\sportewa\\WEB-INF\\classes\\python\\";
		String[] cmd = { "python",
		filepath+"recombee_additems.py",filepath};
		Process p = Runtime.getRuntime().exec(cmd);
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String s = in.readLine();
		//System.out.println(s);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void start () {
      if (t == null) {
         t = new Thread (this);
         t.start ();
      }
   }
	
	
}