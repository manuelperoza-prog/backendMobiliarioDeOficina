package java27.backend.mobiliariodeoficina.models.dto;

import java27.backend.mobiliariodeoficina.models.enums.EstadoCita;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CitaResponse {
    private String codigoCita;
    private String nombreCliente;
    private String email;
    private String empresa;
    private String telefono;
    private LocalDateTime fechaHora;
    private String tipoAsesoria;
    private EstadoCita estado;
    private String notas;
    private LocalDateTime creadoEn;
}