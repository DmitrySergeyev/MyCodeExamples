package dsergeyev.example.models.user;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import dsergeyev.example.models.roles.Role;
import dsergeyev.example.resources.validation.date.PastDateOrNull;
import dsergeyev.example.resources.validation.email.ValidEmailOrNull;
import dsergeyev.example.resources.validation.password.UserPasswordsMatch;
import dsergeyev.example.resources.validation.password.ValidPasswordOrNull;

@Entity
@Table(name = "user")
@UserPasswordsMatch
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonPropertyOrder({ "id", "email", "enabled", "role", "firstName", "secondName", "dateOfBirth", "webSite", "photo",
		"createDate", "updateDate" })
public class User implements Cloneable {

	private long id;
	private String email;
	private String password;
	private String confirmationPassword;

	private boolean enabled;

	private Role role;

	private String firstName;
	private String secondName;
	private LocalDate dateOfBirth;
	private String webSite;
	private String photo;

	private ZonedDateTime createDate;
	private ZonedDateTime updateDate;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@NotNull
	@ValidEmailOrNull
	@Column(name = "email", nullable = false, unique = true, length = 254)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@ValidPasswordOrNull
	@Column(name = "password")
	@JsonProperty(access = Access.WRITE_ONLY)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	public String getConfirmationPassword() {
		return confirmationPassword;
	}

	public void setConfirmationPassword(String matchingPassword) {
		this.confirmationPassword = matchingPassword;
	}

	@Column(name = "enabled")
	@JsonProperty(access = Access.READ_ONLY)
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@NotNull
	@Length(min = 2, max = 50)
	@Column(name = "first_name", nullable = false, length = 50)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@NotNull
	@Length(min = 2, max = 50)
	@Column(name = "second_name", nullable = false, length = 50)
	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	@Column(name = "birth_date", unique = false)
	@Type(type = "org.hibernate.type.LocalDateType")
	@PastDateOrNull
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate birthDate) {
		this.dateOfBirth = birthDate;
	}

	@Column(name = "create_date")
	@Type(type = "org.hibernate.type.ZonedDateTimeType")
	@JsonProperty(access = Access.READ_ONLY)
	public ZonedDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(ZonedDateTime startDate) {
		this.createDate = startDate;
	}

	@Column(name = "update_date")
	@Type(type = "org.hibernate.type.ZonedDateTimeType")
	@JsonProperty(access = Access.READ_ONLY)
	public ZonedDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(ZonedDateTime updateDate) {
		this.updateDate = updateDate;
	}

	@URL
	@Column(name = "web_site")
	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	@URL
	@Column(name = "photo")
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Role.class)
	@JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
	@JsonProperty(access = Access.READ_ONLY)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User [email = " + email + ", firstName = " + firstName + ", secondName = " + secondName + "]";
	}

	public User() {
		super();
	}

	public User(String email, String password, boolean enabled, Role role, String firstName, String secondName,
			LocalDate dateOfBirth, String webSite) {
		super();
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.setRole(role);
		this.firstName = firstName;
		this.secondName = secondName;
		this.dateOfBirth = dateOfBirth;
		this.webSite = webSite;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		User user = (User) o;

		if (id != user.id)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result;
		return result;
	}

	@Override
	public User clone() throws CloneNotSupportedException {
		User newUser = new User(this.getEmail(), this.getPassword(), this.isEnabled(), this.getRole(),
				this.getFirstName(), this.getSecondName(), this.getDateOfBirth(), this.getWebSite());
		newUser.setConfirmationPassword(this.getConfirmationPassword());
		return newUser;
	}
}
