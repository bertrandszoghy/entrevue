package service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {

	@Autowired
    private JavaMailSender mailSender;

    public String sendSimpleMessage(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("bertrand.szpghy@levio.ca");
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);
        mailSender = new JavaMailSenderImpl();
        ((JavaMailSenderImpl) mailSender).setHost("localhost");
	    ((JavaMailSenderImpl) mailSender).setPort(587);

	    ((JavaMailSenderImpl) mailSender).setUsername("bertrand.szoghy@levio.ca");
	    ((JavaMailSenderImpl) mailSender).setPassword("password");

	    Properties props = ((JavaMailSenderImpl) mailSender).getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "false");
	    props.put("mail.smtp.starttls.enable", "false");
	    props.put("mail.debug", "true");

        mailSender.send(message);
        
        return "sent";
    }

	@Override
	public String sendMailWithAttachment(String to, String subject, String text) {
		// TODO Auto-generated method stub
		return null;
	}
}