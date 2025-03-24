package ispp_g2.gastrostock.negocio;

import ispp_g2.gastrostock.categorias.Categoria;
import ispp_g2.gastrostock.diaReparto.DiaReparto;
import ispp_g2.gastrostock.dueño.Dueño;
import ispp_g2.gastrostock.empleado.Empleado;
import ispp_g2.gastrostock.mesa.Mesa;
import ispp_g2.gastrostock.model.NamedEntity;
import ispp_g2.gastrostock.pedido.Pedido;
import ispp_g2.gastrostock.reabastecimiento.Reabastecimiento;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Getter
@Setter
public class Negocio extends NamedEntity {
    
    @NotNull
    @Column(unique = true)
    private Integer tokenNegocio;
    @NotBlank
    private String direccion;
    @NotNull
	@Digits(fraction = 0, integer = 5)
    private String codigoPostal;
    @NotBlank
    private String ciudad;
    @NotBlank
    private String pais;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dueño_id")
    private Dueño dueño;

    @OneToMany(mappedBy = "negocio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pedido> pedidos;

    @OneToMany(mappedBy = "negocio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaReparto> diasReparto;

    @OneToMany(mappedBy = "negocio",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mesa> mesas;

    @OneToMany(mappedBy = "negocio",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reabastecimiento> reabastecimientos;

    @OneToMany(mappedBy = "negocio",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Categoria> categorias;

    @OneToMany(mappedBy = "negocio",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Empleado> empleados;

}
