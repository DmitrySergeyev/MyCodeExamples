package dsergeyev.example.models.chat;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
	@Autowired
	private SimpMessagingTemplate template;

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

	public Chat createChat(CreateChatDto ccDto) {
		User sender = this.userService.getAuthorisatedUser();
		Set<User> users = ccDto.getUserIds().stream().map(id -> this.userService.getUserById(id))
				.collect(Collectors.toSet());

		users.add(sender);

		Chat newChat = new Chat(ccDto.getTitle(), sender, users);
		Chat createdChat = this.chatRepository.save(newChat);

		return createdChat;
	}

	public ResponseEntity<?> createChatAndGetResponseEntity(CreateChatDto ccDto, HttpServletRequest request) {
		CreateChatInfoHttpResponse responseInfo = new CreateChatInfoHttpResponse(HttpStatus.OK,
				"Chat has been succefully created", request.getRequestURI(), this.createChat(ccDto).getId());
		return new ResponseEntity<>(responseInfo, null, HttpStatus.OK);
	}

	public Set<Chat> getAllChat(User user) {
		return this.chatRepository.findAllByUser(user);
	}

	public ResponseEntity<?> getAllChatAndGetResponseEntity(Pageable pageable, HttpServletRequest request) {
		return new ResponseEntity<>(this.chatRepository.findAllByUser(this.userService.getAuthorisatedUser(), pageable),
				null, HttpStatus.OK);
	}

	public Chat getChatById(Long chatId) {
		User user = this.userService.getAuthorisatedUser();
		this.checkChatExistsById(chatId);
		this.checkChatAvailableToUser(chatId, user);
		return this.chatRepository.findOne(chatId);
	}

	public Message createMessage(Long chatId, CreateMessageDto cmDto) {
		this.checkChatExistsById(chatId);

		User user = this.userService.getAuthorisatedUser();
		this.checkChatAvailableToUser(chatId, user);

		Chat chat = this.chatRepository.findOne(chatId);

		Message message = new Message(cmDto.getText(), chat, user);
		Message newMessage = this.messageRepository.save(message);

		chat.setLastMessage(newMessage);
		chat.setUpdateDate(ZonedDateTime.now());
		this.chatRepository.save(chat);

		return newMessage;
	}

	public ResponseEntity<?> createMessageAndGetResponseEntity(Long chatId, CreateMessageDto cmDto,
			HttpServletRequest request) {
		CreateMessageInfoHttpResponse responseInfo = new CreateMessageInfoHttpResponse(HttpStatus.OK,
				"Message has been successfully sent", request.getRequestURI(),
				this.createMessage(chatId, cmDto).getId());
		return new ResponseEntity<>(responseInfo, HttpStatus.OK);
	}

	public Page<Message> getMessages(Long chatId, Pageable pageable) {
		this.checkChatExistsById(chatId);
		User user = this.userService.getAuthorisatedUser();
		this.checkChatAvailableToUser(chatId, user);
		Chat chat = this.chatRepository.findOne(chatId);
		return this.messageRepository.findByChat(chat, pageable);
	}

	public Set<Message> getMessages(Long chatId) {
		this.checkChatExistsById(chatId);
		User user = this.userService.getAuthorisatedUser();
		this.checkChatAvailableToUser(chatId, user);
		Chat chat = this.chatRepository.findOne(chatId);
		return this.messageRepository.findByChat(chat);
	}
}
