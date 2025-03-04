package ispp_g2.gastrostock.Productos;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends CrudRepository<Producto, Integer> {
    
    // Buscar productos cuyo nombre contenga el filtro
    Optional<Producto> encontrarProductoPorNombre(String nombre);
    
    // Listar productos en riesgo de desperdicio
    @Query("SELECT p FROM Producto p WHERE p.fechaCaducidad < :fechaLimite")
    List<Producto> encontrarProductosEnRiesgos(LocalDate fechaLimite);

    // Encontrar la lista de productos que están caducados y son desperdiciados
    @Query("SELECT p FROM Producto p WHERE p.fechaCaducidad < CURRENT_DATE")
    List<Producto> encontrarProductosDesperdiciados();
    
}
