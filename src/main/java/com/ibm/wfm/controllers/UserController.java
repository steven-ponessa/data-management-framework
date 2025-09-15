package com.ibm.wfm.controllers;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ibm.wfm.beans.User;
import com.ibm.wfm.exceptions.UserNotFoundException;
import com.ibm.wfm.services.UserDaoService;

@RestController
public class UserController {
	
	@Autowired
	private UserDaoService userDaoService;
	
	@GetMapping("/api/v1/users")
	public List<User> retrieveAllUsers() {
		return userDaoService.findAll();
	}
	
	//GET /users/{id}
	//retrieveUser(int id)
	@GetMapping("/api/v1/users/{id}")
	//public Use retrieveUser(@PathVariable int id) {
	//EntityModel for Hateoas
	public EntityModel<User> retrieveUser(@PathVariable int id) {
		User user = userDaoService.findOne(id);
		if (user==null) throw new UserNotFoundException("id: "+id+" not found.");
		
		EntityModel<User> em = EntityModel.of(user);

		// Instead of hardcoding the link to "retrieve all users" (/api/v1/users) we want the link related to this specific method.
		WebMvcLinkBuilder linkAllUsers = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUsers());
		WebMvcLinkBuilder linkDeleteUser = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).deleteUser(id));
		
		//Then we add this link with the relationship
		em.add(linkAllUsers.withRel("all-users"));
		em.add(linkDeleteUser.withRel("delete-user"));
		
		return em;
	}
	
	@PostMapping("/api/v1/users")
	public ResponseEntity createUser(@Valid @RequestBody User user) {
		User savedUser = userDaoService.save(user);
		
		//Return CREATED and URI of new object (/users/{id} saveedUser.getId()
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(savedUser.getId())
			.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	//DELETE /users/{id}
	//deleteUser(int id)
	@DeleteMapping("/api/v1/users/{id}")
	public User deleteUser(@PathVariable int id) {
		User user = userDaoService.deleteOne(id);
		if (user==null) throw new UserNotFoundException("id: "+id+" not found.");
		
		return user;
	}

}
