package ispp_g2.gastrostock.plato;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PlatoRepository extends CrudRepository<Plato, Integer>{
    List<Plato> findAll();

    public Plato findByName(String name);

    public List<Plato> findByCategoria(String categoria);

    public Plato getById();

}
