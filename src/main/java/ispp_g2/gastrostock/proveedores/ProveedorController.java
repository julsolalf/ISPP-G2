package ispp_g2.gastrostock.proveedores;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    @Autowired
    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public ResponseEntity<List<Proveedor>> findAll() {
        if(proveedorService.findAll().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(proveedorService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/dto")
    public ResponseEntity<List<ProveedorDTO>> findAllDTO() {
        if(proveedorService.findAll().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<ProveedorDTO> proveedoresDto = proveedorService.findAll()
                .stream()
                .map(proveedorService::convertirProveedorDTO)
                .toList();
        return new ResponseEntity<>(proveedoresDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> findById(@PathVariable String id) {
        try {
            Integer idNum = Integer.parseInt(id);
            Proveedor proveedor = proveedorService.findById(idNum);
            if (proveedor == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(proveedor, HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<ProveedorDTO> findByIdDTO(@PathVariable String id) {
        ProveedorDTO proveedorDTO = proveedorService.convertirProveedorDTO(proveedorService.findById(Integer.parseInt(id)));
        if (proveedorDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(proveedorDTO, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Proveedor> findByEmail(@PathVariable("email") String email) {
        Proveedor proveedor = proveedorService.findByEmail(email);
        if(proveedor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(proveedor, HttpStatus.OK);
    }

    @GetMapping("/telefono/{telefono}")
    public ResponseEntity<Proveedor> findByTelefono(@PathVariable("telefono") String telefono) {
        Proveedor proveedor = proveedorService.findByTelefono(telefono);
        if(proveedor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(proveedor, HttpStatus.OK);
    }

    @GetMapping("/direccion/{direccion}")
    public ResponseEntity<Proveedor> findByDireccion(@PathVariable("direccion") String direccion) {
        Proveedor proveedor = proveedorService.findByDireccion(direccion);
        if(proveedor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(proveedor, HttpStatus.OK);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Proveedor> findByNombre(@PathVariable("nombre") String nombre) {
        Proveedor proveedor = proveedorService.findByNombre(nombre);
        if(proveedor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(proveedor, HttpStatus.OK);
    }

    @GetMapping("/negocio/{negocio}")
    public ResponseEntity<List<Proveedor>> findProveedorByNegocioId(@PathVariable("negocio") Integer negocio) {
        if(proveedorService.findProveedorByNegocioId(negocio).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(proveedorService.findProveedorByNegocioId(negocio), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Proveedor> save(@RequestBody @Valid ProveedorDTO proveedor) {
        if(proveedor == null){
            throw new IllegalArgumentException("Proveedor no puede ser nulo");
        }
        Proveedor newProveedor = proveedorService.convertirDTOProveedor(proveedor);
        return new ResponseEntity<>(proveedorService.save(newProveedor), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> update(@PathVariable("id") Integer id, @RequestBody @Valid ProveedorDTO newProveedor) {
        if(newProveedor == null){
            throw new IllegalArgumentException("Proveedor no puede ser nulo");
        }
        if(proveedorService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Proveedor proveedor = proveedorService.convertirDTOProveedor(newProveedor);
        proveedor.setId(id);
        return new ResponseEntity<>(proveedorService.save(proveedor), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Integer id) {
        Proveedor proveedor = proveedorService.findById(id);
        if(proveedor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        proveedorService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
