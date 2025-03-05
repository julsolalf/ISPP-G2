package ispp_g2.gastrostock.Usuario;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {

    List<Usuario> findAll();

    Usuario findByUsername(String username); 

    Usuario findByEmail(String email); 

    public List<Usuario> findByTipo(TipoUsuario tipo);

    public Usuario getById();
    
}
