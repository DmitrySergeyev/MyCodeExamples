package dsergeyev.example.resources.httpresponse.info;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import dsergeyev.example.resources.httpresponse.StandardHttpResponse;

@JsonPropertyOrder({ "timestamp", "path", "message", "newUserId", "newUserEmail" })
public class UserOperationsInfoHttpResponse extends StandardHttpResponse {

	private Long userId;
	private String userEmail;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long newUserId) {
		this.userId = newUserId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String newUserEmail) {
		this.userEmail = newUserEmail;
	}
	
	public UserOperationsInfoHttpResponse(String message, String path, Long userId, String userEmail) {
		super(message, path);
		this.userId = userId;
		this.userEmail = userEmail;
	}	
}
