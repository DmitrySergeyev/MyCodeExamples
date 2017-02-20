package dsergeyev.example.resources.registration.password.reset;

import org.springframework.context.ApplicationEvent;

import dsergeyev.example.models.user.User;

public class ResetUserPasswordEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	
	private final String appUrl;
	private final User user;

	public ResetUserPasswordEvent(User user, String appUrl) {
		super(user);

		this.user = user;
		this.appUrl = appUrl;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public User getUser() {
		return user;
	}
}
