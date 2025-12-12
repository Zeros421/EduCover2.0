/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package educover.backend;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author aizik
 */

public class JavaMailAPI {
    public static void sendEmail(String recepient, String subject, String text){
        System.setProperty("https.protocols", "TLSv1.2");
        Properties properties = new Properties();
        
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        
        String emailAccount = "Simbaba2606@gmail.com";
        String password = "hyyq ghpj suye zgbl";
        
        javax.mail.Session session = Session.getInstance(properties, new Authenticator(){
                @Override
                protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(emailAccount, password);
                }
        
        });
        try {
            Message message = prepareMessage(session, emailAccount, recepient, subject, text);
            Transport.send(message);
            System.out.println("Email sent");
        }catch (MessagingException ex) {
        ex.printStackTrace();
        }
        
    }
    
    public static Message prepareMessage (Session session, String emailAccount, String recepient, String subject, String text){
        try{
            Message message = new MimeMessage(session);
            
            message.setFrom(new InternetAddress(emailAccount));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject(subject);
            message.setText(text);            
            return message;
        }catch(MessagingException ex){
            ex.printStackTrace();
            return null;
        }
    }
}
