import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.QueryBuilder;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.AggregationOutput;
import java.util.*;
              
			  
public class MongoDBDataStoreUtilities
{
static DBCollection myReviews;
static MongoClient mongo;
public static DBCollection getConnection()
{

mongo = new MongoClient("localhost", 27017);

DB db = mongo.getDB("EventsCustomerReviews");
myReviews= db.getCollection("myReviews");	
return myReviews; 
}

public static String insertReview(Review review)
{
	try
		{		
			myReviews=getConnection();
			BasicDBObject doc = new BasicDBObject("title", "myReviews").
				append("userName", review.getUserId()).
				append("userage",review.getuserage()).
				append("gender",review.getgender()).
				append("occupation",review.getoccupation()).
				append("EventName", review.geteventname()).
				append("SportName", review.getsportname()).
				append("TicketPrice",review.getticketprice()).
				append("EventVenue", review.geteventvenue()).
				append("EventAddress", review.geteventaddress()).
				append("EventCity", review.geteventcity()).
				append("EventState",review.geteventstate()).
				append("EventZipCode",review.geteventzip()).
				append("reviewRating",Integer.parseInt(review.getReviewRating())).
				append("reviewDate", review.getReviewDate()).
				append("reviewText", review.getReviewText());	
			myReviews.insert(doc);
			return "Successfull";
		}
		catch(Exception e)
		{
			e.printStackTrace();
		return "UnSuccessfull";
		}	
		
}

public static HashMap<String, ArrayList<Review>> selectReview(String eventname)
{	
	HashMap<String, ArrayList<Review>> reviews=null;
	
	try
		{

	getConnection();
	DBObject query=QueryBuilder.start("EventName").is(eventname).get();
	DBCursor cursor = myReviews.find(query);
	reviews=new HashMap<String, ArrayList<Review>>();
	while (cursor.hasNext())
	{
			BasicDBObject obj = (BasicDBObject) cursor.next();				
	
		   if(!reviews.containsKey(obj.getString("EventName")))
			{	
				ArrayList<Review> arr = new ArrayList<Review>();
				reviews.put(obj.getString("EventName"), arr);
			}
			ArrayList<Review> listReview = reviews.get(obj.getString("EventName"));	
			String addr=obj.getString("EventCity")+obj.getString("EventState")+obj.getString("EventZipCode");
			Review review =new Review(obj.getString("userName"),obj.getString("EventName"),obj.getDouble("TicketPrice"),addr,obj.getString("reviewDate"),obj.getString("reviewRating"),obj.getString("reviewText"));
			//add to review hashmap
			listReview.add(review);
			
	}
	return reviews;
	}
	catch(Exception e)
	{
		e.printStackTrace();
		 reviews=null;
		 return reviews;	
	}	
	}
	
}	