
package myVoting2;

import com.mongodb.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Date;
import java.util.TimeZone;

public class ModeratorService {
    private AtomicLong counter = new AtomicLong();
    String connectionURI = "mongodb://root:root@ds059471.mongolab.com:59471/cmpe273_db";
    MongoClientURI mongoClientURI = new MongoClientURI(connectionURI);
    MongoClient mongoClient = null;
    DB mongoDb = null;
    public static DBCollection mongoDbCollection = null;
    DBCursor mongoDbCursor = null;

    public ModeratorService() {
        try {
            mongoClient = new MongoClient(mongoClientURI);
            mongoDb = mongoClient.getDB("cmpe273_db");
            mongoDbCollection = mongoDb.getCollection("moderators");
        }
        catch(java.net.UnknownHostException e) {
            System.out.println("Exception Message is: "+e.getMessage());
        }
    }

    public Moderator getModerator(int id) {
        DBObject mongoDbQuery = new BasicDBObject("id", id);
        DBObject mongoDbObj = mongoDbCollection.findOne(mongoDbQuery);
        if(mongoDbObj != null) {
            return new Moderator(Integer.parseInt(mongoDbObj.get("id").toString()),mongoDbObj.get("name").toString(),
                    mongoDbObj.get("email").toString(),mongoDbObj.get("password").toString(),mongoDbObj.get("created_at").toString());
        }
        return null;
    }

    public Moderator createModerator(String name, String email, String password) {
        mongoDbCursor = mongoDbCollection.find();
        try {
            while(mongoDbCursor.hasNext()) {
                DBObject myObj=mongoDbCursor.next();
                if(!mongoDbCursor.hasNext()) {
                    counter.set(Long.parseLong(myObj.get("id").toString()));
                }
            }
        } finally {
            mongoDbCursor.close();
        }
        long tempId=counter.incrementAndGet();
        TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'dd:HH:mm.sss'Z'");
        df.setTimeZone(tz);
        String myLocalTime = df.format(new Date());
        Moderator myModerator=new Moderator((int)tempId, name, email, password, myLocalTime);
        BasicDBObject document = new BasicDBObject();
        document.append("id", (int)tempId);
        document.append("name", name);
        document.append("email", email);
        document.append("password", password);
        document.append("created_at", myLocalTime);
        mongoDbCollection.insert(document);
        return myModerator;
    }

    public Moderator updateModerator(int id, String name, String email, String password) {
        DBObject mongoDbQuery = new BasicDBObject("id", id);
        DBObject mongoDbObj = mongoDbCollection.findOne(mongoDbQuery);
        if(mongoDbObj != null) {
            Moderator myModerator=new Moderator(Integer.parseInt(mongoDbObj.get("id").toString()),mongoDbObj.get("name").toString(),
                    mongoDbObj.get("email").toString(),mongoDbObj.get("password").toString(),mongoDbObj.get("created_at").toString());
            BasicDBObject updateQuery = new BasicDBObject();
            BasicDBObject searchQuery = new BasicDBObject();

            if(!(name.equalsIgnoreCase("*"))) {
                myModerator.setName(name);
                searchQuery.append("name", name);
            }
            if(!(email.equalsIgnoreCase("*"))) {
                myModerator.setEmail(email);
                searchQuery.append("email", email);
            }
            if(!(password.equalsIgnoreCase("*"))) {
                myModerator.setPassword(password);
                searchQuery.append("password", password);
            }
            updateQuery.append("$set",searchQuery);
            mongoDbCollection.update(mongoDbQuery, updateQuery);
            return myModerator;
        }
        return null;
    }

    public int checkIfId(int id) {
        DBObject mongoDbQuery = new BasicDBObject("id", id);
        DBObject mongoDbObj = mongoDbCollection.findOne(mongoDbQuery);
        if(mongoDbObj != null) {
            return 1;
        }
        else {
            return 0;
        }
    }
}

