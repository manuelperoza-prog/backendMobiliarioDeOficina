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

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));


        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + dto.getCategoriaId()));


        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setCategoria(categoria);
        producto.setTipoEspacio(TipoEspacio.valueOf(dto.getTipoEspacio().toUpperCase()));
        producto.setPrecioReferencialUsd(dto.getPrecioReferencialUsd());
        producto.setEnShowroom(dto.getEnShowroom() != null ? dto.getEnShowroom() : true);
        producto.setMaterial(dto.getMaterial());
        producto.setDimensionesEstandar(dto.getDimensionesEstandar());


        producto.getImagenes().clear();

        if (dto.getImagenes() != null && !dto.getImagenes().isEmpty()) {
            dto.getImagenes().forEach(url -> {
                producto.agregarImagen(ProductoImagen.builder().urlImagen(url).build());
            });
        }

        return mapearADTO(productoRepository.save(producto));
    }

    @Transactional
    public void eliminarProducto(Long id) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));



        productoRepository.delete(producto);
    }


    private ProductoResponse mapearADTO(Producto producto) {
        List<String> urls = producto.getImagenes().stream()
                .map(ProductoImagen::getUrlImagen)
                .collect(Collectors.toList());

        return ProductoResponse.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .categoriaId(producto.getCategoria().getId())
                .categoriaNombre(producto.getCategoria().getNombre())
                .tipoEspacio(producto.getTipoEspacio().name())
                .precioReferencialUsd(producto.getPrecioReferencialUsd())
                .enShowroom(producto.getEnShowroom())
                .material(producto.getMaterial())
                .dimensionesEstandar(producto.getDimensionesEstandar())
                .imagenes(urls)
                .build();
    }
}