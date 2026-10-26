package java27.backend.mobiliariodeoficina.controller;

import java27.backend.mobiliariodeoficina.models.dto.ProductoRequest;
import java27.backend.mobiliariodeoficina.models.dto.ProductoResponse;
import java27.backend.mobiliariodeoficina.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;



    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listarProductos(
            @RequestParam(required = false) Boolean enShowroom,
            @RequestParam(required = false) String tipoEspacio,
            @RequestParam(required = false) String buscar) {

        return ResponseEntity.ok(productoService.obtenerTodos(enShowroom, tipoEspacio, buscar));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }


    @PostMapping
    public ResponseEntity<ProductoResponse> crearProducto(@Valid @RequestBody ProductoRequest dto) {
        ProductoResponse nuevoProducto = productoService.crearProducto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest dto) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);


        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Producto eliminado con éxito");

        return ResponseEntity.ok(respuesta);
    }
}