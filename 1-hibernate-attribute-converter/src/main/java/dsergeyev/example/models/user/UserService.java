package dsergeyev.example.models.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    
	public List<User> getAllUsers() {
		return (List<User>) this.userRepository.findAll();
	}

	public User getUser(Long id) {
		return this.userRepository.findOne(id);
	}	

	public void createUser(User user) {
		this.userRepository.save(user);		
	}

	public void updateUser(User user) {
		this.userRepository.save(user);		
	} 
	
	public void deleteUser(Long id) {
		this.userRepository.delete(id);
	}
}
