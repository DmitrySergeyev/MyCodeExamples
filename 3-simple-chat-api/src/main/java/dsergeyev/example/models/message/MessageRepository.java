package dsergeyev.example.models.message;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dsergeyev.example.models.chat.Chat;

public interface MessageRepository extends JpaRepository<Message, Long> {

	public Page<Message> findByChat(Chat chat, Pageable pageable);
	public Set<Message> findByChat(Chat chat);
}
