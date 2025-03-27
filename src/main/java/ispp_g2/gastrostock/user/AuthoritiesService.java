package ispp_g2.gastrostock.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class AuthoritiesService {

    private final AuthoritiesRepository authoritiesRepository;

	@Autowired
	public AuthoritiesService(AuthoritiesRepository authoritiesRepository) {
		this.authoritiesRepository = authoritiesRepository;
	}

	@Transactional(readOnly = true)
	public Authorities findByAuthority(String authority) {
		return authoritiesRepository.findByAuthority(authority);
	}

	@Transactional(readOnly = true)
	public Authorities findById(String id) {
		return authoritiesRepository.findById(id).orElse(null);
	}

	@Transactional(readOnly = true)
	public List<Authorities> findAll() {
		Iterable<Authorities> authorities = authoritiesRepository.findAll();
		return StreamSupport.stream(authorities.spliterator(), false)
				.toList();
	}

	@Transactional
	public Authorities saveAuthorities(Authorities authorities) {
		return authoritiesRepository.save(authorities);
	}

	@Transactional
	public void delete(String id){
		authoritiesRepository.deleteById(id);
	}

}
