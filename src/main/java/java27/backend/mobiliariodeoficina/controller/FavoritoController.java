package java27.backend.mobiliariodeoficina.controller;

import java27.backend.mobiliariodeoficina.service.FavoritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoService favoritoService;

    @GetMapping
    public ResponseEntity<List<Long>> obtenerFavoritos(Principal principal) {
        // principal.getName() contiene el email del usuario extraído del JWT
        List<Long> favoritos = favoritoService.obtenerIdsFavoritos(principal.getName());
        return ResponseEntity.ok(favoritos);
    }

    @PostMapping("/{productoId}")
    public ResponseEntity<Void> agregarFavorito(
            @PathVariable Long productoId,
            Principal principal) {
        favoritoService.agregarFavorito(principal.getName(), productoId);
        return ResponseEntity.noContent().build(); // Devuelve status 204
    }

    @DeleteMapping("/{productoId}")
    public ResponseEntity<Void> eliminarFavorito(
            @PathVariable Long productoId,
            Principal principal) {
        favoritoService.eliminarFavorito(principal.getName(), productoId);
        return ResponseEntity.noContent().build(); // Devuelve status 204
    }
}