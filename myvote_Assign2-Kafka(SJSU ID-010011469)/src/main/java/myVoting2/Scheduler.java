package myVoting2;

	import java.text.SimpleDateFormat;
    import kafka.javaapi.producer.Producer;
    import kafka.producer.KeyedMessage;
    import java.util.List;
	import org.springframework.scheduling.annotation.Scheduled;
	import org.springframework.stereotype.Component;

@Component
	public class Scheduler {

		private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		public static Producer<Integer, String> producer= VotingProducer.getResultProducer();
		PollService ps= new PollService();

		@Scheduled(fixedRate = 50000)
		public void reportResultsOfExpiredPolls() {
			List<String> expiredPollsList=ps.getResultsOfExpiredPolls();

			if(expiredPollsList.size()!=0){

				for(int i=0;i<expiredPollsList.size();i++){
					KeyedMessage<Integer, String> data = new KeyedMessage<>(VotingProducer.topic, expiredPollsList.get(i));
					producer.send(data);
				}

				//producer.close();
				System.out.println("***-----Producer has sent the results of expired Polls-----***");
			}
			else{
				
				System.out.println("No expired poll is found in database...");
			}
		} 
	}


