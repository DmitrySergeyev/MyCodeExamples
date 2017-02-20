package dsergeyev.example.models.user.token;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import dsergeyev.example.models.user.User;

@Entity
@Table(name = "verification_token")
public class VerificationToken {

	private static final int EXPIRATION = 24;

	private Long id;
	private String token;
	private User user;
	private ZonedDateTime expiryDate;
	private boolean verified;

	private ZonedDateTime calculateExpiryDate(int expiryTimeInHours) {
		return ZonedDateTime.now().plusHours(expiryTimeInHours);
	}

	public VerificationToken() {
		super();
	}

	public VerificationToken(String token, User user) {
		super();
		this.token = token;
		this.user = user;
		this.expiryDate = calculateExpiryDate(EXPIRATION);
		this.setVerified(false);
	}
	
	public VerificationToken refresh(String newToken) {
		this.token = newToken;
		this.expiryDate = calculateExpiryDate(EXPIRATION);
		
		return this;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Type(type = "org.hibernate.type.ZonedDateTimeType")
	public ZonedDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(ZonedDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}
}
