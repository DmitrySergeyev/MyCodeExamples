package dsergeyev.example.websockets;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
	
//	private ObjectMapper mapper;
	
//	@Autowired
//	public void setMapper(ObjectMapper mapper) {
//		this.mapper = mapper;
//	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").setHandshakeHandler(new DefaultHandshakeHandler()).withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.setUserDestinationPrefix("/user")
				.setApplicationDestinationPrefixes("/app")
				.enableSimpleBroker("/topic", "/queue");
	}
	
//	@Override
//	public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
//		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//		converter.setObjectMapper(mapper);
//		messageConverters.add(converter);
//		return false;
//	}
}
 