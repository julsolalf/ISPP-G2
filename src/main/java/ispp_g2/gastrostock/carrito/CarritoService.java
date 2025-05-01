package ispp_g2.gastrostock.carrito;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.proveedores.ProveedorRepository;

import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ProveedorRepository proveedorRepository;

    @Autowired
    public CarritoService(CarritoRepository carritoRepository, ProveedorRepository proveedorRepository) {
        this.carritoRepository = carritoRepository;
        this.proveedorRepository = proveedorRepository;
    }

    @Transactional
    public Carrito save(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito update(Integer id, Carrito carrito) {
        Carrito carritoToUpdate = carritoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El carrito no existe"));
        BeanUtils.copyProperties(carrito, carritoToUpdate, "id");
        return carritoRepository.save(carritoToUpdate);
    }

    @Transactional
    public void deleteById(Integer id) {
        carritoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Carrito findById(Integer id) {
        return carritoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El carrito no existe"));
    }

    @Transactional(readOnly = true)
    public List<Carrito> findAll() {
        Iterable<Carrito> iterator = carritoRepository.findAll();
        return StreamSupport.stream(iterator.spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public List<Carrito> findByProveedorId(Integer id) {
        return carritoRepository.findByProveedorId(id);
    }

    @Transactional(readOnly = true)
    public List<Carrito> findByNegocioId(Integer id) {
        return carritoRepository.findByNegocioId(id);
    }

    public Carrito convertirCarrito(CarritoDTO carritoDTO) {
        Carrito carrito = new Carrito();
        carrito.setId(carritoDTO.getId());
        carrito.setPrecioTotal(carritoDTO.getPrecioTotal());
        carrito.setDiaEntrega(carritoDTO.getFechaEntrega());
        carrito.setProveedor(proveedorRepository.findById(carritoDTO.getProveedorId()).orElseThrow(() -> new ResourceNotFoundException("El proveedor no existe")));
        return carrito;
    }

}
