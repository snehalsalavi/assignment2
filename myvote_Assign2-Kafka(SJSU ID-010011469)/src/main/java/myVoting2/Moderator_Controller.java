package myVoting2;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class Moderator_Controller {
    ModeratorService myModeratorService=new ModeratorService();
    private final AtomicLong counter = new AtomicLong();
    int i=1;
    HashMap<Integer, Moderator> moderatorMap=new HashMap<Integer, Moderator>();
    //private Date date =new Date();
    @RequestMapping(method=RequestMethod.POST,value={"/api/v1/moderators"},consumes= "application/json", produces="application/json")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ResponseEntity<Moderator> greeting(@RequestBody @Valid Moderator newModerator) {

        long tempId=counter.incrementAndGet();
        TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'dd:HH:mm.sss'Z'");
        df.setTimeZone(tz);
        String myLocalTime = df.format(new Date());

        Moderator moderator= myModeratorService.createModerator(newModerator.getName(),newModerator.getEmail(),newModerator.getPassword());

        return new ResponseEntity<Moderator>(moderator, HttpStatus.CREATED);
    }

    @RequestMapping(method=RequestMethod.GET, value={"/api/v1/moderators/{moderator_Id}"})
    public Moderator getModerator(@PathVariable String moderator_Id)
    {
        int id = Integer.parseInt(moderator_Id);
        Moderator moderator = myModeratorService.getModerator(id);
        return moderator;
    }

    @RequestMapping(method=RequestMethod.PUT,value={"/api/v1/moderators/{moderator_Id}"})
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ResponseEntity<Moderator> updateModerator(@PathVariable String moderator_Id, @RequestBody Moderator newModerator){
        String name="*";
        String email="*";
        String password="*";
        int id = Integer.parseInt(moderator_Id);
        if(newModerator.getEmail()!=null && !(newModerator.getEmail().equalsIgnoreCase("*"))) {
            email=newModerator.getEmail();
        }
        if(newModerator.getName()!=null && !(newModerator.getName().equalsIgnoreCase("*"))) {
            name=newModerator.getName();
        }
        if(newModerator.getPassword()!=null && !(newModerator.getPassword().equalsIgnoreCase("*"))) {
            password=newModerator.getPassword();
        }
        if(newModerator.getName()!=null || newModerator.getEmail()!=null || newModerator.getPassword()!=null) {
            Moderator myModerator=myModeratorService.updateModerator(id,name, email, password);
            return new ResponseEntity<Moderator>(myModerator, HttpStatus.CREATED);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }



}