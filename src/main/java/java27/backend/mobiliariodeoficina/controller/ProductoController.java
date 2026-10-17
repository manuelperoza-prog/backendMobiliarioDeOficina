package java27.backend.mobiliariodeoficina.controller;

import java27.backend.mobiliariodeoficina.models.dto.ProductoRequest;
import java27.backend.mobiliariodeoficina.models.dto.ProductoResponse;
import java27.backend.mobiliariodeoficina.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    // Ruta Pública: Catálogo con filtros
    // Ejemplo de uso: /api/v1/productos?tipoEspacio=OPERATIVO&enShowroom=true
    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listarProductos(
            @RequestParam(required = false) Boolean enShowroom,
            @RequestParam(required = false) String tipoEspacio,
            @RequestParam(required = false) String buscar) {

        return ResponseEntity.ok(productoService.obtenerTodos(enShowroom, tipoEspacio, buscar));
    }

    // Ruta Pública: Detalle de un mueble específico
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    // Ruta Protegida: Crear nuevo producto (Requiere token JWT de ADMIN o ASESOR)
    @PostMapping
    public ResponseEntity<ProductoResponse> crearProducto(@Valid @RequestBody ProductoRequest dto) {
        ProductoResponse nuevoProducto = productoService.crearProducto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }
}