package myVoting2;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicLong;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PollService {
    private AtomicLong counter = new AtomicLong();
    String textUri = "mongodb://root:root@ds059471.mongolab.com:59471/cmpe273_db";
    MongoClientURI uri = new MongoClientURI(textUri);
    MongoClient m = null;
    DB db = null;

    TimeZone timeZone = TimeZone.getTimeZone("UTC");
    DBCollection collection = null;
    DBCursor cursor = null;
    ModeratorService myModeratorService=new ModeratorService();
    

    public PollService() {
        try {
            m = new MongoClient(uri);
            db = m.getDB("cmpe273_db");
            collection = db.getCollection("polls");
            
        }
        catch(java.net.UnknownHostException e) {
            System.out.println("Custom Message "+e.getMessage());
        }
    }

    public Poll_2 createPoll(String id, String question, String started_at, String expired_at, String[] choice) {
        cursor = collection.find();
        try {
            while(cursor.hasNext()) {
                DBObject myObj=cursor.next();
                if(!cursor.hasNext()) {
                    counter.set(Long.parseLong(myObj.get("id").toString(),36));
                }
            }
        } finally {
            cursor.close();
        }
        if(myModeratorService.checkIfId(Integer.parseInt(id)) == 1) {
            long tempId=counter.incrementAndGet();
            String pollId = java.lang.Long.toString(tempId, 36);
            Poll_2 myPoll=new Poll_2(pollId, question, started_at, expired_at, choice);
            Poll myVotedPoll=new Poll(pollId, question, started_at, expired_at, choice);
            BasicDBObject document = new BasicDBObject();
            document.append("mid", id);
            document.append("id", pollId);
            document.append("question", question);
            document.append("started_at", started_at);
            document.append("expired_at", expired_at);
            document.append("choice",choice);
            document.append("results",myVotedPoll.getResults());
            collection.insert(document);
            return myPoll;
        }
        else {
            return null;
        }
    }

    public Poll_2 viewPollWithoutResult(String id) {
        DBObject query = new BasicDBObject("id", id);
        DBObject dbObj = collection.findOne(query);
        if(dbObj != null) {
            BasicDBList choices = (BasicDBList) dbObj.get("choice");
            String[] strChoice = new String[choices.size()];
            int i = 0;
            for (Object choice : choices) {
                strChoice[i++] = (String) choice;
            }

            return new Poll_2(dbObj.get("id").toString(),dbObj.get("question").toString(),dbObj.get("started_at").toString(),
                    dbObj.get("expired_at").toString(),strChoice);
        }
        return null;
    }

    public Poll viewPollWithResult(int id, String pollId) {
        DBObject query = new BasicDBObject("mid", id).append("id", pollId);
        DBObject dbObj = collection.findOne(query);
        if(dbObj != null) {
            BasicDBList choices = (BasicDBList) dbObj.get("choice");
            String[] strChoice = new String[choices.size()];
            int i = 0;
            for (Object choice : choices) {
                strChoice[i++] = (String) choice;
            }

            BasicDBList resultsNew = (BasicDBList) dbObj.get("results");
            int[] strResult = new int[resultsNew.size()];
            i = 0;
            for (Object result : resultsNew) {
                strResult[i++] = Integer.parseInt(result.toString());
            }

            return new Poll(dbObj.get("id").toString(),dbObj.get("question").toString(),dbObj.get("started_at").toString(),
                    dbObj.get("expired_at").toString(),strChoice);
        }
        return null;
    }

    public ArrayList listAllPolls(int id) {
        ArrayList votedPolls=new ArrayList();
        BasicDBObject query = new BasicDBObject();
        query.put("mid", id);
        cursor = collection.find(query);

        if(myModeratorService.checkIfId(id) == 1) {
            while(cursor.hasNext()) {
                DBObject dbObj = cursor.next();
                BasicDBList choices = (BasicDBList) dbObj.get("choice");
                String[] strChoice = new String[choices.size()];
                int i = 0;
                for (Object choice : choices) {
                    strChoice[i++] = (String) choice;
                }

                BasicDBList resultsNew = (BasicDBList) dbObj.get("results");
                int[] strResult = new int[resultsNew.size()];
                i = 0;
                for (Object result : resultsNew) {
                    strResult[i++] = Integer.parseInt(result.toString());
                }

                Poll myVotedPoll = new Poll(dbObj.get("id").toString(),dbObj.get("question").toString(),dbObj.get("started_at").toString(),
                        dbObj.get("expired_at").toString(),strChoice);
                votedPolls.add(myVotedPoll);
            }
            return votedPolls;
        }
        return null;
    }

    public int deletePoll(int id, String pollId) {
        DBObject query = new BasicDBObject("mid", id).append("id", pollId);
        DBObject dbObj = collection.findOne(query);
        if(dbObj != null) {
            collection.remove(dbObj);
            return 1;
        }
        else {
            return 0;
        }
    }

    public int votePoll(String pollId, int choiceIndex) {
        DBObject query = new BasicDBObject("id", pollId);
        DBObject dbObj = collection.findOne(query);
        if(dbObj != null) {
            BasicDBList choices = (BasicDBList) dbObj.get("choice");
            String[] strChoice = new String[choices.size()];
            int i = 0;
            for (Object choice : choices) {
                strChoice[i++] = (String) choice;
            }

            BasicDBList resultsNew = (BasicDBList) dbObj.get("results");
            int[] strResult = new int[resultsNew.size()];
            i = 0;
            for (Object result : resultsNew) {
                strResult[i++] = Integer.parseInt(result.toString());
            }

            Poll myVotedPoll = new Poll(dbObj.get("id").toString(),dbObj.get("question").toString(),dbObj.get("started_at").toString(),
                    dbObj.get("expired_at").toString(),strChoice);
            int[] results=myVotedPoll.getResults();
            if(choiceIndex<results.length) {
                results[choiceIndex]+=1;
                myVotedPoll.setResults(results);
                BasicDBObject updateQuery = new BasicDBObject();
                BasicDBObject searchQuery = new BasicDBObject("results",results);
                updateQuery.append("$set",searchQuery);
                collection.update(query, updateQuery);
                return 1;
            }
            else {
                return 0;
            }
        }
        else {
            return 0;
        }
    }

    
  //Title :  Results of expired polls are given to kafka broker
  	public List<String> getResultsOfExpiredPolls(){

  		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd'T'hh:mm:ss.ms'Z'");
  		ft.setTimeZone(timeZone);
  		List<String> result=new LinkedList<String>();
  		DBCursor cur =	collection.find();

  		while(cur.hasNext()){

  			String msg=null;
  			DBObject dbo=cur.next();
  			try {
  				String currentTime=ft.format((new Date()));

  				String x=(String) dbo.get("expired_at");
  				System.out.println("extracted expiry time from db : "+x);
  				Date temp=ft.parse(x);
  				String expiryTime=ft.format(temp);
  				Date p = ft.parse(expiryTime);
  				Date c=ft.parse(currentTime);

  				if(c.compareTo(p) <0){
  					continue;

  				}else if(p.compareTo(c)==0){ // both dates are same
  					if(c.getTime() < p.getTime()){

  						continue;  	

  					}else if(p.getTime() == c.getTime()){
  						msg=createProducerMessage(dbo);

  					}else{
  						msg=createProducerMessage(dbo);
  					}
  				}else{
  					msg=createProducerMessage(dbo);
  				}

  			} catch (java.text.ParseException e) {
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			} 

  			if(msg!=null){
  				System.out.println(" Message before adding into list : " + msg);
  				result.add(msg);
  			}
  		}
  		return result;
    }

  	public String createProducerMessage(DBObject dbo){

  		String sjsuId="010011469";
  		String msg=null;
  		DBObject query = new BasicDBObject("moderator_id", dbo.get("moderator_id"));
  		DBObject d =	ModeratorService.collection.findOne(query);
  		String email=(String) d.get("email");
  		msg=email+":"+sjsuId+":Poll Result [";

  		try {
  			JSONArray jr = (JSONArray) new JSONParser().parse(dbo.get("results").toString());
  			JSONArray jc =(JSONArray)new JSONParser().parse( dbo.get("choice").toString());

  			int i=0;
  			while(i<jr.size()){
  				System.out.println(jc.get(i)+"  =  "+jr.get(i));
  				msg=msg+jc.get(i)+" = "+jr.get(i)+",";
  				i++;
  			}
  			msg=msg+"]";
  			System.out.println(msg);
  		} catch (ParseException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  		return msg;
    }
  }

    
    
    
    
    


