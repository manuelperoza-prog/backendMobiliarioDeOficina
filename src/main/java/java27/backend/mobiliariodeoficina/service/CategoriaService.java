package java27.backend.mobiliariodeoficina.service;

import java27.backend.mobiliariodeoficina.models.dto.CategoriaRequest;
import java27.backend.mobiliariodeoficina.models.dto.CategoriaResponse;
import java27.backend.mobiliariodeoficina.models.Categoria;
import java27.backend.mobiliariodeoficina.models.interfaces.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<CategoriaResponse> obtenerTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoriaResponse crearCategoria(CategoriaRequest dto) {
        // Generar slug automáticamente en el backend
        String slugGenerado = generarSlug(dto.getNombre());

        Categoria nuevaCategoria = Categoria.builder()
                .nombre(dto.getNombre())
                .slug(slugGenerado)
                .descripcion(dto.getDescripcion())
                .build();

        return mapearADTO(categoriaRepository.save(nuevaCategoria));
    }

    // Método helper para crear el slug
    private String generarSlug(String texto) {
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        String sinTildes = normalizado.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return sinTildes.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-") // Cambia espacios y caracteres raros por guiones
                .replaceAll("^-|-$", "");      // Quita guiones al principio o al final
    }

    @Transactional
    public CategoriaResponse actualizarCategoria(Long id, CategoriaRequest dto) {
        // 1. Buscamos la categoría existente
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        // 2. Generamos un nuevo slug por si el nombre cambió
        String nuevoSlug = generarSlug(dto.getNombre());

        // 3. Actualizamos los datos
        categoria.setNombre(dto.getNombre());
        categoria.setSlug(nuevoSlug);
        categoria.setDescripcion(dto.getDescripcion());

        // 4. Guardamos y devolvemos el DTO
        return mapearADTO(categoriaRepository.save(categoria));
    }

    @Transactional
    public void eliminarCategoria(Long id) {
        // Buscamos la categoría para confirmar que existe antes de borrarla
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

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