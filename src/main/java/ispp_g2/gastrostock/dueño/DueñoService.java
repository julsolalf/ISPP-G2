package ispp_g2.gastrostock.dueño;

import java.util.List;
import java.util.Optional;

import ispp_g2.gastrostock.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.stream.StreamSupport;

@Service
public class DueñoService {

    private final DueñoRepository duenoRepository;

    @Autowired
    public DueñoService(DueñoRepository duenoRepository) {
        this.duenoRepository = duenoRepository;

    }

    @Transactional(readOnly = true)
    public Dueño getDueñoById(String id) {
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
    public Dueño getDueñoByUser(User user) {
        return duenoRepository.findDueñoByUser(user).orElse(null);
    }

    @Transactional(readOnly = true)
    public Dueño getDueñoByToken(String token) {
        return duenoRepository.findDueñoByToken(token).orElse(null);
    }

    @Transactional
    public Dueño saveDueño(Dueño dueño) {
        return duenoRepository.save(dueño);
    }

    @Transactional
    public void deleteDueño(String  id) {
        duenoRepository.deleteById(id);
    }

}
