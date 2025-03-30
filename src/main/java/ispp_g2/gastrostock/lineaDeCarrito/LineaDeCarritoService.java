package ispp_g2.gastrostock.lineaDeCarrito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class LineaDeCarritoService {

    private final LineaDeCarritoRepository lineaDeCarritoRepository;

    @Autowired
    public LineaDeCarritoService(LineaDeCarritoRepository lineaDeCarritoRepository) {
        this.lineaDeCarritoRepository = lineaDeCarritoRepository;
    }

    @Transactional
    public LineaDeCarrito save(LineaDeCarrito lineaDeCarrito) {
        return lineaDeCarritoRepository.save(lineaDeCarrito);
    }

    @Transactional
    public void delete(LineaDeCarrito lineaDeCarrito) {
        lineaDeCarritoRepository.delete(lineaDeCarrito);
    }

    @Transactional(readOnly = true)
    public LineaDeCarrito findLineaDeCarritoById(Integer id) {
        return lineaDeCarritoRepository.findById(id).orElse(null);
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
}
