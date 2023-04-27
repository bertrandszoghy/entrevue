package ca.levio.entrevue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ca.levio.entrevue.entity.Referent;
import ca.levio.entrevue.repository.ReferentRepository;
import service.EmailService;
import service.EmailServiceImpl;

@SpringBootApplication
public class EntrevueApplication {
	
	private static final Logger log = LoggerFactory.getLogger(EntrevueApplication.class);
	@Autowired 
	private static EmailServiceImpl emailService;

	public static void main(String[] args) {
		SpringApplication.run(EntrevueApplication.class, args);
				
		emailService = new EmailServiceImpl();
		String status = emailService.sendSimpleMessage("bertrandszoghy@gmail.com","test email","test subject");
		log.info("Email sent status is: " + status);
		
	}
	
	@Bean
	public CommandLineRunner commandlineRunner(ReferentRepository referentRepository) {
		
		return (args) -> {
			referentRepository.save(new Referent(1,"Jean","Côté","jeancote@levio.ca",true,3));
			referentRepository.save(new Referent(2,"Paul","Côté","paulcote@levio.ca",true,2));
			referentRepository.save(new Referent(3,"Éric","Côté","ericcote@levio.ca",true,3));
			referentRepository.save(new Referent(4,"Marc","Côté","marccote@levio.ca",true,3));
			referentRepository.save(new Referent(3,"Donald","Côté","donaldcote@levio.ca",true,3));

			for (Referent referent : referentRepository.findAll()) {
				log.info("The referent is: " + referent.toString());
			}
		};
	}

}
