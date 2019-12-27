import java.sql.*;
import java.util.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.*;
import java.text.SimpleDateFormat;  
import java.util.Date; 

import java.nio.charset.StandardCharsets;
import org.json.*;
                	
public class MySqlDataStoreUtilities
{
static Connection conn = null;

public static void getConnection()
{

	try
	{
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/exampledatabase","root","root");							
	}
	catch(Exception e)
	{
	
	}
}

public static void createTables(){
	try{
		getConnection();
		String reg="CREATE TABLE IF NOT EXISTS Registration(username varchar(40),password varchar(40),repassword varchar(40),usertype varchar(40));";
		String event_details="Create table if not exists eventdetails(eventName varchar(150),generName varchar(150),date1 varchar(150),"+
								"image varchar(150),postcode varchar(150),city varchar(150),state varchar(150),venue varchar(150),"+
								"address varchar(150),price varchar(150));";
		String custorder="create table if not exists customerorders2(orderNum int,userName varchar(150),"
						+"eventName varchar(150),sportname varchar(150),purchaseDate varchar(150),venue varchar(500),"
						+"address varchar(500),city varchar(500),postcode varchar(10), state varchar(100),price double);";
		Statement stmt=conn.createStatement();
		stmt.execute(reg);
		stmt.execute(event_details);
		stmt.execute(custorder);
	}
	catch(Exception e){
		System.out.println(e.getMessage());
	}
}

public static int getOrderNum(){
	int ans=0;
	try{
		getConnection();
		
		String query="select max(orderNum) as max from customerorders2;";
		Statement stmt=conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next())
		{	
			ans=rs.getInt("max");
		}
	}
	catch(Exception e){
		System.out.println(e.getMessage());
	}
	return ans;
}

