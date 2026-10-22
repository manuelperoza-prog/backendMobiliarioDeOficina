package java27.backend.mobiliariodeoficina.models.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombreCliente;

    private String empresa; // Opcional

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo no es válido")
    private String email;

    @NotNull(message = "Debe indicar la fecha y hora de la visita")
    @Future(message = "La fecha de la cita debe ser en el futuro")
    private LocalDateTime fechaHora;

    @NotNull(message = "Debe seleccionar el tipo de asesoría")
    private String tipoAsesoria;

    private String notas;
}