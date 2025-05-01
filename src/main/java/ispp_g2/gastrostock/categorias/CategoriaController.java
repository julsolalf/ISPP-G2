package ispp_g2.gastrostock.categorias;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.empleado.EmpleadoService;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.negocio.NegocioService;
import ispp_g2.gastrostock.user.User;
import ispp_g2.gastrostock.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final NegocioService negocioService;
    private final UserService userService;
    private final EmpleadoService empleadoService;

    private static final String ADMIN = "admin";
    private static final String DUENO = "dueno";
    private static final String EMPLEADO = "empleado";

    @Autowired
    public CategoriaController(CategoriaService categoriaService, NegocioService negocioService,
    UserService userService, EmpleadoService empleadoService) {
        this.categoriaService = categoriaService;
        this.negocioService = negocioService;
        this.userService = userService;
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> findAll() {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            if(categoriaService.getCategorias().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(categoriaService.getCategorias(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> findById(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(categoriaService.getById(id), HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Categoria categoria = categoriaService.getById(id);
            if(categoria.getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(categoriaService.getById(id), HttpStatus.OK);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Categoria categoria = categoriaService.getById(id);
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(categoria.getNegocio().getId())) {
                return new ResponseEntity<>(categoriaService.getById(id), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<CategoriaDTO> findByIdDto(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(CategoriaDTO.of(categoriaService.getById(id)), HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Categoria categoria = categoriaService.getById(id);
            if(categoria.getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(CategoriaDTO.of(categoriaService.getById(id)), HttpStatus.OK);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Categoria categoria = categoriaService.getById(id);
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(categoria.getNegocio().getId())) {
                return new ResponseEntity<>(CategoriaDTO.of(categoriaService.getById(id)), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/negocio/{negocioId}")
    public ResponseEntity<List<Categoria>> findByNegocioId(@PathVariable("negocioId") Integer negocioId) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            if(categoriaService.getCategoriasByNegocioId(negocioId).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(categoriaService.getCategoriasByNegocioId(negocioId), HttpStatus.OK);  
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Negocio negocio = negocioService.getById(negocioId);
            if(negocio.getDueno().getUser().getId().equals(user.getId())) {
                if(categoriaService.getCategoriasByNegocioId(negocioId).isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(categoriaService.getCategoriasByNegocioId(negocioId), HttpStatus.OK);
            } 
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(negocioId)) {
                if(categoriaService.getCategoriasByNegocioId(negocioId).isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(categoriaService.getCategoriasByNegocioId(negocioId), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto/negocio/{negocioId}")
    public ResponseEntity<List<CategoriaDTO>> findByNegocioIdDto(@PathVariable("negocioId") Integer negocioId) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            if(categoriaService.getCategoriasByNegocioId(negocioId).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(categoriaService.getCategoriasByNegocioId(negocioId)
                    .stream().map(CategoriaDTO::of).toList(), HttpStatus.OK);  
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Negocio negocio = negocioService.getById(negocioId);
            if(negocio.getDueno().getUser().getId().equals(user.getId())) {
                if(categoriaService.getCategoriasByNegocioId(negocioId).isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(categoriaService.getCategoriasByNegocioId(negocioId)
                .stream().map(CategoriaDTO::of).toList(), HttpStatus.OK);
            } 
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(negocioId)) {
                if(categoriaService.getCategoriasByNegocioId(negocioId).isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(categoriaService.getCategoriasByNegocioId(negocioId)
                .stream().map(CategoriaDTO::of).toList(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/negocio/{negocioId}/inventario")
    public ResponseEntity<List<Categoria>> findByNegocioIdInventario(@PathVariable("negocioId") Integer negocioId) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            if(categoriaService.getCategoriasInventarioByNegocioId(negocioId).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(categoriaService.getCategoriasInventarioByNegocioId(negocioId), HttpStatus.OK);  
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Negocio negocio = negocioService.getById(negocioId);
            if(negocio.getDueno().getUser().getId().equals(user.getId())) {
                if(categoriaService.getCategoriasInventarioByNegocioId(negocioId).isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(categoriaService.getCategoriasInventarioByNegocioId(negocioId), HttpStatus.OK);
            } 
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(negocioId)) {
                if(categoriaService.getCategoriasInventarioByNegocioId(negocioId).isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(categoriaService.getCategoriasInventarioByNegocioId(negocioId), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    
    @GetMapping("/dto/negocio/{negocioId}/inventario")
    public ResponseEntity<List<CategoriaDTO>> findByNegocioIdInventarioDto(@PathVariable("negocioId") Integer negocioId) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            if(categoriaService.getCategoriasInventarioByNegocioId(negocioId).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(categoriaService.getCategoriasInventarioByNegocioId(negocioId)
            .stream().map(CategoriaDTO::of).toList(), HttpStatus.OK);  
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Negocio negocio = negocioService.getById(negocioId);
            if(negocio.getDueno().getUser().getId().equals(user.getId())) {
                if(categoriaService.getCategoriasInventarioByNegocioId(negocioId).isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(categoriaService.getCategoriasInventarioByNegocioId(negocioId)
                .stream().map(CategoriaDTO::of).toList(), HttpStatus.OK);
            } 
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(negocioId)) {
                if(categoriaService.getCategoriasInventarioByNegocioId(negocioId).isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(categoriaService.getCategoriasInventarioByNegocioId(negocioId)
                .stream().map(CategoriaDTO::of).toList(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/negocio/{negocioId}/venta")
    public ResponseEntity<List<Categoria>> findByNegocioIdVenta(@PathVariable("negocioId") Integer negocioId) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(categoriaService.getCategoriasVentaByNegocioId(negocioId), HttpStatus.OK);  
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Negocio negocio = negocioService.getById(negocioId);
            if(negocio.getDueno().getUser().getId().equals(user.getId())) {
                if(categoriaService.getCategoriasVentaByNegocioId(negocioId).isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(categoriaService.getCategoriasVentaByNegocioId(negocioId), HttpStatus.OK);
            } 
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(negocioId)) {
                if(categoriaService.getCategoriasVentaByNegocioId(negocioId).isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(categoriaService.getCategoriasVentaByNegocioId(negocioId), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/dto/negocio/{negocioId}/venta")
    public ResponseEntity<List<CategoriaDTO>> findByNegocioIdVentaDto(@PathVariable("negocioId") Integer negocioId) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            if(categoriaService.getCategoriasVentaByNegocioId(negocioId).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(categoriaService.getCategoriasVentaByNegocioId(negocioId)
            .stream().map(CategoriaDTO::of).toList(), HttpStatus.OK);  
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Negocio negocio = negocioService.getById(negocioId);
            if(negocio.getDueno().getUser().getId().equals(user.getId())) {
                if(categoriaService.getCategoriasVentaByNegocioId(negocioId).isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(categoriaService.getCategoriasVentaByNegocioId(negocioId)
                .stream().map(CategoriaDTO::of).toList(), HttpStatus.OK);
            } 
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(negocioId)) {
                if(categoriaService.getCategoriasVentaByNegocioId(negocioId).isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(categoriaService.getCategoriasVentaByNegocioId(negocioId)
                .stream().map(CategoriaDTO::of).toList(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


    @GetMapping("/nombre/{name}")
    public ResponseEntity<List<Categoria>> findByName(@PathVariable("name") String name) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            if(categoriaService.getCategoriasByName(name).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(categoriaService.getCategoriasByName(name), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping
    public ResponseEntity<Categoria> save(@Valid @RequestBody Categoria categoria) {
        if(categoria==null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(categoriaService.save(categoria), HttpStatus.CREATED);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            if(categoria.getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(categoriaService.save(categoria), HttpStatus.CREATED);
            } 
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(categoria.getNegocio().getId())) {
                return new ResponseEntity<>(categoriaService.save(categoria), HttpStatus.CREATED);
            } 
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/dto")
    public ResponseEntity<Categoria> saveDto(@Valid @RequestBody CategoriaDTO categoriaDTO) {
        if(categoriaDTO==null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            Categoria categoria = categoriaService.convertirCategoria(categoriaDTO);
            return new ResponseEntity<>(categoriaService.save(categoria), HttpStatus.CREATED);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Categoria categoria = categoriaService.convertirCategoria(categoriaDTO);
            if(categoria.getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                return new ResponseEntity<>(categoriaService.save(categoria), HttpStatus.CREATED);
            } 
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(categoriaDTO.getNegocioId())) { 
                Categoria categoria = categoriaService.convertirCategoria(categoriaDTO);
                return new ResponseEntity<>(categoriaService.save(categoria), HttpStatus.CREATED);
            } 
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> update(@PathVariable("id") Integer id, @Valid @RequestBody Categoria categoria) {
        if(categoria == null) {
            throw new IllegalArgumentException("Categoria cannot be null");
        }
        Categoria existingCategoria = categoriaService.getById(id);
        User user = userService.findCurrentUser();

        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            return new ResponseEntity<>(categoriaService.update(id, categoria), HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            if(categoria.getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                if(!existingCategoria.getNegocio().getId().equals(categoria.getNegocio().getId())) {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Si el negocio de la categoría no coincide con el de la categoría en la bdd, no se puede actualizar la categoría
                }
                return new ResponseEntity<>(categoriaService.update(id, categoria), HttpStatus.OK);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(categoria.getNegocio().getId())) {
                if(!existingCategoria.getNegocio().getId().equals(categoria.getNegocio().getId())) {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Si el negocio de la categoría no coincide con el de la categoría en la bdd, no se puede actualizar la categoría
                }
                return new ResponseEntity<>(categoriaService.update(id, categoria), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/dto/{id}")
    public ResponseEntity<Categoria> updateDto(@PathVariable("id") Integer id, @Valid @RequestBody CategoriaDTO categoriaDTO) {
        if(categoriaDTO == null) {
            throw new IllegalArgumentException("CategoriaDTO cannot be null");
        }
        Categoria existingCategoria = categoriaService.getById(id);
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            Categoria categoria = categoriaService.convertirCategoria(categoriaDTO);
            return new ResponseEntity<>(categoriaService.update(id, categoria), HttpStatus.OK);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Categoria categoria = categoriaService.convertirCategoria(categoriaDTO);
            if(categoria.getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                if(!existingCategoria.getNegocio().getId().equals(categoriaDTO.getNegocioId())) {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Si el negocio de la categoría no coincide con el de la categoría en la bdd, no se puede actualizar la categoría
                }
                return new ResponseEntity<>(categoriaService.update(id, categoria), HttpStatus.OK);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(categoriaDTO.getNegocioId())) {
                if(!existingCategoria.getNegocio().getId().equals(categoriaDTO.getNegocioId())) {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Si el negocio de la categoría no coincide con el de la categoría en la bdd, no se puede actualizar la categoría
                }
                Categoria categoria = categoriaService.convertirCategoria(categoriaDTO);
                return new ResponseEntity<>(categoriaService.update(id, categoria), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        User user = userService.findCurrentUser();
        if(user.hasAnyAuthority(ADMIN).equals(true)) {
            categoriaService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if(user.hasAnyAuthority(DUENO).equals(true)) {
            Categoria categoria = categoriaService.getById(id);
            if(categoria.getNegocio().getDueno().getUser().getId().equals(user.getId())) {
                categoriaService.delete(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } else if(user.hasAnyAuthority(EMPLEADO).equals(true)) {
            Categoria categoria = categoriaService.getById(id);
            Empleado empleado = empleadoService.getEmpleadoByUser(user.getId());
            if(empleado.getNegocio().getId().equals(categoria.getNegocio().getId())) {
                categoriaService.delete(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
