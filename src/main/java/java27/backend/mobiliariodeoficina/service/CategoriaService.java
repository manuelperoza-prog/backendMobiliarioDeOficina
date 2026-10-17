package java27.backend.mobiliariodeoficina.service;

import java27.backend.mobiliariodeoficina.models.dto.CategoriaResponse;
import java27.backend.mobiliariodeoficina.models.Categoria;
import java27.backend.mobiliariodeoficina.models.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    private CategoriaResponse mapearADTO(Categoria categoria) {
        return CategoriaResponse.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .slug(categoria.getSlug())
                .descripcion(categoria.getDescripcion())
                .build();
    }
}