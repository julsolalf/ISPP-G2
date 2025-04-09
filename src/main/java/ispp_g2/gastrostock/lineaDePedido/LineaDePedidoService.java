package ispp_g2.gastrostock.lineaDePedido;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.ingrediente.Ingrediente;
import ispp_g2.gastrostock.ingrediente.IngredienteRepository;
import ispp_g2.gastrostock.lote.Lote;
import ispp_g2.gastrostock.lote.LoteRepository;
import ispp_g2.gastrostock.lote.LoteService;
import ispp_g2.gastrostock.pedido.PedidoRepository;
import ispp_g2.gastrostock.productoInventario.ProductoInventario;
import ispp_g2.gastrostock.productoVenta.ProductoVentaRepository;
import jakarta.persistence.criteria.CriteriaBuilder.In;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class LineaDePedidoService {

    private final LineaDePedidoRepository lineaDePedidoRepository;
    private final ProductoVentaRepository productoVentaRepository;
    private final PedidoRepository pedidoRepository;
    private final IngredienteRepository ingredienteRepository;
    private final LoteRepository loteRepository;

    @Autowired
    public LineaDePedidoService(LineaDePedidoRepository lineaDePedidoRepository,
            ProductoVentaRepository productoVentaRepository, PedidoRepository pedidoRepository,
            IngredienteRepository ingredienteRepository, LoteRepository loteRepository) {
        this.lineaDePedidoRepository = lineaDePedidoRepository;
        this.productoVentaRepository = productoVentaRepository;
        this.pedidoRepository = pedidoRepository;
        this.ingredienteRepository = ingredienteRepository;
        this.loteRepository = loteRepository;
    }

    @Transactional(readOnly = true)
    public LineaDePedido getById(Integer id) {
        return lineaDePedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La linea de pedido no existe"));
    }

    @Transactional(readOnly = true)
    public List<LineaDePedido> getLineasDePedido() {
        Iterable<LineaDePedido> linea = lineaDePedidoRepository.findAll();
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
    public List<LineaDePedido> getLineasDePedidoByProductoIdAndPrecioUnitario(Integer producto, Double precioLinea) {
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

    @Transactional
    public LineaDePedido update(Integer id, LineaDePedido lineaDePedido) {
        LineaDePedido toUpdate = lineaDePedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La linea de pedido no existe"));
        BeanUtils.copyProperties(lineaDePedido, toUpdate, "id", "estado");
        return lineaDePedidoRepository.save(toUpdate);
    }

    @Transactional
    public LineaDePedido cambiarEstado(Integer id) {
        LineaDePedido lineaDePedido = lineaDePedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La linea de pedido no existe"));
        if (lineaDePedido.getEstado().equals(true)) {
            return lineaDePedido;
        } else {
            lineaDePedido.setEstado(true);
            List<Ingrediente> ingredientes = ingredienteRepository
                    .findByProductoVentaId(lineaDePedido.getProducto().getId());
            for (Ingrediente ingrediente : ingredientes) {
                ProductoInventario productoInventario = ingrediente.getProductoInventario();
                List<Lote> lotes = loteRepository.findByProductoId(productoInventario.getId());
                if (lotes.get(0).getCantidad() < ingrediente.getCantidad() * lineaDePedido.getCantidad()) {
                    lotes.get(1).setCantidad(lotes.get(1).getCantidad()
                            - (ingrediente.getCantidad() * lineaDePedido.getCantidad() - lotes.get(0).getCantidad()));
                    lotes.get(0).setCantidad(0);
                } else {
                    lotes.get(0).setCantidad(
                            lotes.get(0).getCantidad() - ingrediente.getCantidad() * lineaDePedido.getCantidad());
                }
                loteRepository.saveAll(lotes);
            }
            return lineaDePedidoRepository.save(lineaDePedido);
        }
    }

    public LineaDePedido convertDtoLineaDePedido(LineaDePedidoDTO lineaDePedidoDTO) {
        LineaDePedido lineaDePedido = new LineaDePedido();
        lineaDePedido.setCantidad(lineaDePedidoDTO.getCantidad());
        lineaDePedido.setPrecioUnitario(lineaDePedidoDTO.getPrecioUnitario());
        lineaDePedido.setEstado(lineaDePedidoDTO.getEstado());
        lineaDePedido.setPedido(pedidoRepository.findById(lineaDePedidoDTO.getPedidoId())
                .orElseThrow(() -> new ResourceNotFoundException("El pedido no existe")));
        lineaDePedido.setProducto(productoVentaRepository
                .findProductoVentaByNombreAndNegocioId(lineaDePedidoDTO.getNombreProducto(),
                        lineaDePedido.getPedido().getMesa().getNegocio().getId())
                .orElseThrow(() -> new ResourceNotFoundException("El producto no existe")));
        return lineaDePedido;
    }

}
