package dsergeyev.example.models.user;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	
	public Page<User> findByDateOfBirthBetween(Pageable pageable, LocalDate from, LocalDate to);
	public Page<User> findByDateOfBirthLessThanEqual(Pageable pageable, LocalDate to);
	public Page<User> findByDateOfBirthGreaterThanEqual(Pageable pageable, LocalDate from);

	public User findByEmail (String email);
	
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.email = ?1")
	public Boolean exitsByEmail (String email);
		
	@Query("SELECT CASE WHEN COUNT(u) = 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.email = ?1")
	public Boolean isEmailAvalible(String email);
	
	@Query("SELECT email FROM User u WHERE u.id = ?1")
	public String findEmailById(Long id);
	
	@Query("SELECT id FROM User u WHERE u.email = ?1")
	public Long findIdByEmail(String email);
	
	@Query("SELECT password FROM User u WHERE u.email = ?1")
	public String findPasswordByEmail(String email);
 }