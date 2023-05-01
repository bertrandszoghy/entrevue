package ca.levio.entrevue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ca.levio.entrevue.entity.Referent;
import ca.levio.entrevue.repository.ReferentRepository;
import service.EmailService;
import service.EmailServiceImpl;

@SpringBootApplication
public class EntrevueApplication {
	
	private static final Logger log = LoggerFactory.getLogger(EntrevueApplication.class);
	@Autowired 
	private static EmailServiceImpl emailService;
	private static List<Referent> interviewers = new ArrayList<Referent>();
	private static ReferentRepository referentRepository;

	public static void main(String[] args) {
		SpringApplication.run(EntrevueApplication.class, args);
				
		// test email server
		emailService = new EmailServiceImpl();
		String status = emailService.sendSimpleMessage("bertrandszoghy@gmail.com","test email","test subject");
		log.info("Test email sent status is: " + status);
		
		
	}
	
	// create in-memory database with entity and repository
	@Bean
	public CommandLineRunner commandlineRunner(ReferentRepository referentRepository) {
		
		this.referentRepository = referentRepository;
		return (args) -> {
			referentRepository.save(new Referent(1,"Jean","Côté","jeancote@levio.ca",true,"EMERITE"));
			referentRepository.save(new Referent(2,"Yves","Côté","yvescote@levio.ca",false,"EMERITE"));
			referentRepository.save(new Referent(3,"Gaétan","Côté","gaetancote@levio.ca",false,"EMERITE"));
			referentRepository.save(new Referent(4,"Jimmy","Côté","jimmycote@levio.ca",false,"EMERITE"));
			referentRepository.save(new Referent(5,"Alex","Côté","alexcote@levio.ca",true,"SENIOR"));
			referentRepository.save(new Referent(6,"Paul","Côté","paulcote@levio.ca",false,"SENIOR"));
			referentRepository.save(new Referent(7,"Gaston","Côté","gastoncote@levio.ca",false,"SENIOR"));
			referentRepository.save(new Referent(8,"Claude","Côté","claudecote@levio.ca",false,"SENIOR"));
			referentRepository.save(new Referent(9,"Éric","Côté","ericcote@levio.ca",true,"INTERMEDIAIRE"));
			referentRepository.save(new Referent(10,"Pierre","Côté","pierrecote@levio.ca",false,"INTERMEDIAIRE"));
			referentRepository.save(new Referent(11,"Jacques","Côté","jacquescote@levio.ca",false,"INTERMEDIAIRE"));
			referentRepository.save(new Referent(12,"Ben","Côté","bencote@levio.ca",false,"INTERMEDIAIRE"));
			referentRepository.save(new Referent(13,"Marc","Côté","marccote@levio.ca",true,"JUNIOR"));
			referentRepository.save(new Referent(14,"Donald","Côté","donaldcote@levio.ca",false,"JUNIOR"));
			referentRepository.save(new Referent(15,"Ed","Côté","edcote@levio.ca",false,"JUNIOR"));
			referentRepository.save(new Referent(16,"Steve","Côté","stevecote@levio.ca",false,"JUNIOR"));

			for (Referent referent : referentRepository.findAll()) {
				if(referent.isActive()) {
					//log.info("The referent is not available to interview: " + referent.toString());
				}
				else {
					log.info("The referent can interview: " + referent.toString());
					interviewers.add(referent);
				}
			}
		};
	}

	@RestController
	public class InterviewController {

		private static final String template = "Hello, %s!";
		private final AtomicLong counter = new AtomicLong();
		
		@Value("${app.version}")
		private String appVersion;

		// show app version
		@GetMapping("/version")
		public String version() {
			// http://localhost:8080/version		
			return appVersion; 
		}
		
		// show list of all employees who are not active
		@GetMapping("/disponible")
		public String disponible() {
			// http://localhost:8080/disponible
			String response = "Liste de candidats disponibles:<br>";
			
			for (Referent referent : referentRepository.findAll()) {
				if(!referent.isActive()) {
					response += referent.toString() + "<br>";
				}
			}
			
			return response; 
		}
		
		// provide a job candidate's expertise and return all persons who 
		// could interview him. 
		// parameter must be in the ENUMs of ca.levio.entrevue.ExpertiseType
		// i.e. JUNIOR, INTERMEDIAIRE, SENIOR, EMERITE
		// example: http://localhost:8080/demande/INTERMEDIAIRE
		@GetMapping("/demande/{expertise}")
	    public String getListReferents(@PathVariable("expertise") String expertise) {
			String result = null;
			for (ExpertiseType expertiseType : ExpertiseType.values()) {
		        if (expertiseType.name().equalsIgnoreCase(expertise)) {
		            result = "The candidate's expertise is: " + expertise + "<br>";
		            log.info(result);
		            break;
		        }
		    }	
			
			if(null == result) {
				log.info("Argument not found in ExpertiseType enums: " + expertise);
				return result;
			}
			
			List<Referent> listReferents = new ArrayList<Referent>(); 
			
			for (Referent referent : referentRepository.findAll()) {			
				
				if((!referent.isActive()) && (!referent.getExpertise().equals("JUNIOR"))){
					
					if(expertise.equals("JUNIOR")) {
						listReferents.add(referent);
					}
					else if(expertise.equals("INTERMEDIAIRE")) {
						if((referent.getExpertise().equals("SENIOR")) || referent.getExpertise().equals("EMERITE")) {
							listReferents.add(referent);
						}
					}
					else {
						if(referent.getExpertise().equals("EMERITE")) {
							listReferents.add(referent);
						}
					}
				}
			}
			
			
			// now all you need is to randomly choose in listReferents to get two appropriate referents
			Random rand = new Random();
			int numberOfReferents = 2;
			if(listReferents.size()>= numberOfReferents)  {
				 

				    for (int i = 0; i < numberOfReferents; i++) {
				        int randomIndex = rand.nextInt(listReferents.size());
				        Referent randomReferent = listReferents.get(randomIndex);
				        listReferents.remove(randomIndex);
				        result += "This referent can interview: " + randomReferent.getFirstName() + " " + randomReferent.getLastName() + " " + randomReferent.getExpertise() + " " + randomReferent.getEmailAddress() + "<br>";
				    }
				
			}
			
			return result;
	    }
	}
	
}