public static ArrayList<String> getSports(){
	ArrayList<String> names=new ArrayList();
	try{
	getConnection();
	String sport="select distinct generName from eventdetails order by generName asc ;";
	Statement stmt=conn.createStatement();
	ResultSet rs = stmt.executeQuery(sport);
	while(rs.next())
	{	
		names.add(rs.getString("generName"));
	}
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return names;
}

public static void insertEvents(String event1,FileWriter file)
{
	try
	{
		getConnection();
		JSONArray events=new JSONArray(event1);
		JSONObject writeitems=new JSONObject();
		for(int ev=0;ev<events.length();ev++){
			String timezone="",state="",address="",city="",venue="",date="",time="",postcode="",eventname="",sportname="",imageurl="",evedate="";
			double longitude=0.0,latitude=0.0;
			JSONObject event=(JSONObject)events.get(ev);
			eventname=event.getString("name");
			sportname=event.getJSONArray("classifications").getJSONObject(0).getJSONObject("genre").getString("name");
			imageurl=event.getJSONArray("images").getJSONObject(0).getString("url");
			double price=23.0;
			if(event.getJSONObject("_embedded").has("venues")){
				JSONArray venue_array=event.getJSONObject("_embedded").getJSONArray("venues");
			if(venue_array.getJSONObject(0).has("state"))
				if(venue_array.getJSONObject(0).getJSONObject("state").has("name"))
					state=venue_array.getJSONObject(0).getJSONObject("state").getString("name");
			if(venue_array.getJSONObject(0).has("country"))
				if(venue_array.getJSONObject(0).getJSONObject("country").has("name"))
				{
					if(state!="")
						state=state+","+venue_array.getJSONObject(0).getJSONObject("country").getString("name");
					else
						state=venue_array.getJSONObject(0).getJSONObject("country").getString("name");
				}
			if(venue_array.getJSONObject(0).has("address"))
				if(venue_array.getJSONObject(0).getJSONObject("address").has("line1"))
					address=venue_array.getJSONObject(0).getJSONObject("address").getString("line1");
			if(venue_array.getJSONObject(0).has("city"))
				if(venue_array.getJSONObject(0).getJSONObject("city").has("name"))
					city=venue_array.getJSONObject(0).getJSONObject("city").getString("name");
			if(venue_array.getJSONObject(0).has("name"))
				venue=venue_array.getJSONObject(0).getString("name");
			if(venue_array.getJSONObject(0).has("postalCode"))
				postcode=venue_array.getJSONObject(0).getString("postalCode");
			if(venue_array.getJSONObject(0).has("location"))
			{
				JSONObject loc=venue_array.getJSONObject(0).getJSONObject("location");
				longitude=Double.parseDouble(loc.getString("longitude"));
				latitude=Double.parseDouble(loc.getString("latitude"));
			}
			}
			if(event.has("dates")&& event.getJSONObject("dates").has("start") ){
				if(event.getJSONObject("dates").getJSONObject("start").has("localDate"))
					date=event.getJSONObject("dates").getJSONObject("start").getString("localDate");
				if(event.getJSONObject("dates").getJSONObject("start").has("localTime"))
					time=event.getJSONObject("dates").getJSONObject("start").getString("localTime");
				if(event.getJSONObject("dates").getJSONObject("start").has("dateTime"))
					evedate=event.getJSONObject("dates").getJSONObject("start").getString("dateTime");
			}
			if(event.getJSONObject("dates").has("timezone") && event.getJSONObject("dates").get("timezone")!=JSONObject.NULL)
				timezone=event.getJSONObject("dates").getString("timezone");
			
			if(event.has("priceRanges") && event.get("priceRanges")!=JSONObject.NULL)
				if(event.getJSONArray("priceRanges").getJSONObject(0).has("min"))
					price=event.getJSONArray("priceRanges").getJSONObject(0).getDouble("min");
		String inserteventsquery ="insert into eventdetails VALUES(?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pst = conn.prepareStatement(inserteventsquery);
		pst.setString(1,eventname);
		pst.setString(2,sportname);
		pst.setString(3,date+" "+time+" "+timezone);
		pst.setString(4,imageurl);
		pst.setString(5,postcode);
		pst.setString(6,city);
		pst.setString(7,state);
		pst.setString(8,venue);
		pst.setString(9,address);
		pst.setDouble(10,price);
		pst.execute();
		if(evedate.equals(""))
		{
			if(!date.equals(""))
				evedate=date+"T";
			if(!time.equals(""))
				evedate=evedate+time+"Z";
			else
				evedate=evedate+"00:00:00Z";
		}
		JSONObject itemprops=new JSONObject();
		itemprops.put("eventDate",evedate);
		itemprops.put("city",city.trim().replace(' ','-'));
		itemprops.put("steventDate",(date.trim().replace('-','/')+" "+time.trim()+"("+timezone.trim()).replace(' ','-')+")");
		itemprops.put("generName",sportname.trim().replace(' ','-'));
		itemprops.put("address",address.trim().replace(' ','-'));
		itemprops.put("state",state.trim().replace(' ','-'));
		itemprops.put("venueName",venue.trim().replace(' ','-'));
		itemprops.put("postcode",postcode.trim().trim());
		itemprops.put("ticketPrice",price);
		itemprops.put("latitude",latitude);
		itemprops.put("longitude",longitude);
		itemprops.put("stlongitude",String.valueOf(longitude));
		itemprops.put("stlatitude",String.valueOf(latitude));
		itemprops.put("eventImage",imageurl.trim());
		writeitems.put(eventname.trim().replace(' ','-').replaceAll("[^a-zA-Z0-9_\\-:]",""),itemprops);
		//System.out.println("added:"+eventname);
		}
		file.write(writeitems.toString().trim());
		file.flush();
		
		
	}
	catch(Exception e)
	{
				e.printStackTrace();
	}
	
}

public static void deleteOld(String sportname)
{
	try
	{
		getConnection();
		String deleteOrderQuery ="Delete from eventdetails where generName='"+sportname+"';";
		PreparedStatement pst = conn.prepareStatement(deleteOrderQuery);
		pst.executeUpdate();
	}
	catch(Exception e)
	{
			e.printStackTrace();
	}
}





public static void insertOrder2(int order_num, String userName,String eventname,String sportname,String date,String venue,String addr,String city,String state,String postcode,double orderPrice)
{
	try
	{
	
		getConnection();
		String insertIntoCustomerOrderQuery = "INSERT INTO customerOrders2 "
		+ "VALUES (?,?,?,?,?,?,?,?,?,?,?);";	
			
		PreparedStatement pst = conn.prepareStatement(insertIntoCustomerOrderQuery);
		//set the parameter for each column and execute the prepared statement
		pst.setInt(1,order_num);
		pst.setString(2,userName);
		pst.setString(3,eventname);
		pst.setString(4,sportname);
		pst.setString(5,date);
		pst.setString(6,venue);
		pst.setString(7,addr);
		pst.setString(8,city);
		pst.setString(9,state);
		pst.setString(10,postcode);
		pst.setDouble(11,orderPrice);
		
		pst.execute();
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}		
}




public static HashMap<Integer, Event> selectOrder(String username)
{	

	HashMap<Integer, Event> orderedevents=new HashMap<Integer,Event>();
	try
	{					
		getConnection();
        //select the table 
		String selectOrderQuery ="select * from customerorders2 where userName='"+username+"' order by orderNum;";			
		PreparedStatement pst = conn.prepareStatement(selectOrderQuery);
		ResultSet rs = pst.executeQuery();	
		ArrayList<Event> orderList=new ArrayList<Event>();
		while(rs.next())
		{
			Event eve= new Event(rs.getInt("orderNum"),rs.getString("eventname"),rs.getString("sportname"),rs.getString("purchasedate"),rs.getString("postcode"),rs.getString("city"),rs.getString("state"),rs.getString("venue"),rs.getString("address"),rs.getDouble("price"));
			orderedevents.put(rs.getInt("orderNum"), eve);
		}					
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return orderedevents;
}


public static void insertUser(String username,String password,String repassword,String usertype)
{
	try
	{	

		getConnection();
		String insertIntoCustomerRegisterQuery = "INSERT INTO Registration(username,password,repassword,usertype) "
		+ "VALUES (?,?,?,?);";	
				
		PreparedStatement pst = conn.prepareStatement(insertIntoCustomerRegisterQuery);
		pst.setString(1,username);
		pst.setString(2,password);
		pst.setString(3,repassword);
		pst.setString(4,usertype);
		pst.execute();
	}
	catch(Exception e)
	{
	
	}	
}

public static HashMap<String,User> selectUser()
{	
	HashMap<String,User> hm=new HashMap<String,User>();
	try 
	{
		getConnection();
		Statement stmt=conn.createStatement();
		String selectCustomerQuery="select * from  Registration";
		ResultSet rs = stmt.executeQuery(selectCustomerQuery);
		while(rs.next())
		{	User user = new User(rs.getString("username"),rs.getString("password"),rs.getString("usertype"));
				hm.put(rs.getString("username"), user);
		}
	}
	catch(Exception e)
	{
	}
	return hm;
}
	
}	