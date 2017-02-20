package dsergeyev.example.models.users;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dsergeyev.example.resources.errorhanding.exceptions.EmailAddressInUseException;
import dsergeyev.example.resources.errorhanding.exceptions.ResourceNotFoundException;
import dsergeyev.example.resources.responses.info.IsEmailExistsInfoDetails;
import dsergeyev.example.resources.responses.info.StandartInfoDetails;
import dsergeyev.example.resources.responses.info.UserCreatedInfoMessage;

@Service
public class UserService {

	@Autowired
	private UserPagingAndSortingRepository userRepository;

	public void checkUserExists(Long userId) throws ResourceNotFoundException {

		if (!this.userRepository.exists(userId)) {
			throw new ResourceNotFoundException("User with id = " + userId + " not found");
		}
	}

	public void checkEmailIsAvalible(String email) throws EmailAddressInUseException {

		if (this.userRepository.existsByEmail(email)) {
			throw new EmailAddressInUseException("Email address '" + email + "' is already in use");
		}
	}

	public ResponseEntity<?> checkEmailAddress(String email, HttpServletRequest request) {

		IsEmailExistsInfoDetails infoResponse = new IsEmailExistsInfoDetails();
		infoResponse.setTimeStamp(System.currentTimeMillis());
		infoResponse.setStatus(HttpStatus.OK.value());
		infoResponse.setTitle("Is email available");
		infoResponse.setPath(request.getRequestURI());
		infoResponse.setRequredEmail(email);

		if (this.userRepository.existsByEmail(email)) {
			infoResponse.setDetail("Email address '" + email + "' is already in use");
			infoResponse.setIsEmailAvailable(false);
		} else {
			infoResponse.setDetail("Email address " + email + " is available");
			infoResponse.setIsEmailAvailable(true);
		}

		return new ResponseEntity<>(infoResponse, null, HttpStatus.OK);
	}

	/*
	 * CREATE
	 */
	public ResponseEntity<?> createUser(User user, HttpServletRequest request) {
		this.checkEmailIsAvalible(user.getEmail());

		user.setCreateDate(LocalDateTime.now());
		user.setUpdateDate(LocalDateTime.now());
		this.userRepository.save(user);

		UserCreatedInfoMessage infoResponse = new UserCreatedInfoMessage();
		infoResponse.setTimeStamp(System.currentTimeMillis());
		infoResponse.setStatus(HttpStatus.OK.value());
		infoResponse.setTitle("Create user");
		infoResponse.setPath(request.getRequestURI());
		infoResponse.setDetail(user.toString() + " has been successfully created!");
		infoResponse.setNewUserId(user.getId());

		return new ResponseEntity<>(infoResponse, null, HttpStatus.OK);
	}

	/*
	 * GET
	 */
	public User getUserById(Long userId) {
		this.checkUserExists(userId);
		return this.userRepository.findOne(userId);
	}

	public User getUserByEmail(String email) {
		return this.userRepository.findOneByEmail(email);
	}

	public Page<User> getAllUsers(Pageable pageable) {
		return this.userRepository.findAll(pageable);
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
	public ResponseEntity<?> updateUser(Long userId, User user, HttpServletRequest request) {
		this.checkUserExists(userId);
		String oldEmail = this.userRepository.findEmailById(userId);

		if (!oldEmail.equals(user.getEmail())) {
			this.checkEmailIsAvalible(user.getEmail());
		}

		user.setUpdateDate(LocalDateTime.now());
		this.userRepository.save(user);

		UserCreatedInfoMessage infoResponse = new UserCreatedInfoMessage();
		infoResponse.setTimeStamp(System.currentTimeMillis());
		infoResponse.setStatus(HttpStatus.OK.value());
		infoResponse.setTitle("Update user");
		infoResponse.setPath(request.getRequestURI());
		infoResponse.setDetail(user.toString() + " has been successfully updated!");
		infoResponse.setNewUserId(user.getId());

		return new ResponseEntity<>(infoResponse, null, HttpStatus.OK);
	}

	/*
	 * DELETE
	 */
	public ResponseEntity<?> deleteUser(Long userId, HttpServletRequest request) {
		this.checkUserExists(userId);

		this.userRepository.delete(userId);

		StandartInfoDetails infoResponse = new StandartInfoDetails();
		infoResponse.setTimeStamp(System.currentTimeMillis());
		infoResponse.setStatus(HttpStatus.OK.value());
		infoResponse.setTitle("Delete user");
		infoResponse.setPath(request.getRequestURI());
		infoResponse.setDetail("User with id = " + userId + " has been successfully  removed!");

		return new ResponseEntity<>(infoResponse, null, HttpStatus.OK);
	}
}
