package dsergeyev.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dsergeyev.example.models.user.User;
import dsergeyev.example.models.user.UserService;

@Controller
public class WebSocketController {

	@Autowired
	private SimpMessagingTemplate template;
	@Autowired
	private UserService userService;
	@Autowired
	
//	@MessageMapping("/topic/system-chat")
//	@SendTo("/topic/system-chat")
//	public SystemMessage getMessage2(@RequestParam String text) {
//		User user = this.userService.getAuthorisatedUser();
//		SystemMessage message = new SystemMessage(text, user);
//		this.systemChatMessageRepository.save(message);		
//		return message;
//	}
	
	public String getMessage(String text) {
		User user = this.userService.getAuthorisatedUser();
		this.systemChatMessageRepository.save(message);		
		return String.format("[%s] %s %s (%s): %s", message.getTime().toLocalTime().toString(), user.getFirstName(), user.getSecondName(), user.getRole().getName(), text);
	}
	
	public String sender(@RequestParam String message) {
		this.template.convertAndSend(message);
		return "OK-Sent";
	}
}
