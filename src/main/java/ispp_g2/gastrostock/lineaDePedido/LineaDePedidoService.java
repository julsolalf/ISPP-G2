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
import ispp_g2.gastrostock.pedido.Pedido;
import ispp_g2.gastrostock.pedido.PedidoRepository;
import ispp_g2.gastrostock.productoInventario.ProductoInventario;
import ispp_g2.gastrostock.productoVenta.ProductoVentaRepository;

import java.util.Comparator;
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
        BeanUtils.copyProperties(lineaDePedido, toUpdate, "id", "salioDeCocina", "pedido", "producto");
        return lineaDePedidoRepository.save(toUpdate);
    }

    @Transactional
    public LineaDePedido cambiarEstado(Integer id) {
        LineaDePedido lineaDePedido = lineaDePedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La linea de pedido no existe"));
        if (lineaDePedido.getSalioDeCocina().equals(true)) {
            return lineaDePedido;
        } else {
            lineaDePedido.setSalioDeCocina(true);
            List<Ingrediente> ingredientes = ingredienteRepository
                    .findByProductoVentaId(lineaDePedido.getProducto().getId());
            for (Ingrediente ingrediente : ingredientes) {
                Integer cantidadGastada = lineaDePedido.getCantidad()*ingrediente.getCantidad();
                ProductoInventario productoInventario = ingrediente.getProductoInventario();
                List<Lote> lotes = loteRepository.findByProductoId(productoInventario.getId())
                        .stream().filter(l->l.getCantidad()>0)
                        .sorted(Comparator.comparing(Lote::getFechaCaducidad))
                        .toList();
                restarInventario(lotes, cantidadGastada, 0);
                loteRepository.saveAll(lotes);
            }
            Pedido pedido = lineaDePedido.getPedido();
            pedido.setPrecioTotal(pedido.getPrecioTotal() + lineaDePedido.getPrecioLinea());
            pedidoRepository.save(pedido);
            return lineaDePedidoRepository.save(lineaDePedido);
        }
    }

    private void restarInventario(List<Lote> lotes, Integer cantidad, int i) {
        if (lotes.get(i).getCantidad() < cantidad) {
            restarInventario(lotes, cantidad - lotes.get(i).getCantidad(), i + 1);
            lotes.get(i).setCantidad(0);  
        } else {
            lotes.get(i).setCantidad(lotes.get(i).getCantidad() - cantidad);
        }
    }

    public LineaDePedido convertDtoLineaDePedido(LineaDePedidoDTO lineaDePedidoDTO) {
        LineaDePedido lineaDePedido = new LineaDePedido();
        lineaDePedido.setCantidad(lineaDePedidoDTO.getCantidad());
        lineaDePedido.setPrecioUnitario(lineaDePedidoDTO.getPrecioUnitario());
        lineaDePedido.setSalioDeCocina(lineaDePedidoDTO.getEstado());
        lineaDePedido.setPedido(pedidoRepository.findById(lineaDePedidoDTO.getPedidoId())
                .orElseThrow(() -> new ResourceNotFoundException("El pedido no existe")));
        lineaDePedido.setProducto(productoVentaRepository
                .findById(lineaDePedidoDTO.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("El producto no existe")));
        return lineaDePedido;
    }

}
