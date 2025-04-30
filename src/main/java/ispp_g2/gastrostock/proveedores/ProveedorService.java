package ispp_g2.gastrostock.proveedores;

import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.negocio.NegocioRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final NegocioRepository negocioRepository;

    @Autowired
    public ProveedorService(ProveedorRepository proveedorRepository, NegocioRepository negocioRepository) {
        this.proveedorRepository = proveedorRepository;
        this.negocioRepository = negocioRepository;
    }

    @Transactional(readOnly = true)
    public List<Proveedor> findAll() {
        Iterable<Proveedor> proveedores = proveedorRepository.findAll();
        return StreamSupport.stream(proveedores.spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public Proveedor findById(Integer id) {
        return proveedorRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Proveedor no encontrado"));
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

    @Transactional(readOnly = true)
    public List<Proveedor> findProveedorByNegocioId(Integer negocio) {
        return proveedorRepository.findProveedorByNegocioId(negocio);
    }

    @Transactional(readOnly = true)
    public List<Proveedor> findProveedorByDuenoId(Integer dueno) {
        return proveedorRepository.findProveedorByDuenoId(dueno);
    }

    @Transactional
    public Proveedor save(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Transactional
    public Proveedor update(Integer id, Proveedor proveedor) {
        Proveedor toUpdate = proveedorRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Proveedor no encontrado"));
        BeanUtils.copyProperties(proveedor, toUpdate,"id");
        return proveedorRepository.save(toUpdate);
    }

    @Transactional
    public void deleteById(Integer id) {
        proveedorRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Proveedor convertirDTOProveedor(ProveedorDTO proveedorDTO) {
        Proveedor proveedor = new Proveedor();
        proveedor.setName(proveedorDTO.getName());
        proveedor.setEmail(proveedorDTO.getEmail());
        proveedor.setTelefono(proveedorDTO.getTelefono());
        proveedor.setDireccion(proveedorDTO.getDireccion());
        proveedor.setNegocio(negocioRepository.findById(proveedorDTO.getNegocioId())
            .orElseThrow(()-> new ResourceNotFoundException("Negocio no encontrado")));
        return proveedor;
    }

    @Transactional(readOnly = true)
    public ProveedorDTO convertirProveedorDTO(Proveedor proveedor) {
        ProveedorDTO proveedorDTO = new ProveedorDTO();
        proveedorDTO.setName(proveedor.getName());
        proveedorDTO.setEmail(proveedor.getEmail());
        proveedorDTO.setTelefono(proveedor.getTelefono());
        proveedorDTO.setDireccion(proveedor.getDireccion());
        proveedorDTO.setNegocioId(proveedor.getNegocio().getId());
        return proveedorDTO;
    }
}
