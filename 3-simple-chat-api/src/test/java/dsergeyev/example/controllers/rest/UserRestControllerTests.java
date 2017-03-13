package dsergeyev.example.controllers.rest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;

import dsergeyev.example.ChatApplication;
import dsergeyev.example.ChatApplicationConfig;
import dsergeyev.example.models.roles.RoleRepository;
import dsergeyev.example.models.user.User;
import dsergeyev.example.models.user.UserRepository;
import dsergeyev.example.models.user.UserService;
import dsergeyev.example.models.user.token.VerificationToken;
import dsergeyev.example.models.user.token.VerificationTokenRepository;
import dsergeyev.example.security.MyUserDetailsService;
import dsergeyev.example.security.SecurityConfig;
import net.minidev.json.JSONValue;

@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
public class UserRestControllerTests {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	Map<String, String> validUserDtoMap;
	User validUser;

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private UserService userServcie;
	@Autowired
	private UserRepository userRepository;	
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	private String mapToJsonString(Map<String, String> userDtoMap) throws IOException {
		StringWriter jsonOut = new StringWriter();
		JSONValue.writeJSONString(userDtoMap, jsonOut);
		return jsonOut.toString();
	}

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext)
			.apply(springSecurity())
            .build();

		this.validUserDtoMap = new HashMap<>();
		this.validUserDtoMap.put("email", "testuser1@gmail.com");
		this.validUserDtoMap.put("password", "qweQWE123!@#");
		this.validUserDtoMap.put("confirmationPassword", "qweQWE123!@#");
		this.validUserDtoMap.put("firstName", "FirstName");
		this.validUserDtoMap.put("secondName", "SecondName");
		this.validUserDtoMap.put("dateOfBirth", "1990-01-01");
		this.validUserDtoMap.put("webSite", "https://github.com");
		this.validUserDtoMap.put("photo",
				"http://localhost:8080/api/v1/files/images?id=b2d99010-3abc-4549-b59d-83897b0e1c74.png");
		
		this.validUser = new User("testuser2@gmail.com", "qQ1!1234", false, roleRepository.findByName("ROLE_USER"),
				"Peter", "Petrov", LocalDate.now().minusYears(20), "https://github.com");
	}

	@Test
	public void t1RegisterUserWithWrongBirthDateFormat() throws Exception {
		Map<String, String> testUserDtoMap = new HashMap<>(this.validUserDtoMap);
		testUserDtoMap.replace("dateOfBirth", "199-01-01");
		
		this.mockMvc.perform(post("/api/v1/users/registration").contentType(this.contentType)
				.content(this.mapToJsonString(testUserDtoMap)))
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("timestamp").exists()).andExpect(jsonPath("path").exists())
			.andExpect(jsonPath("message").exists()).andExpect(jsonPath("exception").exists())
			.andExpect(jsonPath("path").value("/api/v1/users/registration"))
			.andExpect(jsonPath("message").value("Could not read document: Can not deserialize value of type "
					+ "java.time.LocalDate from String \"199-01-01\": Text '199-01-01' could not be parsed at index 0"));
	}

	@Test
	public void t2RegisterValidUser() throws Exception {
		this.mockMvc.perform(post("/api/v1/users/registration").contentType(this.contentType)
				.content(this.mapToJsonString(this.validUserDtoMap)))
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("timestamp").exists())
			.andExpect(jsonPath("path").exists())
			.andExpect(jsonPath("message").exists())
			.andExpect(jsonPath("userId").exists())
			.andExpect(jsonPath("userEmail").exists())
			.andExpect(jsonPath("path").value("/api/v1/users/registration"))
			.andExpect(jsonPath("message").value("User has been created and verification email has been sent!"))
			.andExpect(jsonPath("userEmail").value(this.validUserDtoMap.get("email")))
			.andExpect(jsonPath("userId").value(this.userRepository.findIdByEmail(this.validUserDtoMap.get("email"))));
	}

	@Test
	// TODO: Launch security context for test
	public void t3CreatedUserNotEnabled() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/v1/users/1").contentType(this.contentType)
			//	.with(user(this.validUserDtoMap.get("email")).password(this.validUserDtoMap.get("password")).roles("USER")))
			.with(user("user1").password("password").roles("USER"))				)
			//.with(httpBasic("user","password")))
//			.andDo(new ResultHandler() {
//				
//				@Override
//				public void handle(MvcResult result) throws Exception {
//				}
//			})
			.andExpect(status().isUnauthorized())
			.andReturn();
	}
	
	@Test
	public void t4ConfirmRegistrationInvalidToken() throws Exception {
		
		this.mockMvc.perform(get("/api/v1/users/verification/email/confirm" + "?token=" + UUID.randomUUID().toString()).contentType(this.contentType))
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("timestamp").exists())
			.andExpect(jsonPath("path").exists())
			.andExpect(jsonPath("message").exists())
			.andExpect(jsonPath("message").value("Verification error! Specified token doesn't exist"));
			
	}
	
	@Test
	public void t5ConfirmRegistrationExpiredToken() throws Exception {
		VerificationToken vt = verificationTokenRepository.findByUser(this.userRepository.findByEmail(this.validUserDtoMap.get("email")));
		vt.setExpiryDate(vt.getExpiryDate().minusDays(1));
		this.verificationTokenRepository.save(vt);
		
		this.mockMvc.perform(get("/api/v1/users/verification/email/confirm").contentType(this.contentType).param("token", vt.getToken()))
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("timestamp").exists())
			.andExpect(jsonPath("path").exists())
			.andExpect(jsonPath("message").exists())
			.andExpect(jsonPath("message").value("Verification error! The validity of the token has expired"));			
	}
	
	@Test
	public void t6ConfirmRegistrationSuccessfull() throws Exception {
		VerificationToken vt = verificationTokenRepository.findByUser(this.userRepository.findByEmail(this.validUserDtoMap.get("email")));
		vt.setExpiryDate(ZonedDateTime.now());
		this.verificationTokenRepository.save(vt);
		
		this.mockMvc.perform(get("/api/v1/users/verification/email/confirm").contentType(this.contentType).param("token", vt.getToken()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(jsonPath("timestamp").exists())
			.andExpect(jsonPath("path").exists())
			.andExpect(jsonPath("message").exists())
			.andExpect(jsonPath("userId").exists())
			.andExpect(jsonPath("userEmail").exists())
			.andExpect(jsonPath("path").value("/api/v1/email/confirm"))
			.andExpect(jsonPath("message").value("The user has been enabled!"))
			.andExpect(jsonPath("userEmail").value(this.validUserDtoMap.get("email")))
			.andExpect(jsonPath("userId").value(this.userRepository.findIdByEmail(this.validUserDtoMap.get("email"))));
	}
	
	@Test
	public void t7ConfirmRegistrationVerifiedUser() throws Exception {
		VerificationToken vt = verificationTokenRepository.findByUser(this.userRepository.findByEmail(this.validUserDtoMap.get("email")));
		
		this.mockMvc.perform(get("/api/v1/users/verification/email/confirm").contentType(this.contentType).param("token", vt.getToken()))
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(jsonPath("timestamp").exists())
		.andExpect(jsonPath("path").exists())
		.andExpect(jsonPath("message").exists())
		.andExpect(jsonPath("message").value("Verification error! Token has already been used and user has been verified"));	
	}	
}
