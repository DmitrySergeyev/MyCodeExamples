package dsergeyev.example.models.cards;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CreditCardRepository extends PagingAndSortingRepository<CreditCard, Long> {

}
