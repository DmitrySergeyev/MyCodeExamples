package dsergeyev.example.resources.httpresponse.info;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "timeStamp", "path", "status", "info", "message", "newUserId", "newUserEmail" })
public class CreatUserInfoHttpResponse extends StandartInfoHttpResponse {

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
	
	public CreatUserInfoHttpResponse(HttpStatus info, String message, String path, Long newUserId, String newUserEmail) {
		super(info, message, path);
		this.newUserId = newUserId;
		this.newUserEmail = newUserEmail;
	}	
}
