package dsergeyev.example.models.chat;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dsergeyev.example.models.message.CreateMessageDto;
import dsergeyev.example.models.message.Message;
import dsergeyev.example.models.message.MessageRepository;
import dsergeyev.example.models.user.User;
import dsergeyev.example.models.user.UserService;
import dsergeyev.example.resources.errorhanding.exception.ChatNotAvailableToUserException;
import dsergeyev.example.resources.errorhanding.exception.ResourceNotFoundException;

@Service
public class DefaultChatService implements ChatService {

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
	
	private Chat getChatForUser(Long chatId) {
		this.checkChatExistsById(chatId);
		User user = this.userService.getAuthorisatedUser();
		this.checkChatAvailableToUser(chatId, user);
		return this.chatRepository.findOne(chatId);
	}

	@Override
	public Chat createChat(CreateChatDto ccDto) {
		User sender = this.userService.getAuthorisatedUser();
		Set<User> users = ccDto.getUserIds().stream().map(id -> this.userService.getUserById(id))
				.collect(Collectors.toSet());

		users.add(sender);

		Chat newChat = new Chat(ccDto.getTitle(), sender, users);
		Chat createdChat = this.chatRepository.save(newChat);

		return createdChat;
	}
	
	@Override
	public Set<Chat> getAllChat() {
		return this.chatRepository.findAllByUser(this.userService.getAuthorisatedUser());
	}
	
	@Override
	public Page<Chat> getAllChat(Pageable pageable) {
		return this.chatRepository.findAllByUser(this.userService.getAuthorisatedUser(), pageable);
	}

	@Override
	public Chat getChatById(Long chatId) throws ResourceNotFoundException {
		User user = this.userService.getAuthorisatedUser();
		this.checkChatExistsById(chatId);
		this.checkChatAvailableToUser(chatId, user);
		return this.chatRepository.findOne(chatId);
	}

	@Override
	public Message sendMessage(Long chatId, CreateMessageDto cmDto) {
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

	@Override
	public Page<Message> getMessagesByChatId(Long chatId, Pageable pageable) throws ResourceNotFoundException {
		return this.messageRepository.findByChat(this.getChatForUser(chatId), pageable);
	}
	
	@Override
	public Set<Message> getMessagesByChatId(Long chatId) throws ResourceNotFoundException {
		return this.messageRepository.findByChat(this.getChatForUser(chatId));
	}
}
