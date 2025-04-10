package ispp_g2.gastrostock.carrito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;

    @Autowired
    public CarritoService(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    @Transactional
    public Carrito save(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    @Transactional
    public void deleteById(Integer id) {
        carritoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Carrito findById(Integer id) {
        return carritoRepository.findById(id).orElse(null);
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

}
