package hello.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component("mailsender")
public class MailSender {
    @Autowired
    JavaMailSender javaMailSender;

    public void sendMail(String from, String to, String subject, String body)  {
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom(from);
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(body);


        javaMailSender.send(mail);
        System.out.println("отправил");
    }
}
