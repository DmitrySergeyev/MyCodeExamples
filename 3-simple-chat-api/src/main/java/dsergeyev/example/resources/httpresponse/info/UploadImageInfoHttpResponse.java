package dsergeyev.example.resources.httpresponse.info;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import dsergeyev.example.resources.httpresponse.StandardHttpResponse;

@JsonPropertyOrder({ "timeStamp", "path", "message", "imageUrl" })
public class UploadImageInfoHttpResponse extends StandardHttpResponse {

	private String imageUrl;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public UploadImageInfoHttpResponse(String message, String path, String imageUrl) {
		super(message, path);
		this.imageUrl = imageUrl;
	}	
}

