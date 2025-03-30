package ispp_g2.gastrostock.pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(readOnly = true)
    public Pedido getById(Integer id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Pedido> getAll() {
        Iterable<Pedido> pedido = pedidoRepository.findAll();
        return StreamSupport.stream(pedido.spliterator(), false)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Pedido> getPedidoByFecha(LocalDateTime fecha) {
        return pedidoRepository.findPedidoByFecha(fecha);
    }

    @Transactional(readOnly = true)
    public List<Pedido> getPedidoByPrecioTotal(Double precioTotal) {
        return pedidoRepository.findPedidoByPrecioTotal(precioTotal);
    }

    @Transactional(readOnly = true)
    public List<Pedido> getPedidoByMesaId(Integer mesa) {
        return pedidoRepository.findPedidoByMesaId(mesa);
    }

    @Transactional(readOnly = true)
    public List<Pedido> getPedidoByEmpleadoId(Integer empleado) {
        return pedidoRepository.findPedidoByEmpleadoId(empleado);
    }

    @Transactional(readOnly = true)
    public List<Pedido> getPedidoByNegocioId(Integer negocio) {
        return pedidoRepository.findPedidoByNegocioId(negocio);
    }

    @PostMapping
    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void delete(Integer id) {
        pedidoRepository.deleteById(id);
    }
    
}
