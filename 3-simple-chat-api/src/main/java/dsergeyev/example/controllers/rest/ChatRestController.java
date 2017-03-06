package dsergeyev.example.controllers.rest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dsergeyev.example.ChatApplicationConfig;
import dsergeyev.example.controllers.ControllersHelper;
import dsergeyev.example.models.chat.DefaultChatService;
import dsergeyev.example.models.chat.CreateChatDto;
import dsergeyev.example.models.message.CreateMessageDto;
import dsergeyev.example.resources.httpresponse.info.CreateChatInfoHttpResponse;
import dsergeyev.example.resources.httpresponse.info.CreateMessageInfoHttpResponse;

@RestController
public class ChatRestController {

	public static final String CHATS = ChatApplicationConfig.API_VERSION_PREFIX + "/chats";
	public static final String CHATS_ID = CHATS + "/{id}";
	public static final String CHATS_SECURITY = CHATS + "/**";

	public static final String CHAT_ID_MESSAGES = CHATS_ID + "/messages";
	public static final String MESSAGES_SECURITY = CHAT_ID_MESSAGES + "/**";

	private static final int DEFAULT_PAGE_NUM = 0;
	private static final int DEFAULT_PAGE_SIZE = 20;
	private static final String DEFAULT_MESSAGES_SORT = "time";
	private static final String DEFAULT_CHATS_SORT = "updateDate";

	@Autowired
	private DefaultChatService chatService;

	// 1 - Create new chat
	@PostMapping(value = CHATS)
	public ResponseEntity<?> createNewChat(@Valid @RequestBody CreateChatDto ccDto, HttpServletRequest request) {
		CreateChatInfoHttpResponse responseInfo = new CreateChatInfoHttpResponse("Chat has been succefully created",
				request.getRequestURI(), this.chatService.createChat(ccDto).getId());
		return new ResponseEntity<>(responseInfo, ControllersHelper.getHeadersWithJsonContextType(), HttpStatus.OK);
	}

	// 2 - Get all chats for current user
	@GetMapping(value = CHATS)
	public ResponseEntity<?> getAllChats(HttpServletRequest request,
			@PageableDefault(page = DEFAULT_PAGE_NUM, size = DEFAULT_PAGE_SIZE, sort = DEFAULT_CHATS_SORT, direction = Direction.DESC) Pageable pageable) {
		return new ResponseEntity<>(this.chatService.getAllChat(pageable),
				ControllersHelper.getHeadersWithJsonContextType(), HttpStatus.OK);
	}

	// 3 - Get chat by id
	@GetMapping(value = CHATS_ID)
	public ResponseEntity<?> getChatById(@PathVariable Long id) {
		return new ResponseEntity<>(this.chatService.getChatById(id), ControllersHelper.getHeadersWithJsonContextType(),
				HttpStatus.OK);
	}

	// 4 - Send message
	@PostMapping(value = CHAT_ID_MESSAGES)
	public ResponseEntity<?> sendMessage(@PathVariable(value = "id") Long chatId,
			@Valid @RequestBody CreateMessageDto cmDto, HttpServletRequest request) {
		CreateMessageInfoHttpResponse responseInfo = new CreateMessageInfoHttpResponse(
				"Message has been successfully sent", request.getRequestURI(),
				this.chatService.sendMessage(chatId, cmDto).getId());
		return new ResponseEntity<>(responseInfo, ControllersHelper.getHeadersWithJsonContextType(), HttpStatus.OK);
	}

	// 5 - Get messages
	@GetMapping(value = CHAT_ID_MESSAGES)
	public ResponseEntity<?> getMessages(@PathVariable(value = "id") Long chatId,
			@PageableDefault(page = DEFAULT_PAGE_NUM, size = DEFAULT_PAGE_SIZE, sort = DEFAULT_MESSAGES_SORT, direction = Direction.DESC) Pageable pageable) {
		return new ResponseEntity<>(this.chatService.getMessagesByChatId(chatId, pageable),
				ControllersHelper.getHeadersWithJsonContextType(), HttpStatus.OK);
	}
}
