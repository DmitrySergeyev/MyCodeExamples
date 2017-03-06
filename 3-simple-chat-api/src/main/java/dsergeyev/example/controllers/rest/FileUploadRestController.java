package dsergeyev.example.controllers.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dsergeyev.example.ChatApplicationConfig;
import dsergeyev.example.resources.errorhanding.exception.ResourceNotFoundException;
import dsergeyev.example.resources.httpresponse.StandardHttpResponse;
import dsergeyev.example.resources.httpresponse.error.StandartErrorHttpResponse;
import dsergeyev.example.resources.httpresponse.info.UploadImageInfoHttpResponse;

@RestController
public class FileUploadRestController {

	private static String UPLOADED_FOLDER = "C:\\Users\\Dmitry\\MyProjects\\Git\\MyCodeExamples\\3-simple-chat-api\\download\\images\\";

	public static final String FILES = ChatApplicationConfig.API_VERSION_PREFIX + "/files";
	public static final String FILES_IMAGES = FILES + "/images";
	public static final String FILES_IMAGES_id = FILES_IMAGES + "?id=";

	private static String getFileExtension(String fileName) {
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		} else {
			return "";
		}
	}

	@PostMapping(FILES_IMAGES)
	public ResponseEntity<?> singleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

		if (file.isEmpty()) {
			StandardHttpResponse errorDetail = new StandardHttpResponse("Error! Image file can't be empty!",
					request.getRequestURI());
			return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
		}

		if (!file.getContentType().equals(MediaType.IMAGE_PNG_VALUE)
				&& !file.getContentType().equals(MediaType.IMAGE_JPEG_VALUE)) {
			StandardHttpResponse errorDetail = new StandardHttpResponse(
					"Error! Image file must have '.jpeg' or '.png' extension!", request.getRequestURI());
			return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
		}

		String newImageName = UUID.randomUUID().toString() + "." + getFileExtension(file.getOriginalFilename());

		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + newImageName);
			Files.write(path, bytes);

		} catch (IOException ex) {
			StandartErrorHttpResponse errorDetail = new StandartErrorHttpResponse(ex.getClass().getName(),
					"Error! File upload faled", request.getRequestURI());
			return new ResponseEntity<>(errorDetail, null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		UploadImageInfoHttpResponse responseInfo = new UploadImageInfoHttpResponse("File has been uploaded!",
				request.getRequestURI(), FILES_IMAGES_id + newImageName);
		return new ResponseEntity<>(responseInfo, null, HttpStatus.OK);
	}

	@GetMapping(FILES_IMAGES)
	public ResponseEntity<?> getImage(@PathParam(value = "id") String id, HttpServletRequest request) {

		Path path = Paths.get(UPLOADED_FOLDER + "//" + id);
		byte[] data;

		try {
			data = Files.readAllBytes(path);
		} catch (IOException ex) {
			throw new ResourceNotFoundException("Image file not found");
		}

		HttpHeaders headers = new HttpHeaders();

		if (path.getFileName().toString().endsWith(".jpeg") || path.getFileName().toString().endsWith(".jpg")) {
			headers.setContentType(MediaType.IMAGE_JPEG);
		} else if (path.getFileName().toString().endsWith(".png")) {
			headers.setContentType(MediaType.IMAGE_PNG);
		} else {
			throw new ResourceNotFoundException("Image file not found");
		}

		return new ResponseEntity<>(data, headers, HttpStatus.OK);
	}
}
