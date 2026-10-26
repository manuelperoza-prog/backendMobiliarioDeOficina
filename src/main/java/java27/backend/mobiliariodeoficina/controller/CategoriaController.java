package java27.backend.mobiliariodeoficina.controller;

import jakarta.validation.Valid;
import java27.backend.mobiliariodeoficina.models.dto.CategoriaRequest;
import java27.backend.mobiliariodeoficina.models.dto.CategoriaResponse;
import java27.backend.mobiliariodeoficina.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> obtenerTodas() {
        return ResponseEntity.ok(categoriaService.obtenerTodas());
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> crearCategoria(@Valid @RequestBody CategoriaRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crearCategoria(dto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizarCategoria(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequest dto) {

        return ResponseEntity.ok(categoriaService.actualizarCategoria(id, dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);


        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Categoría eliminada con éxito");

        return ResponseEntity.ok(respuesta);
    }
}