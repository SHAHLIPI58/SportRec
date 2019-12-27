import java.util.*;

public class Event{
	String eventname="",sportname="",venue="",city="",address="",date="",imageurl="",postcode="",state="";
	Double price=0.0;
	int ordernum;
	String purchase_date="";
	
	
	
	Event(String eventname, String sportname, String date, String imageurl,String postcode,String city,String state, String venue, String address, Double price)
	{
		this.sportname=sportname;
		this.eventname=eventname;
		this.date=date;
		this.imageurl=imageurl;
		this.postcode=postcode;
		this.city=city;
		this.state=state;
		this.venue=venue;
		this.address=address;
		this.price=price;	 
	}
	
	Event(int ordernum,String eventname, String sportname,String purchase_date,String postcode,String city,String state, String venue, String address, Double price)
	{
		this.sportname=sportname;
		this.eventname=eventname;
		this.purchase_date=purchase_date;
		this.postcode=postcode;
		this.city=city;
		this.state=state;
		this.venue=venue;
		this.address=address;
		this.price=price;
		this.ordernum=ordernum;
	}
	Event(){}
	public String getName(){
		return eventname;
	}
	public double getPrice(){
		return price;
	}
	public String getImage(){
		return imageurl;
	}
	public String getCity(){
		return venue+" , "+address+" , "+city+" , "+state+" , "+postcode;
	}
	public String getDate(){
		return date;
	}
	public String getSport(){
		return sportname;
	}
	public String getPost(){
		return postcode;
	}
	
	public String getcity(){
		return city;
	}
	public String getState(){
		return state;
	}
	public String getVenue(){
		return venue;
	}
	public String getAddress(){
		return address;
	}
	public String getPurchaseTime(){
		return purchase_date;
	}
	public int getOrderNum(){
		return ordernum;
	}
	
	
	
	
}