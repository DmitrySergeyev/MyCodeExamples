package dsergeyev.example.resources.httpresponse.info;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import dsergeyev.example.resources.httpresponse.StandardHttpResponse;

@JsonPropertyOrder({ "timeStamp", "path", "message", "newMessageId" })
public class CreateMessageInfoHttpResponse extends StandardHttpResponse {

	private Long newMessageId;

	public Long getNewMessageId() {
		return newMessageId;
	}

	public void setNewMessageId(Long newUserId) {
		this.newMessageId = newUserId;
	}

	public CreateMessageInfoHttpResponse(String message, String path, Long newMessageId) {
		super(message, path);
		this.newMessageId = newMessageId;
	}	
}