package ispp_g2.gastrostock.proveedores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    @Autowired
    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Transactional(readOnly = true)
    public List<Proveedor> findAll() {
        Iterable<Proveedor> proveedores = proveedorRepository.findAll();
        return StreamSupport.stream(proveedores.spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public Proveedor findById(String id) {
        return proveedorRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Proveedor findByEmail(String email) {
        return proveedorRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Proveedor findByTelefono(String telefono) {
        return proveedorRepository.findByTelefono(telefono);
    }

    @Transactional(readOnly = true)
    public Proveedor findByDireccion(String direccion) {
        return proveedorRepository.findByDireccion(direccion);
    }

    @Transactional(readOnly = true)
    public Proveedor findByNombre(String nombre) {
        return proveedorRepository.findByNombre(nombre);
    }

    @Transactional
    public Proveedor save(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Transactional
    public void deleteById(String id) {
        proveedorRepository.deleteById(id);
    }
}
