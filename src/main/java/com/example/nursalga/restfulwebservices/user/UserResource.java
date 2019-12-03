package com.example.nursalga.restfulwebservices.user;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserResource {

	private static final String Resource = null;
	@Autowired
	private UserDaoService service;
	

	@GetMapping("/users")	
	public List<User> retrieveAllUsers(){
		return service.findAll(); 
	}
	

	@GetMapping("/users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable int id) {
		User user = service.findOne(id);
		if(user == null) {
			throw new UserNotFoundException("id - " + id);
		}
		
		//"all-users", SERVER_PATH + "/users"
		//retrieve all users
		EntityModel<User> model = new EntityModel<>(user);
		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		model.add(linkTo.withRel("all-users"));
	
		
		//HATEOAS
		return model;
	}
	
	//DELETE METHOD
	
	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id) {
		User user = service.deleteById(id);
		if(user == null) {
			throw new UserNotFoundException("id - " + id);
		}
		
	}
	
	//CREATE USER
	//input - details of user
	//output - CREATED & Return  the created URI
	@PostMapping("/users") 
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User createdUser = service.save(user);
		
		//CREATED
		//   /users/4
		
		URI location = ServletUriComponentsBuilder
		.fromCurrentRequest()
		.path("/{id}")
		.buildAndExpand(createdUser.getId())
		.toUri();
		return		ResponseEntity.created(location).build();
		
	}
	
	
	
}
