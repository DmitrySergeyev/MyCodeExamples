package dsergeyev.example.resources.registration;

import java.time.ZonedDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dsergeyev.example.models.user.User;
import dsergeyev.example.models.user.UserService;
import dsergeyev.example.models.user.token.PasswordResetToken;
import dsergeyev.example.models.user.token.VerificationToken;
import dsergeyev.example.resources.errorhanding.exception.PasswordsNotMatchExeption;
import dsergeyev.example.resources.httpresponse.error.StandartErrorHttpResponse;
import dsergeyev.example.resources.httpresponse.info.CreatUserInfoHttpResponse;
import dsergeyev.example.resources.httpresponse.info.StandartInfoHttpResponse;
import dsergeyev.example.resources.registration.password.change.PasswordChangeDto;
import dsergeyev.example.resources.registration.password.reset.PasswordResetDto;
import dsergeyev.example.resources.registration.password.reset.ResetUserPasswordEvent;

@Service
public class RegistrationService {

	@Autowired
	private UserService userService;
	@Autowired
	ApplicationEventPublisher eventPublisher;

	// 1 - Register user
	public ResponseEntity<?> registerUser(User user, HttpServletRequest request) {

		user.setEnabled(false);
		user.setRole(this.userService.getUserRoleByRoleName("ROLE_USER"));
		Long newUserId = this.userService.createUser(user);

		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort();
		String contextPath = request.getServletPath();

		try {
			eventPublisher.publishEvent(new SendVerificationEmailEvent(user, appUrl));
		} catch (Exception ex) {
			StandartErrorHttpResponse responseInfo = new StandartErrorHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR,
					ex.getClass().getName(), "Error! User has been created, but verification email has not been sent!", contextPath);

			return new ResponseEntity<>(responseInfo, HttpStatus.BAD_REQUEST);
		}

