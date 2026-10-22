package java27.backend.mobiliariodeoficina.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PerfilResponse {
    private String nombre;
    private String email;
    private String role;
}