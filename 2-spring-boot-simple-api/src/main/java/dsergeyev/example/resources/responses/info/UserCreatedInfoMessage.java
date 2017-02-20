package dsergeyev.example.resources.responses.info;

public class UserCreatedInfoMessage extends StandartInfoDetails {

	private Long newUserId;

	public Long getNewUserId() {
		return newUserId;
	}

	public void setNewUserId(Long userId) {
		this.newUserId = userId;
	}
}
