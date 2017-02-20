package dsergeyev.example.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dsergeyev.example.ChatApplicationConfig;
import dsergeyev.example.models.chat.Chat;
import dsergeyev.example.models.chat.ChatService;
import dsergeyev.example.models.chat.CreateChatDto;
import dsergeyev.example.models.message.CreateMessageDto;

@RestController
public class ChatController {
	
	private static final int DEFAULT_PAGE_NUM = 0;
	private static final int DEFAULT_PAGE_SIZE = 20;
	private static final String DEFAULT_MESSAGES_SORT = "time";
	private static final String DEFAULT_CHATS_SORT = "updateDate";
	
	@Autowired
	private ChatService chatService;
	
	/*
	 * 1. CREATE CHAT 
	 */
	// 1.1 - Create new chat
	@RequestMapping(method = RequestMethod.POST, value = ChatApplicationConfig.CHATS)
	public ResponseEntity<?> registrationUser(@Valid @RequestBody CreateChatDto ccDto, HttpServletRequest request) {
		return this.chatService.createChat(ccDto, request);
	}
	
	/*
	 * 2. GET CHAT(-S)
	 */
	// 2.1 - Get all chats for current user
	@RequestMapping(method = RequestMethod.GET, value = ChatApplicationConfig.CHATS)
	public ResponseEntity<?> getAllChats(HttpServletRequest request,
			@PageableDefault(page = DEFAULT_PAGE_NUM, size = DEFAULT_PAGE_SIZE, sort = DEFAULT_CHATS_SORT, direction=Direction.DESC) Pageable pageable) {
		return this.chatService.getAllChat(pageable, request);
	}

	// 2.2 - Get chat by id
	@RequestMapping(method = RequestMethod.GET, value = ChatApplicationConfig.CHATS_ID)
	public ResponseEntity<?> getChatById(@PathVariable Long id, HttpServletRequest request) {
		Chat chat = this.chatService.getChatById(id, request);
		return new ResponseEntity<>(chat, HttpStatus.OK);
	}
	
	/*
	 * 3. SEND MESSAGE
	 */
	@RequestMapping(method = RequestMethod.POST, value = ChatApplicationConfig.MESSAGES)
	public ResponseEntity<?> sendMessage(@Valid @RequestBody CreateMessageDto cmDto, HttpServletRequest request) {
		return this.chatService.createMessage(cmDto, request);
	}
	
	/*
	 * 4. GET MESSAGES OF CHAT
	 */
	@RequestMapping(method = RequestMethod.GET, value = ChatApplicationConfig.MESSAGES)
	public ResponseEntity<?> getMessages(@RequestParam Long chatId,
			@PageableDefault(page = DEFAULT_PAGE_NUM, size = DEFAULT_PAGE_SIZE, sort = DEFAULT_MESSAGES_SORT, direction=Direction.DESC) Pageable pageable) {
		return new ResponseEntity<>(this.chatService.getMessages(chatId, pageable), HttpStatus.OK);
	}
}
