package myVoting2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

public class Poll{
    private final String id;
    private final String question;
    private final String started_At;
    private final String expired_At;
    private final String[] choice;
    private int[] results;

    @JsonCreator
    public Poll(@JsonProperty("id") String id, @JsonProperty("question") String question, @JsonProperty("started_at") String started_At,
                @JsonProperty("expired_at") String expired_at ,@JsonProperty("choice") String choice[]) {
        this.id = id;
        this.question = question;
        this.started_At = started_At;
        this.expired_At = expired_at;
        this.choice = choice;
        this.results = new int[choice.length];
    }
    public Poll(String id, String question, String started_at,
                     String expired_at, String[] choice, int[] results) {
        this.id = id;
        this.question = question;
        this.started_At = started_at;
        this.expired_At = expired_at;
        this.choice = choice;
        this.results = results;
    }

    public void setResults(int[] results) {
        this.results=results;
    }

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getStarted_at() {
        return started_At;
    }

    public String getExpired_at() {
        return expired_At;
    }

    public String[] getChoice() {
        return choice;
    }

    public int[] getResults() {
        return results;
    }



}