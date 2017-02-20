package dsergeyev.example.models.groups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GroupService  {
	
	@Autowired
	private GroupRepository groupRepository;
	
	public Page<Group> getAllGroups (Pageable pageable) {		
		return this.groupRepository.findAll(pageable);
	}
}
