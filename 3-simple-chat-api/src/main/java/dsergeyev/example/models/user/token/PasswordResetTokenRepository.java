package dsergeyev.example.models.user.token;

import org.springframework.data.jpa.repository.JpaRepository;

import dsergeyev.example.models.user.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
	
	PasswordResetToken findByToken(String token);	 
	PasswordResetToken findByUser(User user);
 }
