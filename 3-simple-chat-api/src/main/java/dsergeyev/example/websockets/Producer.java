package dsergeyev.example.websockets;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {

	@Autowired
	private SimpMessagingTemplate template;

	public void sendMessageTo(String topic, String message) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(LocalDateTime.now());
		builder.append("] ");
		builder.append(message);
		this.template.convertAndSend("/topic/" + topic, builder.toString());
	}
}