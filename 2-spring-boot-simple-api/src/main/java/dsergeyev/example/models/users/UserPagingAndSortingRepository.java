package dsergeyev.example.models.users;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserPagingAndSortingRepository extends PagingAndSortingRepository<User, Long> {
	
	public Page<User> findByDateOfBirthBetween(Pageable pageable, LocalDate from, LocalDate to);
	public Page<User> findByDateOfBirthLessThanEqual(Pageable pageable, LocalDate to);
	public Page<User> findByDateOfBirthGreaterThanEqual(Pageable pageable, LocalDate from);

	public User findOneByEmail(String email);
		
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.email = ?1")
	public Boolean existsByEmail(String email);
	
	@Query("SELECT email FROM User u WHERE u.id = ?1")
	public String findEmailById(Long id);
 }