package dsergeyev.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dsergeyev.example.ChatApplicationConfig;
import dsergeyev.example.models.message.ServiceMessageRepository;

@RestController
public class AdminController {

	@Autowired
	private ServiceMessageRepository systemChatMessageRepository;
	
	private static final int DEFAULT_PAGE_NUM = 0;
	private static final int DEFAULT_PAGE_SIZE = 20;
	private static final String DEFAULT_SORT = "id";

	@RequestMapping(method = RequestMethod.GET, value = ChatApplicationConfig.ADMIN_CHAT)
	public ResponseEntity<?> getAllSystemMessages(@PageableDefault(page = DEFAULT_PAGE_NUM, size = DEFAULT_PAGE_SIZE, sort = DEFAULT_SORT) Pageable pageable) {
		return new ResponseEntity<>(this.systemChatMessageRepository.findAll(pageable), HttpStatus.OK);
	}
}
