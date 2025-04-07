package ispp_g2.gastrostock.lineaDePedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.pedido.PedidoRepository;
import ispp_g2.gastrostock.productoVenta.ProductoVentaRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class LineaDePedidoService {

    private final LineaDePedidoRepository lineaDePedidoRepository;
    private final ProductoVentaRepository productoVentaRepository;
    private final PedidoRepository pedidoRepository;

    @Autowired
    public LineaDePedidoService(LineaDePedidoRepository lineaDePedidoRepository, ProductoVentaRepository productoVentaRepository, PedidoRepository pedidoRepository) {
        this.lineaDePedidoRepository = lineaDePedidoRepository;
        this.productoVentaRepository = productoVentaRepository;
        this.pedidoRepository = pedidoRepository;
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
        return lineaDePedidoRepository.findLineaDePedidosByPrecioUnitario(precioLinea);
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
        return lineaDePedidoRepository.findLineaDePedidosByProductoIdAndPrecioUnitario(producto, precioLinea);
    }

    @Transactional
    public LineaDePedido save(LineaDePedido lineaDePedido) {
        return lineaDePedidoRepository.save(lineaDePedido);
    }

    @Transactional
    public void delete(Integer id) {
        lineaDePedidoRepository.deleteById(id);
    }

    public LineaDePedido convertDtoLineaDePedido(LineaDePedidoDTO lineaDePedidoDTO) {
        LineaDePedido lineaDePedido = new LineaDePedido();
        lineaDePedido.setCantidad(lineaDePedidoDTO.getCantidad());
        lineaDePedido.setPrecioUnitario(lineaDePedidoDTO.getPrecioUnitario());
        lineaDePedido.setPedido(pedidoRepository.findById(lineaDePedidoDTO.getPedidoId()).orElseThrow(() -> new ResourceNotFoundException("El pedido no existe")));
        lineaDePedido.setProducto(productoVentaRepository.findProductoVentaByNombreAndNegocioId(lineaDePedidoDTO.getNombreProducto(),
        lineaDePedido.getPedido().getMesa().getNegocio().getId()).orElseThrow(() -> new ResourceNotFoundException("El producto no existe")));
        return lineaDePedido;
    }

}
