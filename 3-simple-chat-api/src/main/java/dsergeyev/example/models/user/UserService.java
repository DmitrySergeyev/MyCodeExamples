package dsergeyev.example.models.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dsergeyev.example.models.roles.Role;
import dsergeyev.example.models.roles.RoleRepository;
import dsergeyev.example.models.user.token.PasswordResetToken;
import dsergeyev.example.models.user.token.PasswordResetTokenRepository;
import dsergeyev.example.models.user.token.VerificationToken;
import dsergeyev.example.models.user.token.VerificationTokenRepository;
import dsergeyev.example.resources.errorhanding.exception.InvalidPasswordError;
import dsergeyev.example.resources.errorhanding.exception.ResourceNotFoundException;
import dsergeyev.example.resources.errorhanding.exception.UserVerificationException;
import dsergeyev.example.resources.httpresponse.info.CreatUserInfoHttpResponse;
import dsergeyev.example.resources.httpresponse.info.EmailExistsInfoHttpResponse;
import dsergeyev.example.resources.httpresponse.info.StandartInfoHttpResponse;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	ApplicationEventPublisher eventPublisher;

	public Long createUser(User user) {
		user.setCreateDate(ZonedDateTime.now());
		user.setUpdateDate(ZonedDateTime.now());
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		User newUser = this.userRepository.save(user);

		return newUser.getId();
	}
	
	public Role getUserRoleByRoleName(String roleName) {
		return this.roleRepository.findByName(roleName);
	}

	public boolean checkIsEmailAvalible(String email) {
		return this.userRepository.isEmailAvalible(email);
	}

	public void checkUserExistsById(Long userId) throws ResourceNotFoundException {

		if (!this.userRepository.exists(userId)) {
			throw new ResourceNotFoundException("User with id = " + userId + " not found");
		}
	}

	public void checkUserExistsByEmail(String email) throws ResourceNotFoundException {

		if (!this.userRepository.exitsByEmail(email)) {
			throw new ResourceNotFoundException("User with email = '" + email + "' not found");
		}
	}

	public void checkUserAlreadyVerified(User user) throws ResourceNotFoundException, UserVerificationException {

		if (user.isEnabled()) {
			throw new UserVerificationException("User has already been verified");
		}
	}

	public void checkCurrentPassword(String userEmail, String password)
			throws ResourceNotFoundException, InvalidPasswordError {

		String userPassword = this.getUserPasswordByEmail(userEmail);

		if (userPassword == null) {
			throw new ResourceNotFoundException("User with email '" + userEmail + "' doesn't exist");
		}

		if (!this.passwordEncoder.matches(password, userPassword)) {
			throw new InvalidPasswordError("Invalid password. Access denied.");
		}
	}

	public String getUserPasswordByEmail(String email) {

		String password = this.userRepository.findPasswordByEmail(email);

		if (password == null) {
			throw new ResourceNotFoundException("User with email = '" + email + "' not found");
		}

		return password;
	}
	
	public User getAuthorisatedUser() {
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		return getUserByEmail(userEmail);
	}
	
	public String getAuthorisatedUserEmail() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	public void changeUserPassword(User user, String newPassword) throws ResourceNotFoundException {

		user.setPassword(this.passwordEncoder.encode(newPassword));
		user.setUpdateDate(ZonedDateTime.now());
		this.userRepository.save(user);
	}

	public ResponseEntity<?> checkEmailAddress(String email, HttpServletRequest request) {

		EmailExistsInfoHttpResponse infoResponse = new EmailExistsInfoHttpResponse(HttpStatus.OK,
				request.getRequestURI(), email);

		if (this.userRepository.isEmailAvalible(email)) {
			infoResponse.setMessage("Email address '" + email + "' is available");
			infoResponse.setEmailAwailable(true);
		} else {
			infoResponse.setMessage("Email address '" + email + "' is already in use");
			infoResponse.setEmailAwailable(false);
		}

		return new ResponseEntity<>(infoResponse, null, HttpStatus.OK);
	}

	public void enableUser(User user) {
		user.setEnabled(true);
		this.userRepository.save(user);
	}

	public void createVerificationToken(User user, String token) {
		this.verificationTokenRepository.save(new VerificationToken(token, user));
	}

	public void refreshVerificationToken(VerificationToken vt, String token) {
		this.verificationTokenRepository.save(vt.refresh(token));
	}

	public void createResetPasswordToken(User user, String token) {
		this.passwordResetTokenRepository.save(new PasswordResetToken(token, user));
	}

	public void verifyToken(VerificationToken token) {
		token.setVerified(true);
		this.verificationTokenRepository.save(token);
	}

	public VerificationToken getVerificationToken(String VerificationToken) {
		return this.verificationTokenRepository.findByToken(VerificationToken);
	}

	public VerificationToken getVerificationToken(User user) {
		return this.verificationTokenRepository.findByUser(user);
	}

	public PasswordResetToken getPasswordResetTokem(String passwordResetToken) {
		return this.passwordResetTokenRepository.findByToken(passwordResetToken);
	}

	/*
	 * GET
	 */
	public User getUserById(Long userId) throws ResourceNotFoundException {
		this.checkUserExistsById(userId);
		return this.userRepository.findOne(userId);
	}

	public User getUserByEmail(String email) throws ResourceNotFoundException {
		this.checkUserExistsByEmail(email);
		return this.userRepository.findByEmail(email);
	}

	public Page<User> getAllUsers(String search, Pageable pageable) {
		Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(.+?),");
		Matcher matcher = pattern.matcher(search + ",");				
		UserSpecificationsBuilder builder = new UserSpecificationsBuilder();
		
		while (matcher.find()) {
			builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
		}

		Specification<User> spec = builder.build();
		return this.userRepository.findAll(spec, pageable);
	}

	public Page<User> getAllUsersDateOfBirthBetween(Pageable pageable, LocalDate from, LocalDate to) {
		return this.userRepository.findByDateOfBirthBetween(pageable, from, to);
	}

	public Page<User> getAllUsersDateOfBirthLessThanEqual(Pageable pageable, LocalDate to) {
		return this.userRepository.findByDateOfBirthLessThanEqual(pageable, to);
	}

	public Page<User> getAllUsersDateOfBirthGreaterThanEqual(Pageable pageable, LocalDate from) {
		return this.userRepository.findByDateOfBirthGreaterThanEqual(pageable, from);
	}

	/*
	 * UPDATE
	 */
	public ResponseEntity<?> updateUser(EditUserDto editUserDto, HttpServletRequest request)
			throws ResourceNotFoundException {
		
		User user = this.getAuthorisatedUser();
		this.checkCurrentPassword(user.getEmail(), editUserDto.getPassword());

		user.setFirstName(editUserDto.getFirstName());	
		user.setSecondName(editUserDto.getSecondName());
		user.setDateOfBirth(editUserDto.getDateOfBirth());
		user.setWebSite(editUserDto.getWebSite());
		
		this.userRepository.save(user);		
		CreatUserInfoHttpResponse infoResponse = new CreatUserInfoHttpResponse(HttpStatus.OK,
				"User has been successfully updated!", request.getRequestURI(), user.getId(), user.getEmail());
		return new ResponseEntity<>(infoResponse, null, HttpStatus.OK);
	}

	/*
	 * DELETE
	 */
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> deleteUser(Long userId, HttpServletRequest request) {
		this.checkUserExistsById(userId);
		this.userRepository.delete(userId);
		StandartInfoHttpResponse infoResponse = new StandartInfoHttpResponse(HttpStatus.OK,
				"User with id = " + userId + " has been successfully  removed!", request.getRequestURI());
		return new ResponseEntity<>(infoResponse, null, HttpStatus.OK);
	}
}
