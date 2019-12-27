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

public class DataThread implements Runnable {
	private ArrayList<String> genreIds;
	private Thread t;
	DataThread(ArrayList<String> genreIds) {
	   this.genreIds=genreIds;
	}
   
   public void run() {
	   
	   try{
			for(int gen=0;gen<genreIds.size();gen++)
			{	
				System.out.println(genreIds.size());
				String apikey="VHwQHFGYP2rPLFJ04dyqWww5UIxmAS8G";
				String classificationName="sports";	
				String genreId=(String)genreIds.get(gen);
				StringBuilder sb = new StringBuilder();
				String ticketmaster="https://app.ticketmaster.com/discovery/v2/events.json?classificationName=sports&size=199&apikey="+apikey+"&genreId="+genreId+"";
				URI uri=new URI(ticketmaster);
				URL url = new URL(uri.toString());
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				if(conn.getResponseCode()==200)
				{
					InputStream is = conn.getInputStream();
					BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
					String line;
					while ((line = reader.readLine()) != null) {
						sb.append(line).append('\n');
					}
					conn.disconnect();
				}
				System.out.println(url);
				JSONObject data = new JSONObject(sb.toString());
				
				if(data.has("_embedded")){
					JSONObject container=data.getJSONObject("_embedded");
					JSONArray events=container.getJSONArray("events");
					String sportname=events.getJSONObject(0).getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").getString("name");
					String TOMCAT_HOME = System.getProperty("catalina.home");
					String filepath=TOMCAT_HOME+"\\webapps\\sportewa\\WEB-INF\\classes\\python\\";
					FileWriter file = new FileWriter(filepath+"items.json");
					MySqlDataStoreUtilities.deleteOld(sportname);
					MySqlDataStoreUtilities.insertEvents(events.toString(),file);
					genreIds.remove(genreId);
					RecombeeThread rt=new RecombeeThread();
					rt.start();
					Thread.sleep(50);
				}
			}
		}
		catch(Exception e){
			System.out.println("Exception occured:");
			e.printStackTrace();
			DataThread dt=new DataThread(genreIds);
			dt.start();
		}
   }
   
   public void start () {
      if (t == null) {
         t = new Thread (this);
         t.start ();
      }
   }
}