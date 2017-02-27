package dsergeyev.example.models.message;

import java.time.ZonedDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import dsergeyev.example.models.user.User;

@Entity
@Table(name = "sytem_message")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@JsonPropertyOrder({ "id", "time", "sender", "text"})
public class ServiceMessage {

	private Long id;
	private ZonedDateTime time;
	private User sender;
	private String text;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "TIME")
	@Type(type = "org.hibernate.type.ZonedDateTimeType")
	@JsonProperty(access = Access.READ_ONLY)
	public ZonedDateTime getTime() {
		return time;
	}

	public void setTime(ZonedDateTime time) {
		this.time = time;
	}
	
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, targetEntity=User.class)
	@JoinColumn(name="sender_id")
	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}
	
	@NotNull
	@NotEmpty
	@Column(name = "text", nullable = false)
	public String getText() {
		return text;
	}

	public void setText(String message) {
		this.text = message;
	}
	
	public ServiceMessage() {
		super();
	}

	public ServiceMessage(String text, User sender) {
		super();
		this.text = text;
		this.sender = sender;
		this.time = ZonedDateTime.now();
	}
}
