package ispp_g2.gastrostock.categorias;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends CrudRepository<Categoria, Integer> {

    @Query("SELECT c FROM Categoria c WHERE c.name = ?1")
    List<Categoria> findByName(String name);

    @Query("SELECT c FROM Categoria c WHERE c.negocio.id = ?1")
    List<Categoria> findByNegocioId(Integer negocioId);

    @Query("SELECT c FROM Categoria c WHERE c.negocio.id =?1 AND c.pertenece = 0")
    List<Categoria> findInventarioByNegocioId(Integer negocioId);

    @Query("SELECT c FROM Categoria c WHERE c.negocio.id =?1 AND c.pertenece = 1")
    List<Categoria> findVentaByNegocioId(Integer negocioId);

}
