package ispp_g2.gastrostock.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    // Redirige todas las rutas que no sean archivos estáticos a index.html
    @RequestMapping(value = {"/", "/{path:[^\\.]*}"})
    public String forward() {
        return "forward:/index.html";  // Servirá el archivo index.html
    }
}