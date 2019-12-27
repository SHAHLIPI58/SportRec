import java.io.*;

import javax.servlet.http.*;
import javax.servlet.RequestDispatcher;
import java.util.*;
import java.text.*;

import java.sql.*;

import java.io.IOException;
import java.io.*;



public class AjaxUtility {
	StringBuffer sb = new StringBuffer();
	boolean namesAdded = false;
	static Connection conn = null;
    static String message;
	public static String getConnection()
	{

		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/exampledatabase","root","root");							
			message="Successfull";
			return message;
		}
		catch(SQLException e)
		{
			 message="unsuccessful";
		     return message;
		}
		catch(Exception e)
		{
			 message="unsuccessful";
		     return message;
		}
	}
	
	public  StringBuffer readdata(String searchId)
	{	
		HashMap<String,Event> data;
		data=getData();
		
 	    Iterator it = data.entrySet().iterator();	
        while (it.hasNext()) 
	    {
                    Map.Entry pi = (Map.Entry)it.next();
			if(pi!=null)
			{
				Event p=(Event)pi.getValue();                   
                if (p.getName().toLowerCase().startsWith(searchId))
                {
					
                        sb.append("<event>");
                        sb.append("<eventName>" + p.getName() + "</eventName>");
                        sb.append("</event>");
                }
			}
       }
	   
	   return sb;
	}
	
	public static HashMap<String,Event> getData()
	{
		HashMap<String,Event> hm=new HashMap<String,Event>();
		try
		{
			getConnection();
			Statement stmt=conn.createStatement();
		    String selectEvent="select * from  eventdetails";
			ResultSet rs = stmt.executeQuery(selectEvent);
			while(rs.next())
			{	Event p = new Event(rs.getString("eventName"),rs.getString("generName"),rs.getString("date1"),rs.getString("image"),rs.getString("postcode"),rs.getString("city"),rs.getString("state"),rs.getString("venue"),rs.getString("address"),rs.getDouble("price"));
				hm.put(rs.getString("eventName"), p);
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return hm;			
	}
	
}
