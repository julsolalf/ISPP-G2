package ispp_g2.gastrostock.Ventas;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface VentaRepository extends CrudRepository<Venta, Integer> {

    List<Venta> findAll();

    public Venta getById();
    
}
