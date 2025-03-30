package ispp_g2.gastrostock.lineaDePedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class LineaDePedidoService {

    private final LineaDePedidoRepository lineaDePedidoRepository;

    @Autowired
    public LineaDePedidoService(LineaDePedidoRepository lineaDePedidoRepository) {
        this.lineaDePedidoRepository = lineaDePedidoRepository;
    }

    @Transactional(readOnly = true)
    public LineaDePedido getById(Integer id) {
        return lineaDePedidoRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<LineaDePedido> getLineasDePedido() {
        Iterable<LineaDePedido> linea= lineaDePedidoRepository.findAll();
        return StreamSupport.stream(linea.spliterator(), false)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LineaDePedido> getLineasDePedidoByCantidad(Integer cantidad) {
        return lineaDePedidoRepository.findLineaDePedidosByCantidad(cantidad);
    }

    @Transactional(readOnly = true)
    public List<LineaDePedido> getLineasDePedidoByPrecioLinea(Double precioLinea) {
        return lineaDePedidoRepository.findLineaDePedidosByPrecioLinea(precioLinea);
    }

    @Transactional(readOnly = true)
    public List<LineaDePedido> getLineasDePedidoByPedidoId(Integer pedido) {
        return lineaDePedidoRepository.findLineaDePedidosByPedidoId(pedido);
    }

    @Transactional(readOnly = true)
    public List<LineaDePedido> getLineasDePedidoByProductoId(Integer producto) {
        return lineaDePedidoRepository.findLineaDePedidosByProductoId(producto);
    }

    @Transactional(readOnly = true)
    public List<LineaDePedido> getLineasDePedidoByProductoIdAndCantidad(Integer producto, Integer cantidad) {
        return lineaDePedidoRepository.findLineaDePedidosByProductoIdAndCantidad(producto, cantidad);
    }

    @Transactional(readOnly = true)
    public List<LineaDePedido> getLineasDePedidoByProductoIdAndPrecioLinea(Integer producto, Double precioLinea) {
        return lineaDePedidoRepository.findLineaDePedidosByProductoIdAndPrecioLinea(producto, precioLinea);
    }

    @Transactional
    public LineaDePedido save(LineaDePedido lineaDePedido) {
        return lineaDePedidoRepository.save(lineaDePedido);
    }

    @Transactional
    public void delete(Integer id) {
        lineaDePedidoRepository.deleteById(id);
    }

}
