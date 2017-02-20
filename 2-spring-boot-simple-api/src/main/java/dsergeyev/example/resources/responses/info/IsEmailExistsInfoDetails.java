package dsergeyev.example.resources.responses.info;

public class IsEmailExistsInfoDetails extends StandartInfoDetails {
	
	private String requredEmail;
	private boolean isEmailAwailable;

	public String getRequredEmail() {
		return requredEmail;
	}

	public void setRequredEmail(String requredEmail) {
		this.requredEmail = requredEmail;
	}

	public boolean getIsEmailAvailable() {
		return isEmailAwailable;
	}

	public void setIsEmailAvailable(boolean isEmailExists) {
		this.isEmailAwailable = isEmailExists;
	}
	

}
