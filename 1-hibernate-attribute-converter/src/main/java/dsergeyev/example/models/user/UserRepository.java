package dsergeyev.example.models.user;

import org.springframework.data.repository.CrudRepository;

interface UserRepository extends CrudRepository<User, Long>{

}
