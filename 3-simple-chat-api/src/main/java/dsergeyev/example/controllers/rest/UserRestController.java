package dsergeyev.example.controllers.rest;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dsergeyev.example.ChatApplicationConfig;
import dsergeyev.example.models.user.EditUserDto;
import dsergeyev.example.models.user.User;
import dsergeyev.example.models.user.UserService;
import dsergeyev.example.resources.registration.RegistrationService;
import dsergeyev.example.resources.registration.password.change.PasswordChangeDto;
import dsergeyev.example.resources.registration.password.reset.PasswordResetDto;

@RestController
public class UserRestController {	

	@Autowired
	private UserService userService;
	@Autowired
	private RegistrationService registrationService;	

	public static final String USERS = ChatApplicationConfig.API_VERSION_PREFIX + "/users";
	public static final String USERS_REGISTRATION = USERS + "/registration";
	
	public static final String USERS_VERIFICATION = USERS + "/verification";
	public static final String USERS_VERIFICATION_EMAIL_RESET = USERS_VERIFICATION + "/email/resend";
	public static final String USERS_VERIFICATION_EMAIL_CONFIRM = USERS_VERIFICATION + "/email/confirm";
	
	public static final String USERS_RESET_PASSWORD = USERS + "/reset-password";
	public static final String USERS_RESET_PASSWORD_CONFIRM = USERS_RESET_PASSWORD + "/confirm";
	
	public static final String USERS_CHANGE_PASSWORD = USERS + "/change-password";
	
	public static final String USERS_ID = USERS + "/{id}";
	public static final String USERS_EMAIL = USERS + "/email";
	public static final String USERS_CHECK_BY_EMAIL = USERS + "/check-by-email";
	public static final String USERS_BITHDATE_BETWEEN = USERS + "/bithday/between";
	public static final String USERS_BITHDATE_LESS_THAN = USERS + "/bithday/less-than";
	public static final String USERS_BITHDATE_GREATER_THAN = USERS + "/users/bithday/greater-than";
	
	private static final int DEFAULT_PAGE_NUM = 0;
	private static final int DEFAULT_PAGE_SIZE = 20;
	private static final String DEFAULT_SORT = "id";
	
//	@RequestMapping(method = RequestMethod.GET, value = "/users/my-role")
//	public Collection<? extends GrantedAuthority> registrationUser(HttpServletRequest request) {
//		return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
//	}
	
	/*
	 * 1. USER'S VERIFICATION 
	 */
	// 1.1 - Register new user (with sending confirming registration email)
	@PostMapping(value = USERS_REGISTRATION)
	public ResponseEntity<?> registrationUser(@Valid @RequestBody User user, HttpServletRequest request) {
		return this.registrationService.registerUser(user, request);
	}

	// 1.2 - Resent the email with confirmation of registration
	@GetMapping(value = USERS_VERIFICATION_EMAIL_RESET)
	public ResponseEntity<?> resendConfirmationRegistrationEmail(@RequestParam("email") String email,
			HttpServletRequest request) {
		return this.registrationService.resendRegistrationEmail(email, request);
	}

	// 1.3 - Confirm user's registration
	@GetMapping(value = USERS_VERIFICATION_EMAIL_CONFIRM)
	public ResponseEntity<?> confirmRegistration(@RequestParam("token") String token, HttpServletRequest request) {
		return this.registrationService.confirmRegistration(token, request);
	}

	// 1.4 - Change user's password
	@PostMapping(value = USERS_CHANGE_PASSWORD)
	public ResponseEntity<?> changeUserPassword(@Valid @RequestBody PasswordChangeDto passwordChangeDto,
			HttpServletRequest reques) {
		return this.registrationService.changeUserPassword(passwordChangeDto, reques);
	}

	// 1.5 - Send reset user's password email
	@GetMapping(value = USERS_RESET_PASSWORD)
	public ResponseEntity<?> sendResetPasswordEmail(@RequestParam String email, HttpServletRequest request) {
		return this.registrationService.sendResetPasswordEmail(email, request);
	}

