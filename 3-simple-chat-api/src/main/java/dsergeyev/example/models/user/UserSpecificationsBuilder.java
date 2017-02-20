package dsergeyev.example.models.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

public class UserSpecificationsBuilder {
    
    private final List<UserSearchCriteria> params;
 
    public UserSpecificationsBuilder() {
        params = new ArrayList<UserSearchCriteria>();
    }
 
    public UserSpecificationsBuilder with(String key, String operation, Object value) {
        params.add(new UserSearchCriteria(key, operation, value));
        return this;
    }
 
    public Specification<User> build() {
        if (params.size() == 0) {
            return null;
        }
 
        List<Specification<User>> specs = new ArrayList<Specification<User>>();
        for (UserSearchCriteria param : params) {
            specs.add(new UserSpecification(param));
        }
 
        Specification<User> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specifications.where(result).and(specs.get(i));
        }
        
        return result;
    }
}
