package dsergeyev.example.models.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dsergeyev.example.models.roles.Role;
import dsergeyev.example.resources.errorhanding.exception.ResourceNotFoundException;

public interface UserService {
	
	boolean checkIsEmailAvailable(String email);
	Role getUserRoleByRoleName(String roleName);	
	User getAuthorisatedUser();
	String getAuthorisatedUserEmail();
	
	User saveUser(User user);
	User getUserById(Long userId) throws ResourceNotFoundException;
	User getUserByEmail(String email) throws ResourceNotFoundException;
	Page<User> getAllUsers(String search, Pageable pageable);
	User updateUser(EditUserDto editUserDto);
	
	void checkPassword(String email, String password);
	void changeUserPassword(User user, String newPassword);
}