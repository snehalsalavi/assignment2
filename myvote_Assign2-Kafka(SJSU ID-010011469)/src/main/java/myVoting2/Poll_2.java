package myVoting2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
public class Poll_2 {

     String id;
     String question;
     String choice[];
     String started_At;
     String expired_At;
    //public int [] results;

    @JsonCreator
    public Poll_2(@JsonProperty("id") String id, @JsonProperty("question") String question, @JsonProperty("started_at") String started_At,
                  @JsonProperty("expired_at") String expired_at, @JsonProperty("choice") String choice[]) {
        this.id = id;
        this.question = question;
        this.started_At = started_At;
        this.expired_At = expired_at;
        this.choice = choice;
        //this.results = new int[choice.length];
    }

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getStarted_at() {
        //SimpleDateFormat dt1 =new SimpleDateFormat("dd-MM-yy:HH:mm:SS");
        return started_At;
    }
    public String getExpired_at() {
        //SimpleDateFormat dt1 =new SimpleDateFormat("dd-MM-yy:HH:mm:SS");
        return expired_At;
    }

    public String[] getChoice() {
        return choice;
    }
}