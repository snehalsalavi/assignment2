package myVoting2;

import javax.validation.Valid;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import org.springframework.web.bind.annotation.*;


@RestController
public class Poll_Controller {
    PollService myPollService = new PollService();
    int counter_i = 1;
    HashMap<Integer, ArrayList<String>> moderatorToPoll = new HashMap<Integer, ArrayList<String>>();
    HashMap<String, Poll> pollMapping = new HashMap<String, Poll>();
    HashMap<String, Poll_2> poll_2Mapping = new HashMap<String, Poll_2>();

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/moderators/{moderator_Id}/polls")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ResponseEntity<Poll_2> createPoll(@PathVariable String moderator_Id,
                                            @RequestBody @Valid Poll_2 p1) {
        String myStrId = Integer.toString(counter_i);
        int mod_Id = Integer.parseInt(moderator_Id);
        //String[] choiceArray = p1.getChoice().split(",");
        ArrayList<String> temp = new ArrayList<String>();

        if (p1.getQuestion() != null && p1.getStarted_at() != null &&
                p1.getExpired_at() != null && p1.getChoice() != null) {

            Poll_2 myPoll = myPollService.createPoll(myStrId, p1.getQuestion(), p1.getStarted_at(),
                    p1.getExpired_at(), p1.getChoice());

            if (myPoll != null) {
                return new ResponseEntity<Poll_2>(myPoll, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<Poll_2>(myPoll, HttpStatus.BAD_REQUEST);
            }

        /*if (moderatorToPoll.get(mod_Id) != null) {
            temp = moderatorToPoll.get(mod_Id);
        }
        //myStrId="1ADC2FZ";
        temp.add(myStrId);
        moderatorToPoll.put(mod_Id, temp);
        Poll p = new Poll(myStrId, p1.getQuestion(),p1.getStarted_at(), p1.getExpired_at(), p1.getChoice());

        Poll_2 mypoll = new Poll_2(myStrId, p1.getQuestion(),p1.getStarted_at(), p1.getExpired_at(), p1.getChoice());
        pollMapping.put(myStrId, p);
        poll_2Mapping.put(myStrId, mypoll);
        ArrayList<Integer> result = new ArrayList<Integer>();
        result.add(0);
        result.add(0);
        counter_i++;
        return mypoll;*/
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
        @RequestMapping(method = RequestMethod.GET, value = "/api/v1/polls/{poll_Id}")
        public Poll_2 viewPollWithoutResult (@PathVariable String poll_Id)
        {
            return myPollService.viewPollWithoutResult(poll_Id);
        }

        @RequestMapping(method = RequestMethod.GET, value = "/api/v1/moderators/{moderator_Id}/polls/{poll_Id}")
        public Poll viewPollWithResult (@PathVariable String moderator_Id, @PathVariable String poll_Id){
            //int pid = Integer.parseInt(poll_Id);
            int mid = Integer.parseInt(moderator_Id);
            ArrayList l = moderatorToPoll.get(mid);
            /*Poll p = null;
            if (l != null) {
                Iterator it = l.iterator();
                for (int j = 0; j < l.size(); j++) {
                    if (l.contains(poll_Id)) {
                        p = pollMapping.get(l.get(j));
                    }
                }
            }*/
            return myPollService.viewPollWithResult(mid, poll_Id);
        }

        @RequestMapping(method = RequestMethod.GET, value = "/api/v1/moderators/{moderator_Id}/polls")
        public ArrayList listAllPolls (@PathVariable String moderator_Id){
            int mid = Integer.parseInt(moderator_Id);
            /*ArrayList l = moderatorToPoll.get(mid);
            int k = 0;
            Poll p[] = new Poll[l.size()];
            if (l != null) {
                for (int j = 0; j < l.size(); j++) {

                    p[k] = pollMapping.get(l.get(j));
                    k++;
                }
            }*/
            return myPollService.listAllPolls(mid);
        }

        @RequestMapping(method = RequestMethod.DELETE, value = "/api/v1/moderators/{moderator_Id}/polls/{poll_Id}")
        @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
        public ResponseEntity deletePoll (@PathVariable ("moderator_Id")int moderator_Id, @PathVariable String poll_Id){
            int mid = myPollService.deletePoll(moderator_Id, poll_Id);
            /*ArrayList l = moderatorToPoll.get(mid);
            l.remove(poll_Id);
            moderatorToPoll.put(mid, l);*/
            if (mid == 1) {
                return new ResponseEntity(HttpStatus.valueOf(204));
            }
            else {
                return new ResponseEntity(HttpStatus.valueOf(400));
            }
        }

        @RequestMapping(method = RequestMethod.PUT, value = {"/polls/{poll_Id}"}, params = {"choice"})
        public ResponseEntity votePoll (@PathVariable String poll_Id,
                @RequestParam(value = "choice", defaultValue = "0") int choice){
            // int id = Integer.parseInt(poll_Id);
            int ch = myPollService.votePoll(poll_Id,choice);
            /*Poll p = pollMapping.get(poll_Id);
            p.results[ch]++;
            pollMapping.put(poll_Id, p);*/
            if(ch==1) {
                return new ResponseEntity(HttpStatus.valueOf(204));
            }
            else {
                return new ResponseEntity(HttpStatus.valueOf(400));
            }
        }
}