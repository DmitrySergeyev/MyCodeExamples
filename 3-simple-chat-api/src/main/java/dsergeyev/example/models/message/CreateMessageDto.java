package dsergeyev.example.models.message;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class CreateMessageDto {

	private Long chatId;
	private String text;

	@NotNull
	public Long getChatId() {
		return chatId;
	}

	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}

	@NotEmpty
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public CreateMessageDto() {
		super();
	}
}
