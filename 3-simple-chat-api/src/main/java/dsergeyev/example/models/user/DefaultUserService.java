package dsergeyev.example.models.user;

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
import dsergeyev.example.resources.errorhanding.exception.EmailAddressInUseException;
import dsergeyev.example.resources.errorhanding.exception.InvalidPasswordError;
import dsergeyev.example.resources.errorhanding.exception.ResourceNotFoundException;
import dsergeyev.example.resources.httpresponse.StandardHttpResponse;

@Service
public class DefaultUserService implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	ApplicationEventPublisher eventPublisher;

	private void checkUserExistsById(Long userId) throws ResourceNotFoundException {
		if (!this.userRepository.exists(userId)) {
			throw new ResourceNotFoundException("User with id = " + userId + " not found");
		}
	}

	private void checkUserExistsByEmail(String email) throws ResourceNotFoundException {
		if (!this.userRepository.exitsByEmail(email)) {
			throw new ResourceNotFoundException("User with email = '" + email + "' not found");
		}
	}

	private String getUserPasswordByEmail(String email) {
		String password = this.userRepository.findPasswordByEmail(email);

		if (password == null) {
			throw new ResourceNotFoundException("User with email = '" + email + "' not found");
		}
		return password;
	}

	@Override
	public void checkPassword(String userEmail, String password)
			throws ResourceNotFoundException, InvalidPasswordError {
		String userPassword = this.getUserPasswordByEmail(userEmail);
		if (userPassword == null) {
			throw new ResourceNotFoundException("User with email '" + userEmail + "' doesn't exist");
		}
		if (!this.passwordEncoder.matches(password, userPassword)) {
			throw new InvalidPasswordError("Invalid password. Access denied.");
		}
	}

	@Override
	public User saveUser(User user) {
		if (!this.checkIsEmailAvailable(user.getEmail()))
			throw new EmailAddressInUseException("Email address '" + user.getEmail() + "' is already in use");
		user.setCreateDate(ZonedDateTime.now());
		user.setUpdateDate(ZonedDateTime.now());
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		User newUser = this.userRepository.save(user);
		return newUser;
	}

	@Override
	public Role getUserRoleByRoleName(String roleName) {
		return this.roleRepository.findByName(roleName);
	}

	@Override
	public User getAuthorisatedUser() {
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		return getUserByEmail(userEmail);
	}

	@Override
	public String getAuthorisatedUserEmail() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	@Override
	public void changeUserPassword(User user, String newPassword) {
		user.setPassword(this.passwordEncoder.encode(newPassword));
		user.setUpdateDate(ZonedDateTime.now());
		this.userRepository.save(user);
	}

	@Override
	public boolean checkIsEmailAvailable(String email) {
		return this.userRepository.isEmailAvalible(email);
	}

	@Override
	public User getUserById(Long userId) throws ResourceNotFoundException {
		this.checkUserExistsById(userId);
		return this.userRepository.findOne(userId);
	}

	@Override
	public User getUserByEmail(String email) throws ResourceNotFoundException {
		this.checkUserExistsByEmail(email);
		return this.userRepository.findByEmail(email);
	}

	@Override
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

	// public Page<User> getAllUsersDateOfBirthBetween(Pageable pageable,
	// LocalDate from, LocalDate to) {
	// return this.userRepository.findByDateOfBirthBetween(pageable, from, to);
	// }
	//
	// public Page<User> getAllUsersDateOfBirthLessThanEqual(Pageable pageable,
	// LocalDate to) {
	// return this.userRepository.findByDateOfBirthLessThanEqual(pageable, to);
	// }
	//
	// public Page<User> getAllUsersDateOfBirthGreaterThanEqual(Pageable
	// pageable, LocalDate from) {
	// return this.userRepository.findByDateOfBirthGreaterThanEqual(pageable,
	// from);
	// }

	@Override
	public User updateUser(EditUserDto editUserDto) throws ResourceNotFoundException {
		User user = this.getAuthorisatedUser();
		this.checkPassword(user.getEmail(), editUserDto.getPassword());

		user.setFirstName(editUserDto.getFirstName());
		user.setSecondName(editUserDto.getSecondName());
		user.setDateOfBirth(editUserDto.getDateOfBirth());
		user.setWebSite(editUserDto.getWebSite());

		return this.userRepository.save(user);
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> deleteUser(Long userId, HttpServletRequest request) {
		this.checkUserExistsById(userId);
		this.userRepository.delete(userId);
		StandardHttpResponse infoResponse = new StandardHttpResponse(
				"User with id = " + userId + " has been successfully  removed!", request.getRequestURI());
		return new ResponseEntity<>(infoResponse, null, HttpStatus.OK);
	}

	public void deleteUser(Long userId) {
		this.checkUserExistsById(userId);
		this.userRepository.delete(userId);
	}
}
