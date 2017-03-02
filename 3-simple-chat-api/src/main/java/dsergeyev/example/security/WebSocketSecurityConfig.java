package dsergeyev.example.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
		messages
			// Service chat
			.simpMessageDestMatchers("/app/admin/service-chat").hasAnyRole("MODERATOR", "ADMIN")
			.simpSubscribeDestMatchers("/topic/admin/service-chat").hasAnyRole("MODERATOR", "ADMIN")
			.simpMessageDestMatchers("/app/admin/service-chat/messages/get-all").hasAnyRole("MODERATOR", "ADMIN")
			.simpSubscribeDestMatchers("/user/queue/admin/service-chat/messages/get-all").hasAnyRole("MODERATOR", "ADMIN")
			.anyMessage().authenticated();
    }
	
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
