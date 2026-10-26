package java27.backend.mobiliariodeoficina.controller;

import java27.backend.mobiliariodeoficina.models.dto.CitaRequest;
import java27.backend.mobiliariodeoficina.models.dto.CitaResponse;
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

    @PostMapping
    public ResponseEntity<Map<String, String>> agendarCita(@Valid @RequestBody CitaRequest dto) {
        String codigoGenerado = citaService.agendarCita(dto);

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

    @DeleteMapping("/{codigoCita}")
    public ResponseEntity<Map<String, String>> eliminarCita(@PathVariable String codigoCita) {
        citaService.eliminarCita(codigoCita);
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Cita eliminada con éxito");
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/mis-citas")
    public ResponseEntity<List<CitaResponse>> obtenerMisCitas(Principal principal) {
        return ResponseEntity.ok(citaService.obtenerMisCitas(principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<CitaResponse>> obtenerTodas() {
        return ResponseEntity.ok(citaService.obtenerTodasLasCitas());
    }

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