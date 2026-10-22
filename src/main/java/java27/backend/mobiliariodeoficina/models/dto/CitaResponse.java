package java27.backend.mobiliariodeoficina.models.dto;

import java27.backend.mobiliariodeoficina.models.enums.EstadoCita;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaResponse {
    private Long id;
    private String codigoCita;
    private String nombreCliente;
    private String empresa;
    private String telefono;
    private String email;
    private LocalDateTime fechaHora;
    private String tipoAsesoria;
    private EstadoCita estado;
    private String notas;
}