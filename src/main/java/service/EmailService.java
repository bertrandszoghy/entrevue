package service;

//Interface
public interface EmailService {

 // To send a simple email
 String sendSimpleMessage(String to, String subject, String text);

 // To send an email with attachment
 String sendMailWithAttachment(String to, String subject, String text);
}