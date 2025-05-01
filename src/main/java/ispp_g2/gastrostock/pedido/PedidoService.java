package ispp_g2.gastrostock.pedido;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ispp_g2.gastrostock.empleado.EmpleadoRepository;
import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.mesa.MesaRepository;
import ispp_g2.gastrostock.negocio.NegocioRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final MesaRepository mesaRepository;
    private final EmpleadoRepository empleadoRepository;
    private final NegocioRepository negocioRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, MesaRepository mesaRepository, EmpleadoRepository empleadoRepository, NegocioRepository negocioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.mesaRepository = mesaRepository;
        this.empleadoRepository = empleadoRepository;
        this.negocioRepository = negocioRepository;
    }

    @Transactional(readOnly = true)
    public Pedido getById(Integer id) {
        return pedidoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Pedido no encontrado"));
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

    @Transactional(readOnly = true)
    public Map<Integer, Integer> getNumPedidosPorSemana(Integer negocio) {
        List<Pedido> pedidos = pedidoRepository.findPedidoByNegocioId(negocio);
        Map<Integer, Integer> numPedidosPorSemana = new HashMap<>();
        pedidos.forEach(pedido -> {
            Integer week = pedido.getFecha().toLocalDate().get(WeekFields.ISO.weekOfWeekBasedYear());
            if(numPedidosPorSemana.containsKey(week)){
                numPedidosPorSemana.put(week, numPedidosPorSemana.get(week) + 1);
            } else {
                numPedidosPorSemana.put(week, 1);
            }
        });
        return numPedidosPorSemana.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
    @Transactional(readOnly = true)
    public Map<Month, Double> getIngresosPorMes(Integer negocio) {
        List<Pedido> pedidos = pedidoRepository.findPedidoByNegocioId(negocio);
        Map<Month, Double> ingresosPorMes = new HashMap<>();
        pedidos.forEach(pedido -> {
            Month month = pedido.getFecha().getMonth();
            if(ingresosPorMes.containsKey(month)){
                ingresosPorMes.put(month, ingresosPorMes.get(month) + pedido.getPrecioTotal());
            } else {
                ingresosPorMes.put(month, pedido.getPrecioTotal());
            }
        });
        return ingresosPorMes.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    @Transactional
    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido update(Integer id, Pedido pedido) {
        Pedido pedidoActual = pedidoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Pedido no encontrado"));
        BeanUtils.copyProperties(pedido, pedidoActual, "id");
        return pedidoRepository.save(pedidoActual); 
    }

    public Pedido updatePagado(Integer id, LocalDateTime fecha) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Pedido no encontrado"));
        pedido.setFecha(fecha);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void delete(Integer id) {
        pedidoRepository.deleteById(id);
    }

    public Pedido convertirPedido(PedidoDto pedidoDTO) {
        Pedido pedido = new Pedido();
        pedido.setFecha(pedidoDTO.getFecha());
        pedido.setPrecioTotal(pedidoDTO.getPrecioTotal());
        pedido.setMesa(mesaRepository.findById(pedidoDTO.getMesaId()).orElseThrow(()-> new ResourceNotFoundException("Mesa no encontrada")));
        pedido.setEmpleado(empleadoRepository.findById(pedidoDTO.getEmpleadoId()).orElseThrow(()-> new ResourceNotFoundException("Empleado no encontrado")));
        pedido.setNegocio(negocioRepository.findById(pedidoDTO.getNegocioId()).orElseThrow(()-> new ResourceNotFoundException("Negocio no encontrado")));
        return pedido;
    }

    public PedidoDto convertirPedidoDto(Pedido pedido) {
        PedidoDto pedidoDTO = new PedidoDto();
        pedidoDTO.setId(pedido.getId());
        pedidoDTO.setFecha(pedido.getFecha());
        pedidoDTO.setPrecioTotal(pedido.getPrecioTotal());
        pedidoDTO.setMesaId(pedido.getMesa().getId());
        pedidoDTO.setEmpleadoId(pedido.getEmpleado().getId());
        pedidoDTO.setNegocioId(pedido.getNegocio().getId());
        return pedidoDTO; 
    }


    
}
