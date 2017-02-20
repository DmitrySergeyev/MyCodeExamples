package dsergeyev.example.resources.registration;

import org.springframework.context.ApplicationEvent;

import dsergeyev.example.models.user.User;

public class SendVerificationEmailEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	
	private final String appUrl;
	private final User user;

	public SendVerificationEmailEvent(User user, String appUrl) {
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
