package ispp_g2.gastrostock.user;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    private final UserRepository userRepository;


	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;

	}
	@Transactional(readOnly = true)
	public List<User> findAll() {
		Iterable<User> users = userRepository.findAll();
		return StreamSupport.stream(users.spliterator(), false)
				.toList();
	}

	@Transactional(readOnly = true)
	public User findUserById(Integer id) {
		return userRepository.findById(id).orElse(null);
	}

	@Transactional(readOnly = true)
	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username)
			.orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
	}



	@Transactional(readOnly = true)
	public User findUserByUsernameNull(String username) {
		return userRepository.findByUsername(username)
			.orElse(null);
	}

	@Transactional(readOnly = true)
	public User findUserByUsernameAndPassword(String username, String password) {
		return userRepository.findByUsernameAndPassword(username, password);
	}

	@Transactional(readOnly = true)
	public User findUserByAuthority(String authority) {
		return userRepository.findByAuthority(authority);
	}

	@Transactional(readOnly = true)
	public User findCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null)
			throw new ResourceNotFoundException("Nobody authenticated!");
		else
			return userRepository.findUserByUsername(auth.getName())
					.orElseThrow(() -> new ResourceNotFoundException("User", "Username", auth.getName()));
	}

	public List<User> findUsersWithoutSubscription() {
        return userRepository.findUsersWithoutSubscription();
    }

	@Transactional
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Transactional
	public User updateUser(Integer id, User user) {
		User toUpdate = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
		BeanUtils.copyProperties(user, toUpdate, "id", "authority");
		return userRepository.save(toUpdate);
	}

	@Transactional
	public void deleteUser(Integer id) {
		userRepository.deleteById(id);
	}
}
