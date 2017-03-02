package dsergeyev.example.models.roles;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;

import dsergeyev.example.models.user.User;

@Entity
public class Role {

    private Long id; 
    private String name;
    
	@Id
	@Column(name="ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Length(min=3, max=25)
	@Column(name="NAME", unique=true, nullable=false, length=25)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}  

    public Role() {
    	
 	}
    
    public Role(String name) {
		this.name = name;
	} 
    
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Role role = (Role) o;

		if (id != role.id) return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result;
		return result;
	}
}
