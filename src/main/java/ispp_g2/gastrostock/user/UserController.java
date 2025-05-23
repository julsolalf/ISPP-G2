package ispp_g2.gastrostock.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;
	private final String admin = "admin";

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<List<User>> findAll() {
		User user = userService.findCurrentUser();
		if( !(user.getAuthority().getAuthority().equals(admin))){
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		if (userService.findAll().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> findById(@PathVariable("id") Integer id) {
		User currUser = userService.findCurrentUser();
		if( !(currUser.getAuthority().getAuthority().equals(admin))){
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		User user = userService.findUserById(id);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/username/{username}")
	public ResponseEntity<User> findByUsername(@PathVariable("username") String username) {
		User currUser = userService.findCurrentUser();
		if( !(currUser.getAuthority().getAuthority().equals(admin))){
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		User user = userService.findUserByUsername(username);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/authority/{authority}")
	public ResponseEntity<User> findByAuthority(@PathVariable("authority") String authority) {
		User currUser = userService.findCurrentUser();
		if( !(currUser.getAuthority().getAuthority().equals(admin))){
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		User user = userService.findUserByAuthority(authority);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/usernameAndPassword/{username}/{password}")
	public ResponseEntity<User> findByUsernameAndPassword(@PathVariable("username") String username,
			@PathVariable("password") String password) {
		User currUser = userService.findCurrentUser();
		if( !(currUser.getAuthority().getAuthority().equals(admin))){
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		User user = userService.findUserByUsernameAndPassword(username, password);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<User> save(@RequestBody @Valid User user) {
		User currUser = userService.findCurrentUser();
		if( !(currUser.getAuthority().getAuthority().equals(admin))){
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		if(user==null)
			throw new IllegalArgumentException("User cannot be null");
		return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> update(@PathVariable("id") Integer id, @RequestBody @Valid User user) {
		User currUser = userService.findCurrentUser();
		if( !(currUser.getAuthority().getAuthority().equals(admin))){
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		if(user==null)
			throw new IllegalArgumentException("User cannot be null");
		if(userService.findUserById(id)==null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		user.setId(id);
		return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<User> delete(@PathVariable("id") Integer id) {
		User currUser = userService.findCurrentUser();
		if( !(currUser.getAuthority().getAuthority().equals(admin))){
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		if(userService.findUserById(id)==null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		userService.deleteUser(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/me")
	public ResponseEntity<User> getCurrentUser(Authentication authentication) {
		if (authentication == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		String username = authentication.getName();

		User user = userService.findUserByUsername(username);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}


}
