package dsergeyev.example.controllers.websockets;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import dsergeyev.example.ChatApplicationConfig;
import dsergeyev.example.models.message.ServiceMessage;
import dsergeyev.example.models.message.ServiceMessageRepository;
import dsergeyev.example.models.user.User;
import dsergeyev.example.models.user.UserService;

@Controller
public class AdminWebSocketController {
	
	@Autowired
	private ServiceMessageRepository serviceMessageRepository;
	@Autowired
	private UserService userService;
	
	// 1 - Send message to service chat (only for moderators and administrators)
	@MessageMapping("/admin/service-chat")
	public ServiceMessage sendMessage(@NotNull String text) {
		User user = this.userService.getAuthorisatedUser();
		ServiceMessage message = new ServiceMessage(text, user);		
		this.serviceMessageRepository.save(message);		
		return message;
	}
	
	// 2 Get all messages from service chat
	// JavaScript request example: stompClient.send("/app/admin/service-chat/messages/get-all", {}, {});
	@SubscribeMapping("/admin/service-chat/messages/get-all")
	@SendToUser("/queue/admin/service-chat/messages/get-all")
	public List<ServiceMessage> getMessage() {	
		return this.serviceMessageRepository.findAll();
	}
}
