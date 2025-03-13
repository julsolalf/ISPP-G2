package ispp_g2.gastrostock.mesa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MesaRepository extends CrudRepository<Mesa,String> {
    
    public List<Mesa> findAll();

    public List<Mesa> findMesasByNumeroAsientos(Integer numeroAsientos);

    public Mesa findByName(String name);

    public Mesa findById(Integer id);

    public Mesa findByNumeroMesa(Integer numeroMesa);

}
