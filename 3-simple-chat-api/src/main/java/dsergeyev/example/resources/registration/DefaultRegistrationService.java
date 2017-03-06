package dsergeyev.example.resources.registration;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import dsergeyev.example.models.user.User;
import dsergeyev.example.models.user.UserService;
import dsergeyev.example.models.user.token.PasswordResetToken;
import dsergeyev.example.models.user.token.PasswordResetTokenRepository;
import dsergeyev.example.models.user.token.VerificationToken;
import dsergeyev.example.models.user.token.VerificationTokenRepository;
import dsergeyev.example.resources.errorhanding.exception.InvalidTokenException;
import dsergeyev.example.resources.errorhanding.exception.PasswordsNotMatchExeption;
import dsergeyev.example.resources.errorhanding.exception.ResourceNotFoundException;
import dsergeyev.example.resources.errorhanding.exception.UserVerificationException;
import dsergeyev.example.resources.registration.password.change.PasswordChangeDto;
import dsergeyev.example.resources.registration.password.reset.PasswordResetDto;
import dsergeyev.example.resources.registration.password.reset.ResetUserPasswordEvent;

@Service
public class DefaultRegistrationService implements RegistrationService {

	@Autowired
	private UserService userService;
	@Autowired
	ApplicationEventPublisher eventPublisher;
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	private void checkUserAlreadyVerified(User user) throws ResourceNotFoundException, UserVerificationException {
		if (user.isEnabled()) {
			throw new UserVerificationException("User has already been verified");
		}
	}
	
	private void verifyToken(VerificationToken token) {
		token.setVerified(true);
		this.verificationTokenRepository.save(token);
	}
	
	private PasswordResetToken getPasswordResetTokem(String passwordResetToken) {
		return this.passwordResetTokenRepository.findByToken(passwordResetToken);
	}

	private VerificationToken getVerificationToken(String VerificationToken) {
		return this.verificationTokenRepository.findByToken(VerificationToken);
	}

	@Override
	public Long registerUser(User user, String serverName, int serverPort) throws MailException {
		user.setEnabled(false);
		user.setRole(this.userService.getUserRoleByRoleName("ROLE_USER"));
		Long newUserId = this.userService.saveUser(user);
		String appUrl = "http://" + serverName + serverPort;
		eventPublisher.publishEvent(new SendVerificationEmailEvent(user, appUrl));
		return newUserId;
	}

	@Override
	public void resendRegistrationEmail(String email, String serverName, int serverPort) {
		User user = this.userService.getUserByEmail(email);
		this.checkUserAlreadyVerified(user);
		String appUrl = "http://" + serverName + serverPort;
		eventPublisher.publishEvent(new SendVerificationEmailEvent(user, appUrl));
	}
	
	@Override
	public void confirmRegistration(String token) {
		VerificationToken verificationToken = this.getVerificationToken(token);
		
		if (verificationToken == null) {
			throw new InvalidTokenException("Verification error! Specified token doesn't exist");
		}
		
		if (verificationToken.isVerified() == true) {
			throw new InvalidTokenException("Verification error! Token has already been used");
		}
		
		if (verificationToken.getExpiryDate().isBefore(ZonedDateTime.now())) {
			throw new InvalidTokenException("Verification error! The validity of the token has expired");
		}
		
		User user = verificationToken.getUser();
		user.setEnabled(true);
		this.userService.saveUser(user);
		this.verifyToken(verificationToken);
	}
	
	@Override
	public void changeUserPassword(PasswordChangeDto pcDto) {
		User user = this.userService.getAuthorisatedUser();
		this.userService.checkPassword(user.getEmail(), pcDto.getOldPassword());
		
		if (!pcDto.getNewPassword().equals(pcDto.getConfirmationNewPassword())) {
			throw new PasswordsNotMatchExeption("New password and confirmation of new one don't match");
		}

		this.userService.changeUserPassword(user, pcDto.getNewPassword());
	}
	
	@Override
	public void sendResetPasswordEmail(String email, String serverName, int serverPort) throws MailException {
		User user = this.userService.getUserByEmail(email);
		String appUrl = "http://" + serverName + serverPort;
		eventPublisher.publishEvent(new ResetUserPasswordEvent(user, appUrl));
	}
	
	@Override
	public void resetPassword(PasswordResetDto prDto) {
		if (!prDto.getNewPassword().equals(prDto.getConfirmationNewPassword())) {
			throw new PasswordsNotMatchExeption("New password and confirmation of new one don't match");
		}
		
		PasswordResetToken passwordResetToken = this.getPasswordResetTokem(prDto.getToken());
		
		if (passwordResetToken == null) {
			throw new InvalidTokenException("Reset password error! Specified token does'n exist");
		}

		if (passwordResetToken.getExpiryDate().isBefore(ZonedDateTime.now())) {
			throw new InvalidTokenException("Reset password error! The validity of the token has expired");
		}
		
		User user = passwordResetToken.getUser();
		this.userService.changeUserPassword(user, prDto.getNewPassword());
	}

	@Override
	public VerificationToken getVerificationToken(User user) {
		return this.verificationTokenRepository.findByUser(user);
	}

	@Override
	public void createVerificationToken(User user, String token) {
		this.verificationTokenRepository.save(new VerificationToken(token, user));
	}
	
	@Override
	public void refreshVerificationToken(VerificationToken vt, String token) {
		this.verificationTokenRepository.save(vt.refresh(token));		
	}

	@Override
	public void createResetPasswordToken(User user, String token) {
		this.passwordResetTokenRepository.save(new PasswordResetToken(token, user));		
	}
}