package ispp_g2.gastrostock.lineaDeCarrito;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stripe.model.Product;

import ispp_g2.gastrostock.carrito.CarritoRepository;
import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.productoInventario.ProductoInventarioRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class LineaDeCarritoService {

    private final LineaDeCarritoRepository lineaDeCarritoRepository;
    private final CarritoRepository carritoRepository;
    private final ProductoInventarioRepository productoRepository;

    @Autowired
    public LineaDeCarritoService(LineaDeCarritoRepository lineaDeCarritoRepository,
        CarritoRepository carritoRepository, ProductoInventarioRepository productoRepository) {
        this.lineaDeCarritoRepository = lineaDeCarritoRepository;
        this.carritoRepository = carritoRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public LineaDeCarrito save(LineaDeCarrito lineaDeCarrito) {
        return lineaDeCarritoRepository.save(lineaDeCarrito);
    }

    @Transactional
    public LineaDeCarrito update(Integer id, LineaDeCarrito lineaDeCarrito) {
       LineaDeCarrito toUpdate = lineaDeCarritoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Linea de carrito no encontrada"));
       BeanUtils.copyProperties(lineaDeCarrito, toUpdate, "id");
       return lineaDeCarritoRepository.save(toUpdate); 
    }

    @Transactional
    public void delete(Integer id) {
        lineaDeCarritoRepository.deleteById(id);;
    }

    @Transactional(readOnly = true)
    public LineaDeCarrito findLineaDeCarritoById(Integer id) {
        return lineaDeCarritoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Linea de carrito no encontrada"));
    }

    @Transactional(readOnly = true)
    public List<LineaDeCarrito> findAll() {
        Iterable<LineaDeCarrito> iterable = lineaDeCarritoRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public List<LineaDeCarrito> findLineaDeCarritoByCarritoId(Integer id) {
        return lineaDeCarritoRepository.findLineaDeCarritoByCarritoId(id);
    }

    @Transactional(readOnly = true)
    public List<LineaDeCarrito> findLineaDeCarritoByCarritoIdAndProductoId(Integer idCarrito, Integer idProducto) {
        return lineaDeCarritoRepository.findLineaDeCarritoByCarritoIdAndProductoId(idCarrito, idProducto);
    }

    public LineaDeCarrito convertirLineaDeCarrito(LineaDeCarritoDTO lineaDeCarritoDTO) {
        LineaDeCarrito lineaDeCarrito = new LineaDeCarrito();
        lineaDeCarrito.setId(lineaDeCarritoDTO.getId());
        lineaDeCarrito.setCantidad(lineaDeCarritoDTO.getCantidad());
        lineaDeCarrito.setPrecioLinea(lineaDeCarritoDTO.getPrecioLinea());
        lineaDeCarrito.setProducto(productoRepository.findById(lineaDeCarritoDTO.getProductoId()).orElseThrow(()-> new ResourceNotFoundException("Producto no encontrado")));
        lineaDeCarrito.setCarrito(carritoRepository.findById(lineaDeCarritoDTO.getCarritoId()).orElseThrow(()-> new ResourceNotFoundException("Carrito no encontrado")));
        return lineaDeCarrito; 
    }
}
