package java27.backend.mobiliariodeoficina.models.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequest {

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "Debe asignar una categoría")
    private Long categoriaId;

    @NotNull(message = "Debe especificar el tipo de espacio")
    private String tipoEspacio;

    @NotNull(message = "El precio es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private BigDecimal precioReferencialUsd;

    private Boolean enShowroom;
    private String material;
    private String dimensionesEstandar;

    // Al crear, se pueden pasar las URLs de las imágenes iniciales
    private List<String> imagenes;
}