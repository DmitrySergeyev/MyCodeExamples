package dsergeyev.example.models.cards;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import dsergeyev.example.models.users.User;

@Entity
@Table(name = "CREDIT_CARD")
public class CreditCard {
	
	private Long id;
	private CreditCardType type;
	private String number;
	private User user;
	
	public CreditCard() {
		
	}
	
	public CreditCard(CreditCardType type, String number, User user) {
		this.type = type;
		this.number = number;
		this.user = user;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID", nullable=false, unique=true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull(message="Card type cannot be empty.")
	@Enumerated(EnumType.STRING)
	@Column(name="CARD_TYPE", nullable=false)
	public CreditCardType getType() {
		return type;
	}

	public void setType(CreditCardType type) {
		this.type = type;
	}

	@CreditCardNumber(message="Card number field must contain valid card number (the Luhn algorithm).")
	@Column(name="CARD_NUMBER", nullable=false, unique=true)
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name = "USER_ID", nullable = false)
	@JsonIgnoreProperties(value="cards", allowSetters=true)
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
}
