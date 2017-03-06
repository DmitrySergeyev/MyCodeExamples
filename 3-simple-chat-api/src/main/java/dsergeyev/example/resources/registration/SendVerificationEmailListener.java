package dsergeyev.example.resources.registration;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import dsergeyev.example.ChatApplication;
import dsergeyev.example.controllers.rest.UserRestController;
import dsergeyev.example.models.user.User;
import dsergeyev.example.models.user.token.VerificationToken;

@Component
public class SendVerificationEmailListener implements ApplicationListener<SendVerificationEmailEvent> {

	private static final Logger logger = LoggerFactory.getLogger(ChatApplication.class);

	@Autowired
	private RegistrationService registrationService;
	@Autowired
	private VerificationEmailSender verificationEmailSender;

	@Override
	public void onApplicationEvent(SendVerificationEmailEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(SendVerificationEmailEvent event) {

		User user = event.getUser();
		VerificationToken vt = this.registrationService.getVerificationToken(user);

		// 1 - Generate token
		String token = UUID.randomUUID().toString();

		// 2 - Create new verificationToken for user if it doesn't exist and
		// refresh current one if it does
		if (vt == null) {
			this.registrationService.createVerificationToken(user, token);
		} else {
			this.registrationService.refreshVerificationToken(vt, token);
		}

		// 3 - Send email to user
		String confirmationUrl = event.getAppUrl() + UserRestController.USERS_VERIFICATION_EMAIL_CONFIRM + "?token=" + token;
		this.verificationEmailSender.sendVerificationEmail(user, confirmationUrl);

		logger.info("Verication email to user '{}'  has been send - {}", user.getEmail(), confirmationUrl);
	}
}
