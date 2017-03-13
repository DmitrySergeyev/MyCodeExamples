package dsergeyev.example.resources.registration;

import org.springframework.mail.MailException;

import dsergeyev.example.models.user.User;
import dsergeyev.example.models.user.token.VerificationToken;
import dsergeyev.example.resources.registration.password.change.PasswordChangeDto;
import dsergeyev.example.resources.registration.password.reset.PasswordResetDto;

public interface RegistrationService {
	
	User registerUser(User user, String appUrl) throws MailException;
	void resendRegistrationEmail(String email, String appUrl) throws MailException;
	User confirmRegistration(String token);
	void changeUserPassword(PasswordChangeDto pcDto);
	void sendResetPasswordEmail(String email, String appUrl) throws MailException;
	void resetPassword(PasswordResetDto prDto);
	
	VerificationToken getVerificationToken(User user);
	void createVerificationToken(User user, String token);
	void refreshVerificationToken(VerificationToken vt, String token);
	
	void createResetPasswordToken(User user, String token);
}
