package dsergeyev.example.models.user;

import java.time.LocalDate;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

import dsergeyev.example.models.roles.Role;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTests {

	private static Validator validator;
	private User validUser;

	@BeforeClass
	public static void setUpValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Before
	public void setup() throws Exception {
		this.validUser = new User("user@gmail.com", "qweQWE123!@#", true, new Role("ROLE_USER"), "Peter", "Pertov",
				LocalDate.now().minusYears(20), "https://github.com");
		this.validUser.setConfirmationPassword(this.validUser.getPassword());
	}

	// 1 - Valid user
	@Test
	public void validUser() throws Exception {
		User testUser = this.validUser.clone();
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(0, constraintViolations.size());
	}
	
	@Test
	public void onlyRequiredFields() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setDateOfBirth(null);
		testUser.setWebSite(null);
		testUser.setPhoto(null);
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(0, constraintViolations.size());
	}
	
	// 2 - Email
	@Test
	public void emailIsNull() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setEmail(null);
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("may not be null", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void emailIsEmpty() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setEmail("");
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("not a well-formed email address", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void emailInvalid() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setEmail("email");
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("not a well-formed email address", constraintViolations.iterator().next().getMessage());
	}	
	
	@Test
	public void emailTooLong() throws Exception {
		User testUser = this.validUser.clone();
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < 245; i++)
			sb.append("e");
		testUser.setEmail(sb.append("@gmail.com").toString());
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("not a well-formed email address", constraintViolations.iterator().next().getMessage());
	}
	
	// 3 - Password and confirmation of password
	@Test
	public void passwordIsNull() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setPassword(null);
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("User registration request should contain valid 'password' and 'confirmationPassword' fields", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void confirmationPasswordIsNull() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setConfirmationPassword(null);
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("User registration request should contain valid 'password' and 'confirmationPassword' fields", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void passwordsNotMathc() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setConfirmationPassword(testUser.getPassword() + "1");
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("Password and confirmation of password do not match", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void passwordTooShort() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setPassword("qwQW12!");		
		testUser.setConfirmationPassword(testUser.getPassword());
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("Password must be at least 8 characters in length.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void passwordToLong() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setPassword("qwertyuiopQWERTYUIOP1234567890!");		
		testUser.setConfirmationPassword(testUser.getPassword());
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("Password must be no more than 30 characters in length.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void passwordContainUppercaseCharacter() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setPassword("qwerty1!");		
		testUser.setConfirmationPassword(testUser.getPassword());
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("Password must contain at least 1 uppercase characters.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void passwordContainDigit() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setPassword("qweRTY!@");		
		testUser.setConfirmationPassword(testUser.getPassword());
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("Password must contain at least 1 digit characters.", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void passwordContainSpecialCharacter() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setPassword("qweRTY12");		
		testUser.setConfirmationPassword(testUser.getPassword());
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("Password must contain at least 1 special characters.", constraintViolations.iterator().next().getMessage());
	}
	
	// 4 - First name
	@Test
	public void firstNameNotNull() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setFirstName(null);
		testUser.setConfirmationPassword(testUser.getPassword());
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("may not be null", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void firstNameTooShort() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setFirstName("a");
		testUser.setConfirmationPassword(testUser.getPassword());
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("length must be between 2 and 50", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void firstNameTooLong() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setFirstName("qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopq");
		testUser.setConfirmationPassword(testUser.getPassword());
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("length must be between 2 and 50", constraintViolations.iterator().next().getMessage());
	}
	
	// 5 - Second name
	@Test
	public void secondNameNotNull() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setSecondName(null);
		testUser.setConfirmationPassword(testUser.getPassword());
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("may not be null", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void secondNameTooShort() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setSecondName("a");
		testUser.setConfirmationPassword(testUser.getPassword());
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("length must be between 2 and 50", constraintViolations.iterator().next().getMessage());
	}
	
	@Test
	public void secondNameTooLong() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setSecondName("qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopq");
		testUser.setConfirmationPassword(testUser.getPassword());
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("length must be between 2 and 50", constraintViolations.iterator().next().getMessage());
	}
	
	// 6 - Date of birth
	@Test
	public void dateOfBirthInPast() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setDateOfBirth(LocalDate.now().plusDays(1));
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("must be in the past", constraintViolations.iterator().next().getMessage());
	}
	
	// 7 - Web site
	@Test
	public void webSiteIsValid() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setWebSite("website");
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("must be a valid URL", constraintViolations.iterator().next().getMessage());
	}
	
	// 8 - Photo
	@Test
	public void photoIsValid() throws Exception {
		User testUser = this.validUser.clone();
		testUser.setWebSite("photo");
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(testUser);
		assertEquals(1, constraintViolations.size());
		assertEquals("must be a valid URL", constraintViolations.iterator().next().getMessage());
	}
}