		CreatUserInfoHttpResponse responseInfo = new CreatUserInfoHttpResponse(HttpStatus.OK,
				"User has been created and verification email has been sent!", contextPath, newUserId,
				user.getEmail());
		return new ResponseEntity<>(responseInfo, HttpStatus.OK);
	}

	// 2 - Resent verification email
	public ResponseEntity<?> resendRegistrationEmail(String email, HttpServletRequest request) {

		User user = this.userService.getUserByEmail(email);
		this.userService.checkUserAlreadyVerified(user);

		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort();
		String contextPath = request.getServletPath();

		try {
			eventPublisher.publishEvent(new SendVerificationEmailEvent(user, appUrl));
		} catch (Exception me) {

			StandartInfoHttpResponse responseInfo = new StandartInfoHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error! Verification email has not been sent", contextPath);

			return new ResponseEntity<>(responseInfo, HttpStatus.BAD_REQUEST);
		}

		StandartInfoHttpResponse responseInfo = new StandartInfoHttpResponse(HttpStatus.OK,
				"Verification email has been sent", contextPath);

		return new ResponseEntity<>(responseInfo, HttpStatus.BAD_REQUEST);
	}

	// 3 - Confirm user registration
	public ResponseEntity<?> confirmRegistration(String token, HttpServletRequest request) {

		VerificationToken verificationToken = userService.getVerificationToken(token);
		String contextPath = request.getServletPath();

		if (verificationToken == null) {
			StandartInfoHttpResponse responseInfo = new StandartInfoHttpResponse(HttpStatus.BAD_REQUEST,
					"Verification error! Specified token doesn't exist", contextPath);
			return new ResponseEntity<>(responseInfo, HttpStatus.BAD_REQUEST);
		}
		
		if(verificationToken.isVerified() == true) {
			StandartInfoHttpResponse responseInfo = new StandartInfoHttpResponse(HttpStatus.BAD_REQUEST,
					"Verification error! The user has already been verified", contextPath);
			return new ResponseEntity<>(responseInfo, HttpStatus.BAD_REQUEST);
		}

		if (verificationToken.getExpiryDate().isBefore(ZonedDateTime.now())) {
			StandartInfoHttpResponse responseInfo = new StandartInfoHttpResponse(HttpStatus.BAD_REQUEST,
					"Verification error! The validity of the token has expired", contextPath);
			return new ResponseEntity<>(responseInfo, HttpStatus.BAD_REQUEST);
		}

		User user = verificationToken.getUser();
		this.userService.enableUser(user);
		this.userService.verifyToken(verificationToken);
		
		StandartInfoHttpResponse responseInfo = new StandartInfoHttpResponse(HttpStatus.OK,
				"The user has been enabled!", contextPath);
		return new ResponseEntity<>(responseInfo, HttpStatus.OK);
	}

	// 4 - Change user's password
	public ResponseEntity<?> changeUserPassword(PasswordChangeDto passwordChangeDto, HttpServletRequest request) {

		String contextPath = request.getServletPath();
		String userEmail = request.getUserPrincipal().getName();

		this.userService.checkCurrentPassword(userEmail, passwordChangeDto.getOldPassword());

		if (!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmationNewPassword())) {
			throw new PasswordsNotMatchExeption("New password and confirmation of new one don't match");
		}

		User user = this.userService.getUserByEmail(userEmail);
		this.userService.changeUserPassword(user, passwordChangeDto.getNewPassword());

		StandartInfoHttpResponse responseInfo = new StandartInfoHttpResponse(HttpStatus.OK,
				"Password has been changed", contextPath);
		return new ResponseEntity<>(responseInfo, HttpStatus.OK);
	}

	// 5 - Send reset user's password email
	public ResponseEntity<?> sendResetPasswordEmail(String email, HttpServletRequest request) {

		User user = this.userService.getUserByEmail(email);

		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort();
		String contextPath = request.getServletPath();

		try {
			eventPublisher.publishEvent(new ResetUserPasswordEvent(user, appUrl));
		} catch (Exception me) {

			StandartInfoHttpResponse responseInfo = new StandartInfoHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error! The email with reset password instruction has not been sent!", contextPath);
			return new ResponseEntity<>(responseInfo, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		StandartInfoHttpResponse responseInfo = new StandartInfoHttpResponse(HttpStatus.OK,
				"Email with reset password instruction has been sent!", contextPath);
		return new ResponseEntity<>(responseInfo, HttpStatus.OK);
	}

	// 6 - Reset user's password
	public ResponseEntity<?> resetPassword(PasswordResetDto passwordResetDto, HttpServletRequest request) {

		String contextPath = request.getServletPath();

		if (!passwordResetDto.getNewPassword().equals(passwordResetDto.getConfirmationNewPassword())) {
			throw new PasswordsNotMatchExeption("New password and confirmation of new one don't match");
		}

		PasswordResetToken passwordResetToken = this.userService.getPasswordResetTokem(passwordResetDto.getToken());

		if (passwordResetToken == null) {
			StandartInfoHttpResponse responseInfo = new StandartInfoHttpResponse(HttpStatus.BAD_REQUEST,
					"Error! Specified token does'n exist", contextPath);
			return new ResponseEntity<>(responseInfo, HttpStatus.BAD_REQUEST);
		}

		if (passwordResetToken.getExpiryDate().isBefore(ZonedDateTime.now())) {
			StandartInfoHttpResponse responseInfo = new StandartInfoHttpResponse(HttpStatus.BAD_REQUEST,
					"Error! The validity of the token has expired", contextPath);
			return new ResponseEntity<>(responseInfo, HttpStatus.BAD_REQUEST);
		}

		User user = passwordResetToken.getUser();
		this.userService.changeUserPassword(user, passwordResetDto.getNewPassword());

		StandartInfoHttpResponse responseInfo = new StandartInfoHttpResponse(HttpStatus.OK,
				"Password has been changed", contextPath);
		return new ResponseEntity<>(responseInfo, HttpStatus.OK);
	}

}