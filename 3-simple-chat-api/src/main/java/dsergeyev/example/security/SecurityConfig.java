package dsergeyev.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import dsergeyev.example.controllers.rest.RoleRestController;
import dsergeyev.example.controllers.rest.UserRestController;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.authorizeRequests()				
					// User registration and email verification
					.antMatchers(HttpMethod.POST, UserRestController.USERS_REGISTRATION).permitAll()
					.antMatchers(HttpMethod.GET, UserRestController.USERS_VERIFICATION_EMAIL_RESET).permitAll()
					.antMatchers(HttpMethod.GET, UserRestController.USERS_VERIFICATION_EMAIL_CONFIRM).permitAll()
					// Reset password (forgot password)
					.antMatchers(HttpMethod.GET, UserRestController.USERS_RESET_PASSWORD).permitAll()
					.antMatchers(HttpMethod.POST, UserRestController.USERS_RESET_PASSWORD_CONFIRM).permitAll()
					// Check if user exists by email
					.antMatchers(HttpMethod.GET, UserRestController.USERS_CHECK_BY_EMAIL).permitAll()
					// Delete user
					.antMatchers(HttpMethod.DELETE, UserRestController.USERS_ID).hasRole("ADMIN")
					// Get roles
					.antMatchers(HttpMethod.GET, RoleRestController.ROLE).hasRole("ADMIN")
					// All other requests
					.anyRequest().authenticated()
			.and()
				.httpBasic()
			.and()
				.csrf().disable();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	 @Bean
	 public SecurityContextHolder securityContextHolder() {
		 return new SecurityContextHolder();
	 }
}
