package dsergeyev.example.controllers.websockets;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import dsergeyev.example.models.chat.Chat;
import dsergeyev.example.models.chat.DefaultChatService;
import dsergeyev.example.models.chat.CreateChatDto;
import dsergeyev.example.models.message.CreateMessageDto;
import dsergeyev.example.models.message.Message;
import dsergeyev.example.models.user.User;
import dsergeyev.example.models.user.UserService;

@Controller
public class ChatWebSocketController {

	@Autowired
	private DefaultChatService chatService;
	@Autowired
	private SimpMessagingTemplate template;
	@Autowired
	private UserService userService;
	
	public static final String CHATS = "/chats";
	public static final String QUEUE = "/queue";
	public static final String MESSAGES = "/messages";
	
	public static final String GET_ALL = "/get-all";
	public static final String SEND = "/send";
	public static final String CREATE = "/create";
	
	public static final String CHATS_MESSAGES = CHATS + MESSAGES;
	
	public static final String CHATS_CREATE = CHATS + CREATE;
	public static final String QUEUE_CHATS_CREATE = QUEUE + CHATS_CREATE;
	
	public static final String CHATS_GET_ALL = CHATS + GET_ALL;
	public static final String QUEUE_CHATS_GET_ALL = QUEUE + CHATS_GET_ALL;
	
	public static final String CHATS_ID = CHATS + "/id";
	public static final String CHATS_ID_id = CHATS_ID + "/{id}";	
	public static final String QUEUE_CHATS_ID = QUEUE + CHATS_ID;
	
	public static final String CHATS_id = CHATS + "/{id}";
	public static final String CHATS_id_MESSAGES = CHATS_id + MESSAGES;
	public static final String CHATS_id_MESSAGES_SEND = CHATS_id_MESSAGES + SEND;
	public static final String QUEUE_CHATS_MESSAGES = QUEUE + CHATS_MESSAGES;
	public static final String QUEUE_CHATS_MESSAGES_SEND = QUEUE_CHATS_MESSAGES + SEND;	

	public static final String CHATS_id_MESSAGES_GET_ALL = CHATS_id_MESSAGES + GET_ALL;
	public static final String QUEUE_CHATS_MESSAGES_GET_ALL = QUEUE_CHATS_MESSAGES + GET_ALL;
	

	// 1 - Create new chat
	// JavaScript request example: 
	// stompClient.send("/app/chats/create", {}, JSON.stringify({ 'title':'Test chat 1', 'userIds': [1, 2, 3]}));
	@MessageMapping(CHATS_CREATE)
	@SendToUser(QUEUE_CHATS_CREATE)
	public Chat createNewChat(CreateChatDto ccDto) {
		User user = this.userService.getAuthorisatedUser();
		Chat createdChat = this.chatService.createChat(ccDto);
		// For all participants of the current chat, except sender, send notification about creating new chat to "/user/queue/chats/messages/incoming"		
		createdChat.getUsers().stream().filter(u -> !u.equals(user)).forEach(u -> this.template.convertAndSendToUser(u.getEmail(), "/queue/chats/incoming", createdChat));
		// For sender also return created chat to "/chat/create"
		return createdChat;
	}

	// 2 - Get all chats for current user
	// JavaScript request example: stompClient.send("/app/chats/get-all", {}, {});
	@SubscribeMapping(CHATS_GET_ALL)
	@SendToUser(QUEUE_CHATS_GET_ALL)
	public Set<Chat> getAllChats() {
		return this.chatService.getAllChat();
	}

	// 3 - Get chats by id
	// JavaScript request example: stompClient.send("/app/chats/id/1", {}, {});
	@SubscribeMapping(CHATS_ID_id)
	@SendToUser(QUEUE_CHATS_ID)
	public Chat getChatById(@DestinationVariable Long id) {
		return this.chatService.getChatById(id);
	}
	
	// 4 - Send message
	// JavaScript request example: stompClient.send("/app/chats/1/messages/send", {}, JSON.stringify({ 'text':'Test message' }));
	@SubscribeMapping(CHATS_id_MESSAGES_SEND)
	@SendToUser(QUEUE_CHATS_MESSAGES_SEND)
	public Message sendMessage(@DestinationVariable(value = "id") Long chatId, CreateMessageDto cmDto) {
		User user = this.userService.getAuthorisatedUser();
		Message sendedMessage = this.chatService.sendMessage(chatId, cmDto);
		// For all participants of the current chat, except sender, send message to "/user/queue/chats/messages/incoming"
		sendedMessage.getChat().getUsers().stream().filter(u -> !u.equals(user)).forEach(u -> this.template.convertAndSendToUser(u.getEmail(), "/queue/chats/messages/incoming", sendedMessage));
		// For sender return message back to "/queue/chats/messages/send"
		return sendedMessage;
	}

	// 5 - Get messages
	// JavaScript request example: stompClient.send("/app/chats/1/messages/get-all", {}, {});
	@SubscribeMapping(CHATS_id_MESSAGES_GET_ALL)
	@SendToUser(QUEUE_CHATS_MESSAGES_GET_ALL)
	public Set<Message> getMessages(@DestinationVariable(value = "id") Long id) {
		return this.chatService.getMessagesByChatId(id);
	}
}
