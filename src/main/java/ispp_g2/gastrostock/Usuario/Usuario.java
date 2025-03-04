package ispp_g2.gastrostock.Usuario;
import java.time.LocalDateTime;

import ispp_g2.gastrostock.Negocios.Negocio;
import ispp_g2.gastrostock.model.Person;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "Usuario")
public class Usuario extends Person {
    
    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String telefono;

    @NotBlank
    private String direccion;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;

    @NotNull
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    private String avatarUrl;

    private String codigoRecuperacion;

    @ManyToOne
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

}