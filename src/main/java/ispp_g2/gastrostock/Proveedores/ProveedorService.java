package ispp_g2.gastrostock.proveedores;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ProveedorService {

    private final ProveedorRepository pr;

    @Autowired
    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.pr = proveedorRepository;
    }

}
