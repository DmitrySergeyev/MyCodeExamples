package dsergeyev.example.resources.httpresponse.info;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "timeStamp", "path", "status", "info", "message", "newMessageId" })
public class CreateMessageInfoHttpResponse extends StandartInfoHttpResponse {

	private Long newMessageId;

	public Long getNewMessageId() {
		return newMessageId;
	}

	public void setNewMessageId(Long newUserId) {
		this.newMessageId = newUserId;
	}

	public CreateMessageInfoHttpResponse(HttpStatus info, String message, String path, Long newMessageId) {
		super(info, message, path);
		this.newMessageId = newMessageId;
	}	
}