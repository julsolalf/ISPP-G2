package ispp_g2.gastrostock.lote;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoteRepository extends CrudRepository<Lote,String> {

    @Query("SELECT l FROM Lote l WHERE l.cantidad = ?1")
    List<Lote> findByCantidad(Integer cantidad);

    @Query("SELECT l FROM Lote l WHERE l.fechaCaducidad = ?1")
    List<Lote> findByFechaCaducidad(String fechaCaducidad);

    @Query("SELECT l FROM Lote l WHERE l.producto.id = ?1")
    List<Lote> findByProductoId(Integer producto);

    @Query("SELECT l FROM Lote l WHERE l.reabastecimiento.id = ?1")
    List<Lote> findByReabastecimientoId(Integer reabastecimiento);

}
