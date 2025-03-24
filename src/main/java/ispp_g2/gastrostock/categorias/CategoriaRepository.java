package ispp_g2.gastrostock.categorias;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends CrudRepository<Categoria, String> {

    @Query("SELECT c FROM Categoria c WHERE c.name = ?1")
    List<Categoria> findByName(String name);

    @Query("SELECT c FROM Categoria c WHERE c.negocio.id = ?1")
    List<Categoria> findByNegocioId(String negocioId);
}
