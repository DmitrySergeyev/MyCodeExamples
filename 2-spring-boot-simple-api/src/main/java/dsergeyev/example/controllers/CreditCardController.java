package dsergeyev.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dsergeyev.example.models.cards.CreditCardService;

@RestController
public class CreditCardController {
	
	private static final int DEFAULT_PAGE_NUM = 0;
	private static final int DEFAULT_PAGE_SIZE = 20;
	private static final String DEFAULT_SORT = "id";

	@Autowired
	private CreditCardService creditCardService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/cards")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> getAllUsers(
			@PageableDefault(page = DEFAULT_PAGE_NUM, size = DEFAULT_PAGE_SIZE, sort = DEFAULT_SORT) Pageable pageable) {

		return new ResponseEntity<>(this.creditCardService.getAllCards(pageable), HttpStatus.OK);
	}
}
