package dsergeyev.example.resources.httpresponse.info;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import dsergeyev.example.resources.httpresponse.StandardHttpResponse;

@JsonPropertyOrder({ "timestamp", "path", "message", "newChatId" })
public class CreateChatInfoHttpResponse extends StandardHttpResponse {

	private Long newChatId;

	public Long getNewChatId() {
		return newChatId;
	}

	public void setNewChatId(Long newUserId) {
		this.newChatId = newUserId;
	}

	public CreateChatInfoHttpResponse(String message, String path, Long newUserId) {
		super(message, path);
		this.newChatId = newUserId;
	}	
}
