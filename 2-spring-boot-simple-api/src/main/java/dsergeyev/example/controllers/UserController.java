package dsergeyev.example.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dsergeyev.example.models.cards.CreditCard;
import dsergeyev.example.models.cards.CreditCardType;
import dsergeyev.example.models.groups.Group;
import dsergeyev.example.models.users.User;
import dsergeyev.example.models.users.UserService;

@RestController
public class UserController {

	private static final int DEFAULT_PAGE_NUM = 0;
	private static final int DEFAULT_PAGE_SIZE = 20;
	private static final String DEFAULT_SORT = "id";

	@Autowired
	private UserService userService;

	/*
	 * CREATE
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/users")
	public ResponseEntity<?> createUser(@Valid @RequestBody User user, HttpServletRequest request) {
		return this.userService.createUser(user, request);
	}

	/*
	 * GET
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/users/{id}")
	public ResponseEntity<?> getUser(@PathVariable Long id) {
		User user = this.userService.getUserById(id);

		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/users/check-by-email")
	public ResponseEntity<?> checkEmailAddress(
			@RequestParam(value = "email", required = true) @PathVariable String email, HttpServletRequest request) {

		return this.userService.checkEmailAddress(email, request);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/users")
	public ResponseEntity<?> getAllUsers(
			@PageableDefault(page = DEFAULT_PAGE_NUM, size = DEFAULT_PAGE_SIZE, sort = DEFAULT_SORT) Pageable pageable) {

		return new ResponseEntity<>(this.userService.getAllUsers(pageable), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/users/bithday/between")
	public ResponseEntity<?> getUserBirhdayBetween(
			@PageableDefault(page = DEFAULT_PAGE_NUM, size = DEFAULT_PAGE_SIZE, sort = DEFAULT_SORT) Pageable pageable,
			@RequestParam(value = "from", required = true) @DateTimeFormat(iso=ISO.DATE) LocalDate from,
			@RequestParam(value = "to", required = true) @DateTimeFormat(iso=ISO.DATE) LocalDate to) {
		return new ResponseEntity<>(this.userService.getAllUsersDateOfBirthBetween(pageable, from, to), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/users/bithday/less-than")
	public ResponseEntity<?> getAllUsersDirthdateLess(
			@PageableDefault(page = DEFAULT_PAGE_NUM, size = DEFAULT_PAGE_SIZE, sort = DEFAULT_SORT) Pageable pageable,
			@RequestParam(value = "to", required = true) @DateTimeFormat(iso=ISO.DATE) LocalDate to) {
		return new ResponseEntity<>(this.userService.getAllUsersDateOfBirthLessThanEqual(pageable, to), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/users/bithday/greater-than")
	public ResponseEntity<?> getUserBirhdayBetween(
			@PageableDefault(page = DEFAULT_PAGE_NUM, size = DEFAULT_PAGE_SIZE, sort = DEFAULT_SORT) Pageable pageable,
			@RequestParam(value = "to", required = true) @DateTimeFormat(iso=ISO.DATE) LocalDate from) {
		return new ResponseEntity<>(this.userService.getAllUsersDateOfBirthGreaterThanEqual(pageable, from),
				HttpStatus.OK);
	}

	/*
	 * UPDATE
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/users/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user, HttpServletRequest request) {
		return this.userService.updateUser(id, user, request);
	}

	/*
	 * DELETE
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id, HttpServletRequest request) {
		return this.userService.deleteUser(id, request);
	}

	/*
	 * TEST
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/users/test")
	public ResponseEntity<?> testUser(HttpServletRequest request) {

		User user1 = new User();
		user1.setFirstName("Dmitry");
		user1.setSecondName("Sergeyev");
		user1.setDateOfBirth(LocalDate.of(1995, 5, 5));
		user1.setCreateDate(LocalDateTime.now());
		user1.setUpdateDate(LocalDateTime.now());
		user1.setEmail("dmitry@sergeyev.com");

		User user2 = new User();
		user2.setFirstName("Ivan");
		user2.setSecondName("Ivanov");
		user2.setDateOfBirth(LocalDate.of(1990, 1, 1));
		user2.setCreateDate(LocalDateTime.now());
		user2.setUpdateDate(LocalDateTime.now());
		user2.setEmail("ivan@ivanov.com");

		User user3 = new User();
		user3.setFirstName("Peter");
		user3.setSecondName("Petrov");
		user3.setDateOfBirth(LocalDate.of(1993, 3, 3));
		user3.setCreateDate(LocalDateTime.now());
		user3.setUpdateDate(LocalDateTime.now());
		user3.setEmail("peter@petrov.com");

		/*
		 * One to many { user(1) -> (n)creditCard }
		 */
		Set<CreditCard> cards1 = new HashSet<>();
		cards1.add(new CreditCard(CreditCardType.MASTER_CARD, "5555555555554444", user1));
		cards1.add(new CreditCard(CreditCardType.AMERICAN_EXPRESS, "378282246310005", user1));
		user1.setCards(cards1);

		Set<CreditCard> cards2 = new HashSet<>();
		cards1.add(new CreditCard(CreditCardType.VISA, "4012888888881881", user2));
		cards1.add(new CreditCard(CreditCardType.AMERICAN_EXPRESS, "371449635398431", user2));
		user2.setCards(cards2);

		/*
		 * Many to many { user(n) <-> (n)group }
		 */
		Group group1 = new Group("Group1", "Group1_description");
		Group group2 = new Group("Group2", "Group2_description");
		Group group3 = new Group("Group3", "Group3_description");

		Set<Group> groups1 = new HashSet<>();
		Set<Group> groups2 = new HashSet<>();

		groups1.add(group1);
		groups1.add(group2);
		groups2.add(group2);
		groups2.add(group3);

		user1.setGroups(groups1);
		user2.setGroups(groups2);

		/*
		 * Save users
		 */
		this.userService.createUser(user1, request);
//		this.userService.createUser(user2, request);
		this.userService.createUser(user3, request);

		return new ResponseEntity<>(HttpStatus.OK);
	}

}
