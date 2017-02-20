package dsergeyev.example.controllers;

import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dsergeyev.example.models.user.User;
import dsergeyev.example.models.user.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET, value = "/users")
	public List<User> getAllUsers() {
		return this.userService.getAllUsers();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/users/{id}")
	public User getUser(@PathVariable Long id) {
		return this.userService.getUser(id);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/users")
	public ResponseEntity<?> createUser(@RequestBody User user) {

		user.setBirthDate1(LocalDate.now());
		user.setBirthDate2(LocalDate.now());
		user.setBirthDate3(LocalDate.now());
		
		user.setCreateDate(LocalDateTime.now());
		user.setUpdateDate(LocalDateTime.now());
		user.setColor(new Color(25, 26, 27));

		this.userService.createUser(user);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/users")
	public void updateUser(@RequestBody User user) {
		this.userService.updateUser(user);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/users/{id}")
	public void deleteUser(@PathVariable Long id) {
		this.userService.deleteUser(id);
	}
}
