package dsergeyev.example.resources.httpresponse.info;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "timeStamp", "path", "status", "info", "message", "newChatId" })
public class CreateChatInfoHttpResponse extends StandartInfoHttpResponse {

	private Long newChatId;

	public Long getNewChatId() {
		return newChatId;
	}

	public void setNewChatId(Long newUserId) {
		this.newChatId = newUserId;
	}

	public CreateChatInfoHttpResponse(HttpStatus info, String message, String path, Long newUserId) {
		super(info, message, path);
		this.newChatId = newUserId;
	}	
}
