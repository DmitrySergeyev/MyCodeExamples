package dsergeyev.example.resources.registration.password.reset;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import dsergeyev.example.ChatApplication;
import dsergeyev.example.models.user.User;
import dsergeyev.example.models.user.UserService;
import dsergeyev.example.resources.registration.VerificationEmailSender;

@Component
public class ResetUserPasswordListener implements ApplicationListener<ResetUserPasswordEvent> {
    
	private static final Logger logger = LoggerFactory.getLogger(ChatApplication.class);
	
	@Autowired
    private UserService service;	   
	@Autowired
	private VerificationEmailSender verificationEmailSender;
 
    @Override
    public void onApplicationEvent(ResetUserPasswordEvent event) {
        this.confirmRegistration(event);
    }
 
    private void confirmRegistration(ResetUserPasswordEvent event) {
    	
    	User user = event.getUser();
    	
        String resetPasswordToken = UUID.randomUUID().toString();
        this.service.createResetPasswordToken(user, resetPasswordToken);
         
        this.verificationEmailSender.sendResetPasswordEmail(user, resetPasswordToken);
        
        logger.info("Reset password token for user '{}' has been send - {}", user.getEmail(), resetPasswordToken);   
    }
}