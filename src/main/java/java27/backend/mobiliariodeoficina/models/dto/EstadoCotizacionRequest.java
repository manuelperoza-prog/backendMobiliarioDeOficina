package java27.backend.mobiliariodeoficina.models.dto;

import java27.backend.mobiliariodeoficina.models.enums.EstadoCotizacion;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EstadoCotizacionRequest {
    @NotNull(message = "El estado es obligatorio")
    private EstadoCotizacion estado;
}