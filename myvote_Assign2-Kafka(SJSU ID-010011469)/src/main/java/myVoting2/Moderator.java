package myVoting2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class Moderator {
    private final int id;
    @NotBlank
    private String name;
    @Email @NotBlank
    private  String email;
    @NotBlank
    private  String password;
    private String created_at;

    @JsonCreator
    public Moderator(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("email") String email, @JsonProperty("password") String password, @JsonProperty("created_at") String created_at) {
        this.id = id;
        this.name = name;
        this.email=email;
        this.password=password;
        this.created_at= created_at;
    }

    public int getId() {
        return id;
    }
    public String getPassword() {
        return password;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at){this.created_at =created_at;}
    public void setEmail(String email){this.email=email; }
    public void setName(String name){this.name =name;}
    public void setPassword(String password){this.password=password; }

}