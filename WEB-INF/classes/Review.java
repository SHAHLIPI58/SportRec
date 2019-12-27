import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class Review{
	String userid,eventname, sportname,eventvenue;
	String eventaddress,eventcity,eventzip,eventstate;
	String userage,gender;
	String occupation;
	double ticketprice;
	String reviewrating, reviewdate, reviewtext ;
	
	Review(String userid,String eventname,String sportname,double ticketprice,String eventvenue,String eventaddress,String eventcity
	,String eventstate,String eventzip,String userage,String gender,String occupation,String reviewrating,String reviewdate,String reviewtext)
	{	this.eventname=eventname;
		this.sportname=sportname;
		this.ticketprice=ticketprice;
		this.eventzip=eventzip;
		this.eventcity=eventcity;
		this.eventstate=eventstate;
		this.eventaddress=eventaddress;
		this.eventvenue=eventvenue;
		this.userid=userid;
		this.userage=userage;
		this.gender=gender;
		this.occupation=occupation;
		this.reviewdate=reviewdate;
		this.reviewrating=reviewrating;
		this.reviewtext=reviewtext;
	}
	public Review(String userid, String eventname, double ticketprice, String eventcity, String reviewdate, String reviewrating, String reviewtext) {
       this.eventname = eventname;
       this.ticketprice = ticketprice;
	   this.reviewdate=reviewdate;
	   this.userid=userid;
	   this.eventcity=eventcity;
       this.reviewrating = reviewrating;
       this.reviewtext = reviewtext;
    }
	
	String geteventname(){
		return eventname;
	}
	String getsportname(){
		return sportname;
	}
	double getticketprice(){
		return ticketprice;
	}
	String getUserId(){
		return userid;
	}
	String getuserage(){
		return userage;
	}
	String getgender(){
		return gender;
	}
	String getoccupation(){
		return occupation;
	}
	String geteventvenue(){
		return eventvenue;
	}
	String geteventaddress(){
		return eventaddress;
	}
	
	String geteventstate(){
		return eventstate;
	}
	String geteventzip(){
		return eventzip;
	}
	String geteventcity(){
		return eventcity;
	}
	String getReviewDate(){
		return reviewdate;
	}
	String getReviewRating(){
		return reviewrating;
	}
	String getReviewText(){
		return reviewtext;
	}
}