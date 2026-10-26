package java27.backend.mobiliariodeoficina.models.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponse {
    private Long id;
    private String nombre;
    private String descripcion;


    private Long categoriaId;
    private String categoriaNombre;

    private String tipoEspacio;
    private BigDecimal precioReferencialUsd;
    private Boolean enShowroom;
    private String material;
    private String dimensionesEstandar;


    private List<String> imagenes;
}