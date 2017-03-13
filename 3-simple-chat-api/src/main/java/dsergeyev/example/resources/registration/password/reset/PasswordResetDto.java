package dsergeyev.example.resources.registration.password.reset;

import org.hibernate.validator.constraints.NotEmpty;

import dsergeyev.example.resources.validation.password.ValidPasswordOrNull;

public class PasswordResetDto {

	private String newPassword;
	private String confirmationNewPassword;
	private String token;
	
	public PasswordResetDto() {
		super();
	}
	
	@NotEmpty
	@ValidPasswordOrNull
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@NotEmpty
	public String getConfirmationNewPassword() {
		return confirmationNewPassword;
	}

	public void setConfirmationNewPassword(String confirmationNewPassword) {
		this.confirmationNewPassword = confirmationNewPassword;
	}
	
	@NotEmpty
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
