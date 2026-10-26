package java27.backend.mobiliariodeoficina.models.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class CotizacionRequest {

    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String nombreCliente;

    private String empresa;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un formato de email válido")
    private String email;

    private String notas;

    @NotEmpty(message = "La cotización debe tener al menos un producto")
    private List<@Valid ItemCotizacion> items;
}