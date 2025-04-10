package ispp_g2.gastrostock.auth.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank
    String username;
    
    @NotBlank
    String password;
    
    @NotBlank
    String firstName;
    
    @NotBlank
    String lastName;
    
    @NotBlank
    String email;
    
    @NotBlank
    String numTelefono;

}
