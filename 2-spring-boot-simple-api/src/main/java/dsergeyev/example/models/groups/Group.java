package dsergeyev.example.models.groups;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dsergeyev.example.models.users.User;

@Entity
@Table(name = "COMMUNITY")
public class Group {

	private long id;
	private String name;
	private String description;
	
	private Set<User> users = new HashSet<>();
	
	public Group() {
		
	}
	
	public Group(String name, String description) {
		this.name = name;
		this.description = description;
	}	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID", nullable=false, unique=true)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@NotEmpty(message="Group name cannot be empty.")
	@Column(name="NAME", nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotEmpty(message="Group description cannot be empty.")
	@Column(name="DESCRIPTION", nullable=true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToMany(cascade=CascadeType.ALL, mappedBy="groups")
	@JsonIgnoreProperties(value="groups", allowSetters=true)
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
}
