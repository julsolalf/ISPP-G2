package ispp_g2.gastrostock.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permitir CORS para todos los endpoints
        registry.addMapping("/**")
                .allowedOrigins("https://ispp-2425-g2.ew.r.appspot.com")  // Permitir solicitudes desde el frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Métodos permitidos
                .allowedHeaders("*");  // Permitir todos los encabezados
    }
}