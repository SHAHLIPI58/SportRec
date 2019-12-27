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
@WebServlet("/Startup")

/*  
startup servlet Intializes HashMap in SaxParserDataStore
*/

public class Startup extends HttpServlet
{

	public void init() throws ServletException
    {
		MySqlDataStoreUtilities.createTables();
		ArrayList<String> classes=getClasses();
		DataThread dt=new DataThread(classes);
		dt.start();
	}

public ArrayList<String> getClasses(){
	ArrayList<String> genreIds=new ArrayList();
	try{
		
		String apikey="VHwQHFGYP2rPLFJ04dyqWww5UIxmAS8G";
		String classificationName="sports";	
		int num_sports=0;
		String ticketmaster="https://app.ticketmaster.com/discovery/v2/classifications.json?apikey=VHwQHFGYP2rPLFJ04dyqWww5UIxmAS8G"; 
		URI uri=new URI(ticketmaster);
		StringBuilder sb1 = new StringBuilder();
		URL url = new URL(uri.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		if(conn.getResponseCode()==200)
		{
			InputStream is = conn.getInputStream();
			BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
			String line;
			while ((line = reader.readLine()) != null) {
				sb1.append(line).append('\n');
			}
			conn.disconnect();
		}
		JSONObject data1 = new JSONObject(sb1.toString());
		JSONObject container1=data1.getJSONObject("_embedded");
		JSONArray classes=container1.getJSONArray("classifications");
		int num_classes=classes.length();
		for(int c=0;c<num_classes;c++){
			JSONObject elem=classes.getJSONObject(c);
			if(elem.has("segment") && elem.getJSONObject("segment").getString("name").equals("Sports")){
				JSONObject seg=elem.getJSONObject("segment");
				JSONObject seg_emb=seg.getJSONObject("_embedded");
				JSONArray sportids=seg_emb.getJSONArray("genres");
				num_sports=sportids.length();
				for(int s=0;s<num_sports;s++)
				{
					String id=sportids.getJSONObject(s).getString("id");
					String name=sportids.getJSONObject(s).getString("name");
					genreIds.add(sportids.getJSONObject(s).getString("id"));
				}
				break;
			}
		}
}
catch(Exception e){
	e.printStackTrace();
}
return genreIds;
}
}
