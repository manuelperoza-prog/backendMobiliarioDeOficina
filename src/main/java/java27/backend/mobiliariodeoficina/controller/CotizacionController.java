package java27.backend.mobiliariodeoficina.controller;

import jakarta.validation.Valid;
import java27.backend.mobiliariodeoficina.models.Cotizacion;
import java27.backend.mobiliariodeoficina.models.dto.CotizacionRequest;
import java27.backend.mobiliariodeoficina.models.dto.CotizacionResponse;
import java27.backend.mobiliariodeoficina.models.dto.EstadoRequest;
import java27.backend.mobiliariodeoficina.service.CotizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cotizaciones")
public class CotizacionController {

    @Autowired
    private CotizacionService cotizacionService;

    @PostMapping
    public ResponseEntity<CotizacionResponse> crearCotizacion(@Valid @RequestBody CotizacionRequest request) {
        CotizacionResponse response = cotizacionService.crearCotizacion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<Cotizacion>> obtenerCotizaciones() {
        return ResponseEntity.ok(cotizacionService.obtenerTodas());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Cotizacion> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoRequest request) {

        Cotizacion cotizacionActualizada = cotizacionService.actualizarEstado(id, request.getEstado());
        return ResponseEntity.ok(cotizacionActualizada);
    }
}