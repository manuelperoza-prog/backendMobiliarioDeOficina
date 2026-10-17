package java27.backend.mobiliariodeoficina.controller;

import java27.backend.mobiliariodeoficina.models.dto.CitaRequest;
import java27.backend.mobiliariodeoficina.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    // Ruta Pública: Formulario de agendamiento desde el frontend
    @PostMapping
    public ResponseEntity<Map<String, String>> agendarCita(@Valid @RequestBody CitaRequest dto) {
        String codigoGenerado = citaService.agendarCita(dto);

        // Devolvemos un JSON amigable {"mensaje": "...", "codigoCita": "CITA-XXXX"}
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Cita agendada con éxito");
        respuesta.put("codigoCita", codigoGenerado);

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }
}