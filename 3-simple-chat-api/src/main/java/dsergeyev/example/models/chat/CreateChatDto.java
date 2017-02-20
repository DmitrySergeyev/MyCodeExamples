package dsergeyev.example.models.chat;

import java.util.Set;

import org.hibernate.validator.constraints.NotEmpty;

public class CreateChatDto {

	private String title;
	private Set<Long> userIds;
		
	@NotEmpty
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotEmpty
	public Set<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(Set<Long> userIds) {
		this.userIds = userIds;
	}	
}
