package ispp_g2.gastrostock.Proveedores;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    @Autowired
    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public List<Proveedor> findAll() {
        List<Proveedor> proveedores = new ArrayList<>();
        proveedorRepository.findAll().forEach(proveedores::add);
        return proveedores;
    }

    public Optional<Proveedor> findById(Long id) {
        return proveedorRepository.findById(id);
    }

    public Optional<Proveedor> findByNombre(String nombre) {
        return proveedorRepository.findByNombre(nombre);
    }

    public Proveedor save(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public void deleteById(Long id) {
        proveedorRepository.deleteById(id);
    }
}
