package dsergeyev.example.models.chat;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import dsergeyev.example.models.message.CreateMessageDto;
import dsergeyev.example.models.message.Message;
import dsergeyev.example.models.message.MessageRepository;
import dsergeyev.example.models.user.User;
import dsergeyev.example.models.user.UserService;
import dsergeyev.example.resources.errorhanding.exception.ChatNotAvailableToUserException;
import dsergeyev.example.resources.errorhanding.exception.ResourceNotFoundException;
import dsergeyev.example.resources.httpresponse.info.CreateChatInfoHttpResponse;
import dsergeyev.example.resources.httpresponse.info.CreateMessageInfoHttpResponse;

@Service
public class ChatService {

	@Autowired
	private ChatRepository chatRepository;
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private UserService userService;

	private void checkChatExistsById(Long chatId) {
		if (!this.chatRepository.exitsById(chatId)) {
			throw new ResourceNotFoundException("Chat with id = '" + chatId + "' not found");
		}
	}

	private void checkChatAvailableToUser(Long chatId, User user) {
		if (!this.chatRepository.checkChatAvailableToUser(chatId, user)) {
			throw new ChatNotAvailableToUserException(
					"Chat with id = '" + chatId + "' isn't available to user with id = '" + user.getId() + "'");
		}
	}

	public ResponseEntity<?> createChat(CreateChatDto ccDto, HttpServletRequest request) {
		Set<User> users = new HashSet<>();
		ccDto.getUserIds().forEach(id -> users.add(this.userService.getUserById(id)));
		
		User user = this.userService.getAuthorisatedUser();
		users.add(user);
		
		Chat newChat = new Chat(ccDto.getTitle(), user, users);
		Chat createdChat = this.chatRepository.save(newChat);
		CreateChatInfoHttpResponse responseInfo = new CreateChatInfoHttpResponse(HttpStatus.OK,
				"Chat has been succefully created", request.getRequestURI(), createdChat.getId());
		return new ResponseEntity<>(responseInfo, null, HttpStatus.OK);
	}

	public ResponseEntity<?> getAllChat(Pageable pageable, HttpServletRequest request) {
		User user = this.userService.getAuthorisatedUser();
		return new ResponseEntity<>(this.chatRepository.findAllByUser(user, pageable), null, HttpStatus.OK);
	}

	public Chat getChatById(Long chatId, HttpServletRequest request) {
		User user = this.userService.getAuthorisatedUser();
		this.checkChatExistsById(chatId);
		this.checkChatAvailableToUser(chatId, user);
		return this.chatRepository.findOne(chatId);
	}

	public ResponseEntity<?> createMessage(CreateMessageDto cmDto, HttpServletRequest request) {
		this.checkChatExistsById(cmDto.getChatId());		

		User user = this.userService.getAuthorisatedUser();
		this.checkChatAvailableToUser(cmDto.getChatId(), user);
		
		Chat chat = this.chatRepository.findOne(cmDto.getChatId());		

		Message message = new Message(cmDto.getText(), chat);
		message.setSender(user);
		Message newMessage = this.messageRepository.save(message);

		chat.setLastMessage(newMessage);
		chat.setUpdateDate(ZonedDateTime.now());
		this.chatRepository.save(chat);

		CreateMessageInfoHttpResponse responseInfo = new CreateMessageInfoHttpResponse(HttpStatus.OK,
				"Message has been successfully sent", request.getRequestURI(), newMessage.getId());
		return new ResponseEntity<>(responseInfo, HttpStatus.OK);
	}

	public Page<Message> getMessages(Long chatId, Pageable pageable) {
		this.checkChatExistsById(chatId);
		
		User user = this.userService.getAuthorisatedUser();
		this.checkChatAvailableToUser(chatId, user);
		
		Chat chat = this.chatRepository.findOne(chatId);
		return this.messageRepository.findByChat(chat, pageable);
	}
}
