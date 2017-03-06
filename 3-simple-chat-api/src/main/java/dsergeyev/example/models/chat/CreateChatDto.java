package dsergeyev.example.models.chat;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

public class CreateChatDto {

	private String title;
	private List<Long> userIds;
		
	@NotEmpty
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotEmpty
	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}	
}
