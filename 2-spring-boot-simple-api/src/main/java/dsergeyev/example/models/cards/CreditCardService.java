package dsergeyev.example.models.cards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CreditCardService {

	@Autowired
	private CreditCardRepository creditCardRepository;
	
	public Page<CreditCard> getAllCards (Pageable pageable) {		
		return this.creditCardRepository.findAll(pageable);
	}
}
