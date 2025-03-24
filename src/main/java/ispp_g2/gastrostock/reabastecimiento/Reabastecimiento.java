package ispp_g2.gastrostock.reabastecimiento;

import java.time.LocalDate;
import java.util.List;

import ispp_g2.gastrostock.lote.Lote;
import ispp_g2.gastrostock.model.BaseEntity;
import ispp_g2.gastrostock.negocio.Negocio;
import ispp_g2.gastrostock.proveedores.Proveedor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Reabastecimiento extends BaseEntity {

    @NotNull
    private LocalDate fecha;

    @NotNull
    @Positive
    private Double precioTotal;

    @NotBlank
    private String referencia;

    @ManyToOne(optional = false)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

    @OneToMany(mappedBy = "reabastecimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lote> lotes;
}
