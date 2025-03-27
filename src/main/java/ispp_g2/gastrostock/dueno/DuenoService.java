package ispp_g2.gastrostock.dueno;

import java.util.List;
import java.util.Optional;

import ispp_g2.gastrostock.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.stream.StreamSupport;

@Service
public class DuenoService {

    private final DuenoRepository duenoRepository;

    @Autowired
    public DuenoService(DuenoRepository duenoRepository) {
        this.duenoRepository = duenoRepository;

    }

    @Transactional(readOnly = true)
    public Dueno getDuenoById(String id) {
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
    public Dueno getDuenoByUser(String userId) {
        return duenoRepository.findDuenoByUser(userId).orElse(null);
    }

    @Transactional(readOnly = true)
    public Dueno getDuenoByToken(String token) {
        return duenoRepository.findDuenoByToken(token).orElse(null);
    }


    @Transactional
    public Dueno saveDueno(Dueno dueno) {
        return duenoRepository.save(dueno);
    }

    @Transactional
    public void deleteDueno(String  id) {
        duenoRepository.deleteById(id);
    }

}
