package java27.backend.mobiliariodeoficina.service;

import java27.backend.mobiliariodeoficina.models.dto.ProductoRequest;
import java27.backend.mobiliariodeoficina.models.dto.ProductoResponse;
import java27.backend.mobiliariodeoficina.models.Categoria;
import java27.backend.mobiliariodeoficina.models.Producto;
import java27.backend.mobiliariodeoficina.models.ProductoImagen;
import java27.backend.mobiliariodeoficina.models.enums.TipoEspacio;
import java27.backend.mobiliariodeoficina.models.interfaces.CategoriaRepository;
import java27.backend.mobiliariodeoficina.models.interfaces.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<ProductoResponse> obtenerTodos(Boolean enShowroom, String tipoEspacio, String buscar) {
        List<Producto> productos;

        // Reglas de filtrado para el catálogo
        if (buscar != null && !buscar.isBlank()) {
            productos = productoRepository.buscarPorNombre(buscar);
        } else if (enShowroom != null && enShowroom) {
            productos = productoRepository.findByEnShowroomTrue();
        } else if (tipoEspacio != null && !tipoEspacio.isBlank()) {
            productos = productoRepository.findByTipoEspacio(TipoEspacio.valueOf(tipoEspacio.toUpperCase()));
        } else {
            productos = productoRepository.findAll();
        }

        return productos.stream().map(this::mapearADTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        return mapearADTO(producto);
    }

    @Transactional
    public ProductoResponse crearProducto(ProductoRequest dto) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + dto.getCategoriaId()));

        Producto producto = Producto.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .categoria(categoria)
                .tipoEspacio(TipoEspacio.valueOf(dto.getTipoEspacio().toUpperCase()))
                .precioReferencialUsd(dto.getPrecioReferencialUsd())
                .enShowroom(dto.getEnShowroom() != null ? dto.getEnShowroom() : true)
                .material(dto.getMaterial())
                .dimensionesEstandar(dto.getDimensionesEstandar())
                .build();

        // Si mandan URLs de imágenes en el DTO, las asociamos usando el método helper de la entidad
        if (dto.getImagenes() != null && !dto.getImagenes().isEmpty()) {
            dto.getImagenes().forEach(url -> {
                producto.agregarImagen(ProductoImagen.builder().urlImagen(url).build());
            });
        }

        Producto productoGuardado = productoRepository.save(producto);
        return mapearADTO(productoGuardado);
    }

    @Transactional
    public ProductoResponse actualizarProducto(Long id, ProductoRequest dto) {
        // Buscamos el producto existente
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        // Buscamos la categoría por si la cambió
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + dto.getCategoriaId()));

        // Actualizamos los datos básicos
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setCategoria(categoria);
        producto.setTipoEspacio(TipoEspacio.valueOf(dto.getTipoEspacio().toUpperCase()));
        producto.setPrecioReferencialUsd(dto.getPrecioReferencialUsd());
        producto.setEnShowroom(dto.getEnShowroom() != null ? dto.getEnShowroom() : true);
        producto.setMaterial(dto.getMaterial());
        producto.setDimensionesEstandar(dto.getDimensionesEstandar());

        // Manejo de Imágenes: Borramos las viejas y guardamos las nuevas
        producto.getImagenes().clear(); // Hibernate borrará las antiguas automáticamente

        if (dto.getImagenes() != null && !dto.getImagenes().isEmpty()) {
            dto.getImagenes().forEach(url -> {
                producto.agregarImagen(ProductoImagen.builder().urlImagen(url).build());
            });
        }

        return mapearADTO(productoRepository.save(producto));
    }

    @Transactional
    public void eliminarProducto(Long id) {
        // Buscamos el producto primero para asegurar que existe
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        // Al eliminar el producto, gracias al orphanRemoval=true y CascadeType.ALL
        // en la entidad, las imágenes asociadas se borrarán automáticamente.
        productoRepository.delete(producto);
    }

    // Transformador manual Entidad -> DTO (Evita bucles y oculta datos internos)
    private ProductoResponse mapearADTO(Producto producto) {
        List<String> urls = producto.getImagenes().stream()
                .map(ProductoImagen::getUrlImagen)
                .collect(Collectors.toList());

        return ProductoResponse.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .categoriaId(producto.getCategoria().getId())         // <--- LÍNEA AGREGADA
                .categoriaNombre(producto.getCategoria().getNombre()) // Sacamos solo el nombre
                .tipoEspacio(producto.getTipoEspacio().name())
                .precioReferencialUsd(producto.getPrecioReferencialUsd())
                .enShowroom(producto.getEnShowroom())
                .material(producto.getMaterial())
                .dimensionesEstandar(producto.getDimensionesEstandar())
                .imagenes(urls)
                .build();
    }
}