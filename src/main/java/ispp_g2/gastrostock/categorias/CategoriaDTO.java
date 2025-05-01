package ispp_g2.gastrostock.categorias;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaDTO {

    private Integer id;

    @NotBlank
    private String nombre;

    @NotNull
    private Integer negocioId;

    @NotNull
    private Pertenece pertenece;

    public static CategoriaDTO of(Categoria categoria) {
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(categoria.getId());
        categoriaDTO.setNombre(categoria.getName());
        categoriaDTO.setNegocioId(categoria.getNegocio().getId());
        categoriaDTO.setPertenece(categoria.getPertenece());
        return categoriaDTO;
    }
    
}
