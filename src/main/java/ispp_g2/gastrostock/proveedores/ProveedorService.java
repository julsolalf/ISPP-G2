package ispp_g2.gastrostock.proveedores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    @Autowired
    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Transactional(readOnly = true)
    public List<Proveedor> getAll() {
        return proveedorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Proveedor> getById(Integer id) {
        return proveedorRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Proveedor> getByFirstName(String firstName) {
        return proveedorRepository.findByFirstNameContainingIgnoreCase(firstName);
    }

    @Transactional(readOnly = true)
    public List<Proveedor> getByDiaReparto(DiaSemana diaSemana) {
        return proveedorRepository.findByDiasRepartoContaining(diaSemana);
    }

    @Transactional
    public Proveedor save(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Transactional
    public void deleteById(Integer id) {
        proveedorRepository.deleteById(id);
    }
}
