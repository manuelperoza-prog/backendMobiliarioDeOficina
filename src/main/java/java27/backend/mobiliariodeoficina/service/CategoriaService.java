package java27.backend.mobiliariodeoficina.service;

import java27.backend.mobiliariodeoficina.models.dto.CategoriaRequest;
import java27.backend.mobiliariodeoficina.models.dto.CategoriaResponse;
import java27.backend.mobiliariodeoficina.models.Categoria;
import java27.backend.mobiliariodeoficina.models.interfaces.CategoriaRepository;
import java27.backend.mobiliariodeoficina.models.interfaces.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.Normalizer;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public List<CategoriaResponse> obtenerTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoriaResponse crearCategoria(CategoriaRequest dto) {

        String slugGenerado = generarSlug(dto.getNombre());

        Categoria nuevaCategoria = Categoria.builder()
                .nombre(dto.getNombre())
                .slug(slugGenerado)
                .descripcion(dto.getDescripcion())
                .build();

        return mapearADTO(categoriaRepository.save(nuevaCategoria));
    }


    private String generarSlug(String texto) {
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        String sinTildes = normalizado.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return sinTildes.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }

    @Transactional
    public CategoriaResponse actualizarCategoria(Long id, CategoriaRequest dto) {

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));


        String nuevoSlug = generarSlug(dto.getNombre());


        categoria.setNombre(dto.getNombre());
        categoria.setSlug(nuevoSlug);
        categoria.setDescripcion(dto.getDescripcion());


        return mapearADTO(categoriaRepository.save(categoria));
    }


    @Transactional
    public void eliminarCategoria(Long idCategoria) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La categoría no existe"));


        boolean tieneProductos = productoRepository.existsByCategoriaId(idCategoria);

        if (tieneProductos) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar la categoría porque tiene productos asociados. Elimine o reasigne los productos primero."
            );
        }

        categoriaRepository.delete(categoria);
    }

    private CategoriaResponse mapearADTO(Categoria categoria) {
        return CategoriaResponse.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .slug(categoria.getSlug())
                .descripcion(categoria.getDescripcion())
                .build();
    }
}