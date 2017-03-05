package dsergeyev.example.resources.httpresponse.info;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "timeStamp", "path", "status", "info", "message", "imageUrl" })
public class UploadImageInfoHttpResponse extends StandartInfoHttpResponse {

	private String imageUrl;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public UploadImageInfoHttpResponse(HttpStatus info, String message, String path, String imageUrl) {
		super(info, message, path);
		this.imageUrl = imageUrl;
	}	
}

