package dsergeyev.example.resources.registration.password.change;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import dsergeyev.example.resources.validation.password.ValidPassword;

public class PasswordChangeDto {

	private String oldPassword;
	private String newPassword;
	private String confirmationNewPassword;
	
	public PasswordChangeDto() {
		super();
	}
	
	@NotNull
	@NotEmpty
	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	@NotNull
	@NotEmpty
	@ValidPassword
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@NotNull
	@NotEmpty
	public String getConfirmationNewPassword() {
		return confirmationNewPassword;
	}

	public void setConfirmationNewPassword(String confirmationNewPassword) {
		this.confirmationNewPassword = confirmationNewPassword;
	}
}
