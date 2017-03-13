package dsergeyev.example.resources.httpresponse.info;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import dsergeyev.example.resources.httpresponse.StandardHttpResponse;

@JsonPropertyOrder({ "timestamp", "path", "message", "requredEmail", "isEmailAwailable" })
public class EmailExistsInfoHttpResponse extends StandardHttpResponse {

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
	
	public EmailExistsInfoHttpResponse(String path, String requredEmail) {
		super(path);
		this.requredEmail = requredEmail;
	}

	public EmailExistsInfoHttpResponse(String message, String path, String requredEmail,
			boolean isEmailAwailable) {
		super(message, path);
		this.requredEmail = requredEmail;
		this.isEmailAwailable = isEmailAwailable;
	}
}

	
