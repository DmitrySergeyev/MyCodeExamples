package dsergeyev.example.models.user;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import dsergeyev.example.resources.validation.date.PastDateOrNull;

public class EditUserDto {

	private String password;
	private String firstName;
	private String secondName;
	private LocalDate dateOfBirth;
	private String webSite;
	
	@NotEmpty
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@NotEmpty
	@Length(min = 2, max = 50)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@NotEmpty
	@Length(min = 2, max = 50)
	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	
	@PastDateOrNull
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate birthDate) {
		this.dateOfBirth = birthDate;
	}
	
	@URL
	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
}
