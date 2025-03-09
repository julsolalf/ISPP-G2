package ispp_g2.gastrostock.proveedores;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private ProveedorService ps;

    @Autowired
    public ProveedorController(ProveedorService proveedorService) {
        this.ps = proveedorService;
    }

}
