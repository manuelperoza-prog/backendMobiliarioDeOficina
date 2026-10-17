package java27.backend.mobiliariodeoficina.config;

import java27.backend.mobiliariodeoficina.models.Categoria;
import java27.backend.mobiliariodeoficina.models.Producto;
import java27.backend.mobiliariodeoficina.models.ProductoImagen;
import java27.backend.mobiliariodeoficina.models.TipoEspacio;
import java27.backend.mobiliariodeoficina.models.CategoriaRepository;
import java27.backend.mobiliariodeoficina.models.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
public class DataSeedConfig {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    @Bean
    public CommandLineRunner cargarDatosIniciales() {
        return args -> {
            // Solo inserta si la base de datos está vacía
            if (categoriaRepository.count() == 0) {

                // 1. Crear Categorías
                Categoria catOperativo = Categoria.builder()
                        .nombre("Estaciones Operativas")
                        .slug("estaciones-operativas")
                        .descripcion("Mobiliario modular para equipos de trabajo dinámicos.")
                        .build();

                Categoria catSilleria = Categoria.builder()
                        .nombre("Sillería Ergonómica")
                        .slug("silleria-ergonomica")
                        .descripcion("Sillas diseñadas para el máximo confort y salud postural.")
                        .build();

                categoriaRepository.save(catOperativo);
                categoriaRepository.save(catSilleria);

                // 2. Crear Productos de Prueba
                Producto escritorio = Producto.builder()
                        .nombre("Escritorio Modular tipo Bench para 4 personas")
                        .descripcion("Estación de trabajo optimizada con pasacables integrados y pantallas divisorias.")
                        .categoria(catOperativo)
                        .tipoEspacio(TipoEspacio.OPERATIVO)
                        .precioReferencialUsd(new BigDecimal("450.00"))
                        .enShowroom(true)
                        .material("Melamina 18mm con estructura metálica electrostática.")
                        .dimensionesEstandar("2.40m x 1.20m x 0.75m")
                        .build();
                escritorio.agregarImagen(ProductoImagen.builder().urlImagen("https://ejemplo.com/fotos/escritorio-bench.jpg").build());

                Producto silla = Producto.builder()
                        .nombre("Silla Ejecutiva Malla Transpirable")
                        .descripcion("Silla con soporte lumbar ajustable, brazos 3D y mecanismo sincrónico.")
                        .categoria(catSilleria)
                        .tipoEspacio(TipoEspacio.GERENCIAL)
                        .precioReferencialUsd(new BigDecimal("120.00"))
                        .enShowroom(true)
                        .material("Asiento en espuma inyectada, respaldo en malla, base de nylon.")
                        .dimensionesEstandar("Estándar ajustable")
                        .build();
                silla.agregarImagen(ProductoImagen.builder().urlImagen("https://ejemplo.com/fotos/silla-ejecutiva.jpg").build());

                productoRepository.save(escritorio);
                productoRepository.save(silla);

                System.out.println("✅ Datos semilla cargados con éxito.");
            }
        };
    }
}