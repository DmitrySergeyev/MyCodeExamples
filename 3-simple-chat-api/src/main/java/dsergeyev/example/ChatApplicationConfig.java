package dsergeyev.example;

import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
public class ChatApplicationConfig {
	
	public static final String REQUEST_MAPPING_PREFIX = "/api/v1";

	public static final String USERS = REQUEST_MAPPING_PREFIX + "/users";
	public static final String USERS_REGISTRATION = USERS + "/registration";
	
	public static final String USERS_VERIFICATION = USERS + "/verification";
	public static final String USERS_VERIFICATION_EMAIL_RESET = USERS_VERIFICATION + "/email/resend";
	public static final String USERS_VERIFICATION_EMAIL_CONFIRM = USERS_VERIFICATION + "/email/confirm";
	
	public static final String USERS_RESET_PASSWORD = USERS + "/reset-password";
	public static final String USERS_RESET_PASSWORD_CONFIRM = USERS_RESET_PASSWORD + "/confirm";
	
	public static final String USERS_CHANGE_PASSWORD = USERS + "/change-password";
	public static final String USERS_RESET_USER_PASSWORD = USERS + "/reset-password";
	public static final String USERS_COMPLETE_RESET_USER_PASSWORD = USERS + "/reset-password/confirm";
	
	public static final String USERS_ID = USERS + "/{id}";
	public static final String USERS_EMAIL = USERS + "/email";
	public static final String USERS_CHECK_BY_EMAIL = USERS + "/check-by-email";
	public static final String USERS_BITHDATE_BETWEEN = USERS + "/bithday/between";
	public static final String USERS_BITHDATE_LESS_THAN = USERS + "/bithday/less-than";
	public static final String USERS_BITHDATE_GREATER_THAN = USERS + "/users/bithday/greater-than";
		
	public static final String CHATS = REQUEST_MAPPING_PREFIX + "/chats";
	public static final String CHATS_ID = CHATS + "/{id}";
	public static final String CHATS_SECURITY = CHATS + "/**";
	
	public static final String MESSAGES = REQUEST_MAPPING_PREFIX + "/messages";
	public static final String MESSAGES_SECURITY = MESSAGES + "/**";

	@Bean
	public Validator validator() {
	    return new LocalValidatorFactoryBean();
	}

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
	    MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
	    methodValidationPostProcessor.setValidator(validator());
	    return methodValidationPostProcessor;
	}
}
