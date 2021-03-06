package dsergeyev.example.security;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import dsergeyev.example.models.chat.Chat;
import dsergeyev.example.models.chat.ChatRepository;
import dsergeyev.example.models.roles.Role;
import dsergeyev.example.models.roles.RoleRepository;
import dsergeyev.example.models.user.User;
import dsergeyev.example.models.user.UserService;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent>  {

	boolean alreadySetup = false;

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private ChatRepository chatRepositiry;	
	@Autowired
	private UserService userService;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (alreadySetup)
			return;
		
		Role adminRole = new Role("ROLE_ADMIN");
		Role moderRole = new Role("ROLE_MODERATOR");
		Role userRole = new Role("ROLE_USER");
		adminRole = this.roleRepository.save(adminRole);
		moderRole = this.roleRepository.save(moderRole);
		userRole = this.roleRepository.save(userRole);
		
		User testAdmin = new User("admin1@gmail.com", "admin1", true, adminRole, "Admin1", "Admin2", LocalDate.of(1991, 1, 1), "https://github.com");
		User moderUser = new User("moder1@gmail.com", "moder1", true, moderRole, "Moder1", "Moder2", LocalDate.of(1992, 2, 2), "https://vk.com");
		
		User testUser1 = new User("user1@gmail.com", "user1", true, userRole, "User1-1", "User1-2", LocalDate.of(1993, 3, 3), null);
		User testUser2 = new User("user2@gmail.com", "user2", true, userRole, "User2-1", "User2-2", LocalDate.of(1994, 4, 4), null);
		User testUser3 = new User("user3@gmail.com", "user3", true, userRole, "User3-1", "User3-2", null, "https;//my-web-site.com");
		User testUser4 = new User("user4@gmail.com", "user4", true, userRole, "User4-1", "User4-2", LocalDate.of(1996, 6, 6), null);
		User testUser5 = new User("user5@gmail.com", "user5", true, userRole, "User5-1", "User5-2", LocalDate.of(1997, 7, 7), null);
			
		Set<User> users1 = new HashSet<>();
		users1.add(testUser2);
		users1.add(testUser3);
		users1.add(testUser4);
		
		Set<User> users2 = new HashSet<>();
		users2.add(testUser1);
		users2.add(testUser2);
		users2.add(testUser5);
		
		Chat conversation1 = new Chat("User 2, 3, 4 chat!", testUser2, users1);
		Chat conversation2 = new Chat("User 1, 2, 5 chat!", testUser5, users2);
			
		this.userService.saveUser(testAdmin);
		this.userService.saveUser(moderUser);
		this.userService.saveUser(testUser1);
		this.userService.saveUser(testUser2);
		this.userService.saveUser(testUser3);
		this.userService.saveUser(testUser4);
		this.userService.saveUser(testUser5);
		
		this.chatRepositiry.save(conversation1);
		this.chatRepositiry.save(conversation2);
				
	    alreadySetup = true;
	}
}
