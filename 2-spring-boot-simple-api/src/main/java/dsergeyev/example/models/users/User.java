package dsergeyev.example.models.users;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
//import javax.validation.constraints.Past;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import dsergeyev.example.models.cards.CreditCard;
import dsergeyev.example.models.groups.Group;

@Entity
@Table(name = "USER")
public class User {

	private static final int MIN_LEGTH_OF_EMAIL_ADDRESS= 3;
	private static final int MAX_LEGTH_OF_EMAIL_ADDRESS = 254;
	private static final String NOT_EMPTY_EMAIL_ADDRESS_MESSAGE =
			"Contact email cannot be empty.";
	private static final String LEGTH_OF_EMAIL_ADDRESS_MESSAGE =
			"Length of email address must be between 3 and 254 characters";
	
	private static final int MIN_LEGTH_OF_FIRST_NAME = 2;
	private static final int MAX_LEGTH_OF_FIRST_NAME = 30;
	private static final String NOT_EMPTY_FIRST_NAME_MESSAGE =
			"Fisrt name cannot be empty.";
	private static final String LENGHT_OF_FIRST_NAME_MESSAGE = 
			"Length of first name must be between 2 and 30 characters";
	
	private static final int MIN_LEGTH_OF_SECOND_NAME = 2;
	private static final int MAX_LEGTH_OF_SECOND_NAME = 50;
	private static final String NOT_EMPTY_SECOND_NAME_MESSAGE =
			"Second name cannot be empty.";
	private static final String LENGHT_OF_SECOND_NAME_MESSAGE = 
			"Length of second name must be between 2 and 50 characters";
	
	private long id;
	private String email; 
	
	private String firstName;
	private String secondName;	
	private LocalDate dateOfBirth;	
	private String webSite;
		
	private LocalDateTime createDate;
	private LocalDateTime updateDate;

	private Set<CreditCard> cards = new HashSet<>();
	private Set<Group> groups = new HashSet<>();	
	
	@Override
	public String toString() {
		return "User [email=" + email + ", firstName=" + firstName + ", secondName=" + secondName + "]";
	}
	
	public User() {
		
	}
		
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	@Email
	@NotEmpty(message=NOT_EMPTY_EMAIL_ADDRESS_MESSAGE)
	@Length(min=MIN_LEGTH_OF_EMAIL_ADDRESS, max=MAX_LEGTH_OF_EMAIL_ADDRESS, 
		message=LEGTH_OF_EMAIL_ADDRESS_MESSAGE)
	@Column(name = "EMAIL", nullable=false, unique=true)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@NotEmpty(message=NOT_EMPTY_FIRST_NAME_MESSAGE)
	@Length(min=MIN_LEGTH_OF_FIRST_NAME, max=MAX_LEGTH_OF_FIRST_NAME, 
		message=LENGHT_OF_FIRST_NAME_MESSAGE)
	@Column(name="FISRT_NAME", nullable=false, length=30)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@NotEmpty(message=NOT_EMPTY_SECOND_NAME_MESSAGE)
	@Length(min=MIN_LEGTH_OF_SECOND_NAME, max=MAX_LEGTH_OF_SECOND_NAME, 
		message=LENGHT_OF_SECOND_NAME_MESSAGE)
	@Column(name="SECOND_NAME", nullable=false)
	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	@Column(name = "BIRTH_DATE", unique=false)
	@Type(type="org.hibernate.type.LocalDateType")
//	@Past(message="Date of birth must be date in the past.") - annotation not available for the 
//  data types from java.time.*, like LocalDate, in Hibernate version 5.0.11.Final.
// 	Will be available in version 5.2.0.Alfa - https://hibernate.atlassian.net/browse/HV-874
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	
	public void setDateOfBirth(LocalDate birthDate) {
		this.dateOfBirth = birthDate;
	}
	
	@Column(name = "CREATE_DATE")
	@Type(type="org.hibernate.type.LocalDateTimeType")
	@JsonProperty(access=Access.READ_ONLY)
	public LocalDateTime getCreateDate() {
		return createDate;
	}		

	public void setCreateDate(LocalDateTime startDate) {
		this.createDate = startDate;
	}
	
	@Column(name = "UPDATE_DATE")
	@Type(type="org.hibernate.type.LocalDateTimeType")	
	@JsonProperty(access=Access.READ_ONLY)
	public LocalDateTime getUpdateDate() {
		return updateDate;
	}
	
	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JsonIgnoreProperties(value="users", allowSetters=true)
	public Set<CreditCard> getCards() {
		return this.cards;
	}

	public void setCards(Set<CreditCard> cards) {
		this.cards = cards;
	}
	
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable (name = "user_community",
		joinColumns = @JoinColumn (name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "group_id"))
	@JsonIgnoreProperties(value="users", allowSetters=true)
	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}
	
	@URL(message="Web site field must contain valid URL")
	@Column(name = "WEB_SITE", unique=true)
	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
}
