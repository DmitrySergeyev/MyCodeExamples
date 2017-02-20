package dsergeyev.example.models.chat;

import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import dsergeyev.example.models.message.Message;
import dsergeyev.example.models.user.User;

@Entity
@Table(name = "chat")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@JsonPropertyOrder({ "id", "time", "updateDate", "lastMessage", "owner", "users" })
public class Chat {

	private Long id;
	private String title;
	private ZonedDateTime updateDate;

	private User owner;
	
	private Set<User> users;
	private Message lastMessage;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@NotEmpty
	@Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name="update_time")
	@JsonProperty(access=Access.READ_ONLY)
	public ZonedDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(ZonedDateTime updateDate) {
		this.updateDate = updateDate;
	}

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, targetEntity=Message.class)
	@JoinColumn(name="last_message_id")
	public Message getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(Message lastMessage) {
		this.lastMessage = lastMessage;
	}
	
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, targetEntity=User.class)
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name = "user_chat", 
	joinColumns = @JoinColumn (name = "chat_id", referencedColumnName="id"),
	inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName="id"))
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}	

	public Chat() {
		super();
	}

	public Chat(String title, User owner, Set<User> users) {
		super();
		this.title = title;
		this.owner = owner;
		this.users = users;
		this.updateDate = ZonedDateTime.now();
	}
}
