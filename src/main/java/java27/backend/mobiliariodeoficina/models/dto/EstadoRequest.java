package java27.backend.mobiliariodeoficina.models.dto;

import jakarta.validation.constraints.NotNull;
import java27.backend.mobiliariodeoficina.models.enums.EstadoCotizacion;
import lombok.Data;

@Data
public class EstadoRequest {

    @NotNull(message = "El estado es obligatorio")
    private EstadoCotizacion estado;
}