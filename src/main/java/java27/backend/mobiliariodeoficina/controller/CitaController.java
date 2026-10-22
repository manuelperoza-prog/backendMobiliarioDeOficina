package java27.backend.mobiliariodeoficina.controller;

import java27.backend.mobiliariodeoficina.models.CitaShowroom;
import java27.backend.mobiliariodeoficina.models.dto.CitaRequest;
import java27.backend.mobiliariodeoficina.models.dto.EstadoCitaRequest;
import java27.backend.mobiliariodeoficina.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
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

    @PutMapping("/{codigoCita}")
    public ResponseEntity<Map<String, String>> modificarCita(
            @PathVariable String codigoCita,
            @Valid @RequestBody CitaRequest dto) {

        citaService.modificarCita(codigoCita, dto);
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Cita modificada con éxito");
        return ResponseEntity.ok(respuesta);
    }

    // Ruta Protegida: Cliente cancela/elimina su propia cita
    @DeleteMapping("/{codigoCita}")
    public ResponseEntity<Map<String, String>> eliminarCita(@PathVariable String codigoCita) {
        citaService.eliminarCita(codigoCita);
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Cita eliminada con éxito");
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/mis-citas")
    public ResponseEntity<List<CitaShowroom>> obtenerMisCitas(Principal principal) {
        // Corrección aplicada aquí: uso de la variable citaService en minúscula
        return ResponseEntity.ok(citaService.obtenerMisCitas(principal.getName()));
    }

    // GET administrativo protegido por SecurityConfig
    @GetMapping
    public ResponseEntity<List<CitaShowroom>> obtenerTodas() {
        return ResponseEntity.ok(citaService.obtenerTodasLasCitas());
    }

    // PATCH administrativo para cambiar estado
    @PatchMapping("/{codigoCita}")
    public ResponseEntity<Map<String, String>> actualizarEstado(
            @PathVariable String codigoCita,
            @Valid @RequestBody EstadoCitaRequest request) {

        citaService.actualizarEstadoCita(codigoCita, request.getEstado());

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Estado de la cita actualizado a " + request.getEstado());
        return ResponseEntity.ok(respuesta);
    }
}