	// 1.6 - Reset user's password
	@PostMapping(value = USERS_RESET_PASSWORD_CONFIRM)
	public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetDto passwordResetDto,
			HttpServletRequest request) {
		return this.registrationService.resetPassword(passwordResetDto, request);
	}

	/*
	 * 2. GET USER(-S)
	 */
	// 2.1 Check if user exist by user's email
	@GetMapping(value = USERS_CHECK_BY_EMAIL)
	public ResponseEntity<?> checkEmailAddress(
			@RequestParam(value = "email", required = true) @PathVariable String email, HttpServletRequest request) {
		return this.userService.checkEmailAddress(email, request);
	}

	// 2.2 - Get user by id
	@GetMapping(value = USERS_ID)
	public ResponseEntity<?> getUser(@PathVariable("id") Long id) {
		User user = this.userService.getUserById(id);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	// 2.3 - Get user by email
	@GetMapping(value = USERS_EMAIL)
	public ResponseEntity<?> getUser(@PathParam("email") String email) {
		User user = this.userService.getUserByEmail(email);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	// 2.4 - Get all users
	@GetMapping(value = USERS)	
	public ResponseEntity<?> getAllUsers(@RequestParam(value = "search", defaultValue="") String search,
			@PageableDefault(page = DEFAULT_PAGE_NUM, size = DEFAULT_PAGE_SIZE, sort = DEFAULT_SORT) Pageable pageable) {
		return new ResponseEntity<>(this.userService.getAllUsers(search, pageable), HttpStatus.OK);
	}

	// 2.5 - Get all users with date of birth inside the span of date
	@GetMapping(value = USERS_BITHDATE_BETWEEN)
	public ResponseEntity<?> getUserBirhdayBetween(
			@PageableDefault(page = DEFAULT_PAGE_NUM, size = DEFAULT_PAGE_SIZE, sort = DEFAULT_SORT) Pageable pageable,
			@RequestParam(value = "from", required = true) @DateTimeFormat(iso=ISO.DATE) LocalDate from,
			@RequestParam(value = "to", required = true) @DateTimeFormat(iso=ISO.DATE) LocalDate to) {
		return new ResponseEntity<>(this.userService.getAllUsersDateOfBirthBetween(pageable, from, to), HttpStatus.OK);
	}

	// 2.6 - Get all users with date of birth early than specified date
	@GetMapping(value = USERS_BITHDATE_LESS_THAN)
	public ResponseEntity<?> getAllUsersDirthdateLess(
			@PageableDefault(page = DEFAULT_PAGE_NUM, size = DEFAULT_PAGE_SIZE, sort = DEFAULT_SORT) Pageable pageable,
			@RequestParam(value = "to", required = true) @DateTimeFormat(iso=ISO.DATE) LocalDate to) {
		return new ResponseEntity<>(this.userService.getAllUsersDateOfBirthLessThanEqual(pageable, to), HttpStatus.OK);
	}

	// 2.7 - Get all users with
	@GetMapping(value = USERS_BITHDATE_GREATER_THAN)
	public ResponseEntity<?> getUserBirhdayBetween(
			@PageableDefault(page = DEFAULT_PAGE_NUM, size = DEFAULT_PAGE_SIZE, sort = DEFAULT_SORT) Pageable pageable,
			@RequestParam(value = "to", required = true) @DateTimeFormat(iso=ISO.DATE) LocalDate from) {
		return new ResponseEntity<>(this.userService.getAllUsersDateOfBirthGreaterThanEqual(pageable, from),
				HttpStatus.OK);
	}

	/*
	 * 3. UPDATE USER
	 */
	// 3.1 - Update user
	@PutMapping(value = USERS)
	public ResponseEntity<?> updateUser(@Valid @RequestBody EditUserDto editUserDto, HttpServletRequest request) {
		return this.userService.updateUser(editUserDto, request);
	}

	/*
	 * 4. DELETE USER
	 */
	// 4.1 - Delete user by id
//	@DeleteMapping(value = USERS_ID)
//	public ResponseEntity<?> deleteUser(@PathVariable Long id, HttpServletRequest request) {
//		return this.userService.deleteUser(id, request);
//	}
}
