package dsergeyev.example.models.chat;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dsergeyev.example.models.user.User;

public interface ChatRepository extends JpaRepository<Chat, Long> {

	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN 'true' ELSE 'false' END FROM Chat c WHERE c.id = ?1")
	public Boolean exitsById(Long id);

	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN 'true' ELSE 'false' END FROM Chat c WHERE (c.id = ?1 AND ?2 in elements(c.users))")
	public Boolean checkChatAvailableToUser(Long chatId, User user);

	@Query(value = "SELECT c FROM Chat c WHERE :user in elements(c.users)")
	public Page<Chat> findAllByUser(@Param("user") User user, Pageable pageable);

	@Query(value = "SELECT c FROM Chat c WHERE :user in elements(c.users)")
	public Set<Chat> findAllByUser(@Param("user") User user);
	
//	public Set<ChatWithoutLastMessage> findAllByOwner(User user);
}
