package ispp_g2.gastrostock.ventas;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ispp_g2.gastrostock.exceptions.ResourceNotFoundException;
import ispp_g2.gastrostock.negocio.NegocioRepository;
import ispp_g2.gastrostock.pedido.PedidoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final NegocioRepository negocioRepository;
    private final PedidoRepository pedidoRepository;

    @Transactional(readOnly = true)
    public List<Venta> getAll() {
        return StreamSupport.stream(ventaRepository.findAll().spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public Venta getById(Integer id) {
        return ventaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("La venta no existe"));
    }

    @Transactional(readOnly = true)
    public List<Venta> getVentasByNegocioId(Integer negocioId) {
        return ventaRepository.findVentasByNegocioId(negocioId);
    }

    @Transactional
    public Venta save(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Transactional
    public Venta update(Integer id, Venta venta) {
        Venta toUpdate = ventaRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("La venta no existe"));
        BeanUtils.copyProperties(venta, toUpdate, "id");
        return ventaRepository.save(toUpdate);
    }

    @Transactional
    public void delete(Integer id) {
        Venta toDelete = ventaRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("La venta no existe"));
            ventaRepository.delete(toDelete);
    }

    public Venta convertirVenta(VentaDto ventaDto) {
        Venta venta = new Venta();
        venta.setId(ventaDto.getId());
        venta.setNegocio(negocioRepository.findById(ventaDto.getNegocioId()).orElseThrow(()-> new ResourceNotFoundException("El negocio no existe")));
        return venta;
    }

    public VentaDto convertirVentaDto(Venta venta) {
        VentaDto ventaDto = new VentaDto();
        ventaDto.setId(venta.getId());
        ventaDto.setNegocioId(venta.getNegocio().getId());
        ventaDto.setPrecioTotal(venta.getPrecioTotal(pedidoRepository.findPedidoByVentaId(venta.getId())));
        return ventaDto;
    }

}
