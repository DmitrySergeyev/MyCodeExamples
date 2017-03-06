package dsergeyev.example.resources.httpresponse.info;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import dsergeyev.example.resources.httpresponse.StandardHttpResponse;

@JsonPropertyOrder({ "timeStamp", "path", "message", "newUserId", "newUserEmail" })
public class CreatUserInfoHttpResponse extends StandardHttpResponse {

	private Long newUserId;
	private String newUserEmail;

	public Long getNewUserId() {
		return newUserId;
	}

	public void setNewUserId(Long newUserId) {
		this.newUserId = newUserId;
	}

	public String getNewUserEmail() {
		return newUserEmail;
	}

	public void setNewUserEmail(String newUserEmail) {
		this.newUserEmail = newUserEmail;
	}
	
	public CreatUserInfoHttpResponse(String message, String path, Long newUserId, String newUserEmail) {
		super(message, path);
		this.newUserId = newUserId;
		this.newUserEmail = newUserEmail;
	}	
}
