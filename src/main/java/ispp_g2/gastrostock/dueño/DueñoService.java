package ispp_g2.gastrostock.dueño;

import java.util.List;

import ispp_g2.gastrostock.user.AuthoritiesRepository;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.stream.StreamSupport;

@Service
public class DueñoService {

    private final DueñoRepository duenoRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final UserRepository userRepository;

    @Autowired
    public DueñoService(DueñoRepository duenoRepository, AuthoritiesRepository authoritiesRepository, UserRepository userRepository) {
        this.duenoRepository = duenoRepository;
        this.authoritiesRepository = authoritiesRepository;
        this.userRepository = userRepository;

    }

    @Transactional(readOnly = true)
    public Dueño getDueñoById(Integer id) {
        return duenoRepository.findById(id).orElse(null);
    }


    @Transactional(readOnly = true)
    public List<Dueño> getAllDueños() {
        Iterable<Dueño> dueños = duenoRepository.findAll();
        return StreamSupport.stream(dueños.spliterator(), false)
                .toList();
    }


    @Transactional(readOnly = true)
    public Dueño getDueñoByEmail(String email) {
        return duenoRepository.findDueñoByEmail(email).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Dueño> getDueñoByNombre(String nombre) {
        return duenoRepository.findDueñoByNombre(nombre);
    }

    @Transactional(readOnly = true)
    public List<Dueño> getDueñoByApellido(String apellido) {
        return duenoRepository.findDueñoByApellido(apellido);
    }

    @Transactional(readOnly = true)
    public Dueño getDueñoByTelefono(String telefono) {
        return duenoRepository.findDueñoByTelefono(telefono).orElse(null);
    }

    @Transactional(readOnly = true)
    public Dueño getDueñoByUser(Integer userId) {
        return duenoRepository.findDueñoByUser(userId).orElse(null);
    }

    @Transactional(readOnly = true)
    public Dueño getDueñoByToken(String token) {
        return duenoRepository.findDueñoByToken(token).orElse(null);
    }


    @Transactional
    public Dueño saveDueño(Dueño dueño) {
        userRepository.save(dueño.getUser());
        return duenoRepository.save(dueño);
    }

    @Transactional
    public void deleteDueño(Integer  id) {
        duenoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Dueño convertirDTODueño(DueñoDTO dueñoDTO) {
        Dueño dueño = new Dueño();
        User user = new User();
        user.setUsername(dueñoDTO.getUsername());
        user.setPassword(dueñoDTO.getPassword());
        user.setAuthority(authoritiesRepository.findByAuthority("dueño"));
        dueño.setFirstName(dueñoDTO.getFirstName());
        dueño.setLastName(dueñoDTO.getLastName());
        dueño.setEmail(dueñoDTO.getEmail());
        dueño.setNumTelefono(dueñoDTO.getNumTelefono());
        dueño.setTokenDueño(dueñoDTO.getTokenDueño());
        dueño.setUser(user);
        return dueño;
    }

    @Transactional(readOnly = true)
    public DueñoDTO convertirDueñoDTO(Dueño dueño) {
        DueñoDTO dueñoDTO = new DueñoDTO();
        dueñoDTO.setUsername(dueño.getUser().getUsername());
        dueñoDTO.setPassword(dueño.getUser().getPassword());
        dueñoDTO.setFirstName(dueño.getFirstName());
        dueñoDTO.setLastName(dueño.getLastName());
        dueñoDTO.setEmail(dueño.getEmail());
        dueñoDTO.setNumTelefono(dueño.getNumTelefono());
        dueñoDTO.setTokenDueño(dueño.getTokenDueño());
        return dueñoDTO;
    }

}
