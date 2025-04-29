package ispp_g2.gastrostock.dueno;

import java.util.List;

import ispp_g2.gastrostock.exceptions.DuenoSaveException;
import ispp_g2.gastrostock.user.AuthoritiesRepository;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.stream.StreamSupport;

@Service
public class DuenoService {

    private final DuenoRepository duenoRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final UserRepository userRepository;

    @Autowired
    public DuenoService(DuenoRepository duenoRepository, AuthoritiesRepository authoritiesRepository, UserRepository userRepository) {
        this.duenoRepository = duenoRepository;
        this.authoritiesRepository = authoritiesRepository;
        this.userRepository = userRepository;

    }

    @Transactional(readOnly = true)
    public Dueno getDuenoById(Integer id) {
        return duenoRepository.findById(id).orElse(null);
    }


    @Transactional(readOnly = true)
    public List<Dueno> getAllDuenos() {
        Iterable<Dueno> duenos = duenoRepository.findAll();
        return StreamSupport.stream(duenos.spliterator(), false)
                .toList();
    }


    @Transactional(readOnly = true)
    public Dueno getDuenoByEmail(String email) {
        return duenoRepository.findDuenoByEmail(email).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Dueno> getDuenoByNombre(String nombre) {
        return duenoRepository.findDuenoByNombre(nombre);
    }

    @Transactional(readOnly = true)
    public List<Dueno> getDuenoByApellido(String apellido) {
        return duenoRepository.findDuenoByApellido(apellido);
    }

    @Transactional(readOnly = true)
    public Dueno getDuenoByTelefono(String telefono) {
        return duenoRepository.findDuenoByTelefono(telefono).orElse(null);
    }

    @Transactional(readOnly = true)
    public Dueno getDuenoByUser(Integer userId) {
        return duenoRepository.findDuenoByUser(userId).orElse(null);
    }

    @Transactional(readOnly = true)
    public Dueno getDuenoByToken(String token) {
        return duenoRepository.findDuenoByToken(token).orElse(null);
    }


    @Transactional
    public Dueno saveDueno(Dueno dueno) {
        if (dueno == null) {
            throw new IllegalArgumentException("El dueño no puede ser nulo");
        }
        if (dueno.getUser() == null) {
            throw new IllegalArgumentException("El dueño debe tener un usuario asociado");
        }
        try {
            User savedUser = userRepository.save(dueno.getUser());
            dueno.setUser(savedUser);
            return duenoRepository.save(dueno);
        } catch (Exception e) {
            throw new DuenoSaveException("Error al guardar el dueño: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Dueno updateDueno(Integer id, Dueno dueno) {
      Dueno toUpdate = duenoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("El dueño con id " + id + " no existe"));
      BeanUtils.copyProperties(dueno, toUpdate, "id", "user", "tokenDueno");
      return duenoRepository.save(toUpdate);
    }

    @Transactional
    public void deleteDueno(Integer  id) {
        duenoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Dueno convertirDTODueno(DuenoDTO duenoDTO) {
        Dueno dueno = new Dueno();
        User user = new User();
        user.setUsername(duenoDTO.getUsername());
        user.setPassword(duenoDTO.getPassword());
        user.setAuthority(authoritiesRepository.findByAuthority("dueno"));
        dueno.setFirstName(duenoDTO.getFirstName());
        dueno.setLastName(duenoDTO.getLastName());
        dueno.setEmail(duenoDTO.getEmail());
        dueno.setNumTelefono(duenoDTO.getNumTelefono());
        dueno.setTokenDueno(generarToken());
        dueno.setUser(user);
        return dueno;
    }

    @Transactional(readOnly = true)
    public DuenoDTO convertirDuenoDTO(Dueno dueno) {
        DuenoDTO duenoDTO = new DuenoDTO();
        duenoDTO.setUsername(dueno.getUser().getUsername());
        duenoDTO.setPassword(dueno.getUser().getPassword());
        duenoDTO.setFirstName(dueno.getFirstName());
        duenoDTO.setLastName(dueno.getLastName());
        duenoDTO.setEmail(dueno.getEmail());
        duenoDTO.setNumTelefono(dueno.getNumTelefono());
        duenoDTO.setTokenDueno(dueno.getTokenDueno());
        return duenoDTO;
    }

    private String generarToken() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Integer l = 30;
        Random r = new Random();

        StringBuilder sb = new StringBuilder(l);
        for (int i = 0; i < l; i++) {
            int index = r.nextInt(caracteres.length());
            sb.append(caracteres.charAt(index));
        }

        return "gst-" + sb.toString();
    }

}
