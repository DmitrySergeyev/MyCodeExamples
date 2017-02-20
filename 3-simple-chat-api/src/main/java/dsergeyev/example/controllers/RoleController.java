package dsergeyev.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dsergeyev.example.ChatApplicationConfig;
import dsergeyev.example.models.roles.RoleRepository;

@RestController
public class RoleController {

	@Autowired
	private RoleRepository roleRepository;
	
	@RequestMapping(method = RequestMethod.GET, value = "/roles")
	public ResponseEntity<?> getAllRoles() {
		return new ResponseEntity<>(this.roleRepository.findAll(), HttpStatus.OK);		
	}
}
