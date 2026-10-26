package java27.backend.mobiliariodeoficina.controller;

import jakarta.validation.Valid;
import java27.backend.mobiliariodeoficina.models.Cotizacion;
import java27.backend.mobiliariodeoficina.models.dto.CotizacionRequest;
import java27.backend.mobiliariodeoficina.models.dto.CotizacionResponse;
import java27.backend.mobiliariodeoficina.models.dto.EstadoCotizacionRequest;
import java27.backend.mobiliariodeoficina.service.CotizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<Cotizacion>> obtenerTodas() {
        return ResponseEntity.ok(cotizacionService.obtenerTodasLasCotizaciones());
    }

    @PatchMapping("/{codigo}")
    public ResponseEntity<Map<String, String>> actualizarEstado(
            @PathVariable String codigo,
            @Valid @RequestBody EstadoCotizacionRequest request) {

        cotizacionService.actualizarEstadoCotizacion(codigo, request.getEstado());

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Estado de la cotización actualizado a " + request.getEstado());
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/mis-cotizaciones")
    public ResponseEntity<List<Cotizacion>> obtenerMisCotizaciones(Principal principal) {
        return ResponseEntity.ok(cotizacionService.obtenerMisCotizaciones(principal.getName()));
    }
}