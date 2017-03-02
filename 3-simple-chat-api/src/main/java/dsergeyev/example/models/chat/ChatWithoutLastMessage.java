package dsergeyev.example.models.chat;

import java.time.ZonedDateTime;
import java.util.Set;

import dsergeyev.example.models.user.User;

public interface ChatWithoutLastMessage {

	Long getId();
	String getTitle();
	ZonedDateTime getUpdateDate();
	User getOwner();
	Set<User> getUsers();
}
