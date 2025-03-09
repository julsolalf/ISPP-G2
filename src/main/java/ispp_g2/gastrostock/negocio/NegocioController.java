package ispp_g2.gastrostock.negocio;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/negocios")
public class NegocioController {

    private NegocioService ns;

    @Autowired
    public NegocioController(NegocioService negocioService) {
        this.ns = negocioService;
    }

}
