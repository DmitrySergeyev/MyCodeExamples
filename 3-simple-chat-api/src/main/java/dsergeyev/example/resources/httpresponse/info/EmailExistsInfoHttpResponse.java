package dsergeyev.example.resources.httpresponse.info;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "timeStamp", "status", "info", "message", "path" })
public class EmailExistsInfoHttpResponse extends StandartInfoHttpResponse {

	private String requredEmail;
	private boolean isEmailAwailable;

	public String getRequredEmail() {
		return requredEmail;
	}

	public void setRequredEmail(String requredEmail) {
		this.requredEmail = requredEmail;
	}

	public boolean isEmailAwailable() {
		return isEmailAwailable;
	}

	public void setEmailAwailable(boolean isEmailAwailable) {
		this.isEmailAwailable = isEmailAwailable;
	}
	
	public EmailExistsInfoHttpResponse(HttpStatus info, String path, String requredEmail) {
		super(info, path);
		this.requredEmail = requredEmail;
	}

	public EmailExistsInfoHttpResponse(HttpStatus info, String message, String path, String requredEmail,
			boolean isEmailAwailable) {
		super(info, message, path);
		this.requredEmail = requredEmail;
		this.isEmailAwailable = isEmailAwailable;
	}
}

	
