package java27.backend.mobiliariodeoficina.models.dto;

import java27.backend.mobiliariodeoficina.models.enums.EstadoCita;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EstadoCitaRequest {
    @NotNull(message = "El estado es obligatorio")
    private EstadoCita estado;
}