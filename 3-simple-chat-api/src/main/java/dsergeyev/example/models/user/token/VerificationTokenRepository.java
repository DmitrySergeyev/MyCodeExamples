package dsergeyev.example.models.user.token;

import org.springframework.data.jpa.repository.JpaRepository;

import dsergeyev.example.models.user.User;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
	
	VerificationToken findByToken(String token);	 
    VerificationToken findByUser(User user);
    
//	@Query("SELECT t FROM VerificationToken t WHERE t.user_id = ?1")
//	VerificationToken findByUserId(Long userId);
 }
