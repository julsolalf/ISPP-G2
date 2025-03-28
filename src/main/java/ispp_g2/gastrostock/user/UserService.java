package ispp_g2.gastrostock.user;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
		return userRepository.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public User findUserByUsernameAndPassword(String username, String password) {
		return userRepository.findByUsernameAndPassword(username, password);
	}

	@Transactional(readOnly = true)
	public User findUserByAuthority(String authority) {
		return userRepository.findByAuthority(authority);
	}

	@Transactional
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Transactional
	public void deleteUser(Integer id) {
		userRepository.deleteById(id);
	}
}
