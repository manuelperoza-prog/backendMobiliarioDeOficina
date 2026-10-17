package java27.backend.mobiliariodeoficina.controller;

import java27.backend.mobiliariodeoficina.models.dto.CategoriaResponse;
import java27.backend.mobiliariodeoficina.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> obtenerTodas() {
        return ResponseEntity.ok(categoriaService.obtenerTodas());
    }
}