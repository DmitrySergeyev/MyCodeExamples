package dsergeyev.example.resources.registration;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import dsergeyev.example.models.user.User;

@Service
public class VerificationEmailSender {

	private JavaMailSender javaMailSender;
	
	@Autowired
	public VerificationEmailSender(JavaMailSender javaMailSender){
		this.javaMailSender = javaMailSender;
	}
	
	private SimpleMailMessage createSimpleMail(User user, String text) {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setSentDate(new Date());
		mail.setTo(user.getEmail());
		mail.setText(text);	
		return mail;
	}
	
	public void sendVerificationEmail(User user, String text) throws MailException {
		SimpleMailMessage mail = this.createSimpleMail(user, text);
		mail.setSubject("Confirmation of registration!");		
		javaMailSender.send(mail);
	}
	
	public void sendResetPasswordEmail(User user, String text) throws MailException {
		SimpleMailMessage mail = this.createSimpleMail(user, text);
		mail.setSubject("Reset user's password!");		
		javaMailSender.send(mail);
	}
}
