package dsergeyev.example.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
		messages
			.simpMessageDestMatchers("/app/system-chat").hasAnyRole("MODERATOR", "ADMIN")
			.simpSubscribeDestMatchers("/topic/system-chat").hasAnyRole("MODERATOR", "ADMIN")
			.anyMessage().authenticated();
    }
	
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
