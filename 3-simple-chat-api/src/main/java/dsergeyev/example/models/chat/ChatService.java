package dsergeyev.example.models.chat;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dsergeyev.example.models.message.CreateMessageDto;
import dsergeyev.example.models.message.Message;
import dsergeyev.example.resources.errorhanding.exception.ResourceNotFoundException;

public interface ChatService {
	
	Chat createChat(CreateChatDto ccDto);
	
	Set<Chat> getAllChat();
	Page<Chat> getAllChat(Pageable pageable);
	
	Chat getChatById(Long chatId) throws ResourceNotFoundException;
	
	Message sendMessage(Long chatId, CreateMessageDto cmDto);
	
	Set<Message> getMessagesByChatId(Long chatId) throws ResourceNotFoundException;
	Page<Message> getMessagesByChatId(Long chatId, Pageable pageable) throws ResourceNotFoundException;
}
