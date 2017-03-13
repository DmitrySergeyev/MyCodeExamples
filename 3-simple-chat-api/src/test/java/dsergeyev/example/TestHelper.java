package dsergeyev.example;

import java.time.LocalDate;

import dsergeyev.example.models.roles.Role;
import dsergeyev.example.models.user.User;

public class TestHelper {	
	public User getValidUser() {
		User validUser = new User("user@gmail.com", "qweQWE123!@#", true, new Role("ROLE_USER"), "Peter", "Pertov",
				LocalDate.now().minusYears(20), "https://github.com");
		validUser.setConfirmationPassword(validUser.getPassword());		
		return validUser;
	}
}
