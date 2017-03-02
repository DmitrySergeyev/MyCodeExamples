package dsergeyev.example.models.message;

import org.hibernate.validator.constraints.NotEmpty;

public class CreateMessageDto {

	private String text;

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
