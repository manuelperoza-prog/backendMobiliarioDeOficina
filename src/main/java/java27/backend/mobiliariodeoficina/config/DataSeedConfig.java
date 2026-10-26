package java27.backend.mobiliariodeoficina.config;

import java27.backend.mobiliariodeoficina.models.Categoria;
import java27.backend.mobiliariodeoficina.models.Producto;
import java27.backend.mobiliariodeoficina.models.ProductoImagen;
import java27.backend.mobiliariodeoficina.models.enums.TipoEspacio;
import java27.backend.mobiliariodeoficina.models.interfaces.CategoriaRepository;
import java27.backend.mobiliariodeoficina.models.interfaces.ProductoRepository;
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

            if (categoriaRepository.count() > 0) {
                return;
            }




            Categoria escritorios = categoriaRepository.save(categoria(
                    "Escritorios", "escritorios",
                    "Escritorios y estaciones de trabajo para oficinas, gerencias y espacios en casa."));

            Categoria silleria = categoriaRepository.save(categoria(
                    "Sillería", "silleria",
                    "Sillas ergonómicas, ejecutivas, operativas y de visita para cada tipo de espacio."));

            Categoria comedores = categoriaRepository.save(categoria(
                    "Comedores", "comedores",
                    "Juegos de comedor para hogares y áreas de descanso de empresas."));

            Categoria peinadores = categoriaRepository.save(categoria(
                    "Peinadores", "peinadores",
                    "Peinadores y tocadores para salones de belleza, barberías y habitaciones."));

            Categoria camillas = categoriaRepository.save(categoria(
                    "Camillas", "camillas",
                    "Camillas para consultorios, spas y centros de estética."));






            productoRepository.save(producto(
                    "Escritorio Ejecutivo en L",
                    "Escritorio gerencial en forma de L con módulo de gavetas con cerradura y pasacables integrado.",
                    escritorios, TipoEspacio.OFICINA, "480.00", true,
                    "Melamina 18mm acabado roble, cantos en PVC 2mm.",
                    "1.60m x 1.50m x 0.75m",
                    "https://images.unsplash.com/photo-1518455027359-f3f8164ba6bd?q=80&w=800",
                    "https://images.unsplash.com/photo-1524758631624-e2822e304c36?q=80&w=800"));

            productoRepository.save(producto(
                    "Estación de Trabajo Bench para 4 Personas",
                    "Estación operativa compartida con paneles divisorios y canaleta central para cableado.",
                    escritorios, TipoEspacio.OFICINA, "850.00", true,
                    "Melamina 18mm con estructura metálica con pintura electrostática.",
                    "2.40m x 1.20m x 0.75m",
                    "https://images.unsplash.com/photo-1497215848147-380126786191?q=80&w=800"));

            productoRepository.save(producto(
                    "Escritorio Compacto Home Office",
                    "Escritorio de líneas simples con repisa superior, ideal para espacios pequeños en casa.",
                    escritorios, TipoEspacio.HOGAR, "160.00", false,
                    "Melamina 15mm acabado blanco, patas metálicas negras.",
                    "1.00m x 0.50m x 0.75m",
                    "https://images.unsplash.com/photo-1517502884422-41eaead166d4?q=80&w=800"));


            productoRepository.save(producto(
                    "Silla Ejecutiva Malla Transpirable",
                    "Silla con soporte lumbar ajustable, brazos 3D, cabecero y mecanismo sincrónico.",
                    silleria, TipoEspacio.OFICINA, "220.00", true,
                    "Respaldo en malla, asiento en espuma inyectada, base de aluminio.",
                    "0.65m x 0.65m x 1.20m",
                    "https://images.unsplash.com/photo-1505843490538-5133c6c7d0e1?q=80&w=800"));

            productoRepository.save(producto(
                    "Silla Operativa Ergonómica",
                    "Silla de uso diario con regulación de altura neumática y respaldo con soporte lumbar.",
                    silleria, TipoEspacio.OFICINA, "85.00", true,
                    "Respaldo en malla, asiento tapizado en tela, base de nylon.",
                    "0.60m x 0.60m x 1.00m",
                    "https://images.unsplash.com/photo-1592078615290-033ee584e267?q=80&w=800"));

            productoRepository.save(producto(
                    "Silla de Visita Apilable",
                    "Silla fija para recepciones y salas de espera, apilable para ahorrar espacio.",
                    silleria, TipoEspacio.CONSULTORIO, "45.00", false,
                    "Asiento y respaldo tapizados en semicuero, estructura metálica cromada.",
                    "0.55m x 0.55m x 0.82m",
                    "https://images.unsplash.com/photo-1612872087720-bb876e2e67d1?q=80&w=800"));


            productoRepository.save(producto(
                    "Juego de Comedor 4 Puestos",
                    "Mesa rectangular con cuatro sillas tapizadas, ideal para apartamentos.",
                    comedores, TipoEspacio.HOGAR, "390.00", true,
                    "Mesa en MDF acabado nogal, sillas tapizadas en tela.",
                    "Mesa: 1.20m x 0.80m x 0.76m",
                    "https://images.unsplash.com/photo-1604578762246-41134e37f9cc?q=80&w=800"));

            productoRepository.save(producto(
                    "Juego de Comedor 6 Puestos",
                    "Mesa amplia con tope de vidrio templado y seis sillas de respaldo alto.",
                    comedores, TipoEspacio.HOGAR, "680.00", true,
                    "Tope en vidrio templado 8mm, base metálica, sillas en semicuero.",
                    "Mesa: 1.60m x 0.90m x 0.76m",
                    "https://images.unsplash.com/photo-1577140917170-285929fb55b7?q=80&w=800"));

            productoRepository.save(producto(
                    "Mesa de Comedor Corporativo",
                    "Mesa resistente para áreas de comedor y descanso de empleados.",
                    comedores, TipoEspacio.OFICINA, "320.00", false,
                    "Tope en melamina 25mm, estructura metálica con pintura electrostática.",
                    "1.80m x 0.80m x 0.75m",
                    "https://images.unsplash.com/photo-1577140917170-285929fb55b7?q=80&w=800"));


            productoRepository.save(producto(
                    "Peinador Profesional con Espejo",
                    "Estación de trabajo para salón con espejo amplio, repisa y gavetas para herramientas.",
                    peinadores, TipoEspacio.SALON_BELLEZA, "350.00", true,
                    "Melamina 18mm acabado blanco, espejo de 5mm.",
                    "0.90m x 0.45m x 1.80m",
                    "https://images.unsplash.com/photo-1633534125867-27b9c375b486?q=80&w=800"));

            productoRepository.save(producto(
                    "Peinador Doble para Barbería",
                    "Peinador de dos puestos con espejos independientes e iluminación LED.",
                    peinadores, TipoEspacio.SALON_BELLEZA, "620.00", false,
                    "Melamina 18mm acabado negro, espejos de 5mm, tira LED.",
                    "1.80m x 0.45m x 1.90m",
                    "https://images.unsplash.com/photo-1560066984-138dadb4c035?q=80&w=800"));

            productoRepository.save(producto(
                    "Peinador Tocador con Banqueta",
                    "Tocador para habitación con espejo, tres gavetas y banqueta tapizada.",
                    peinadores, TipoEspacio.HOGAR, "260.00", true,
                    "MDF lacado blanco, banqueta tapizada en tela.",
                    "1.00m x 0.40m x 1.45m",
                    "https://images.unsplash.com/photo-1595514535310-82d87e0bd8fb?q=80&w=800"));


            productoRepository.save(producto(
                    "Camilla Fija para Consultorio",
                    "Camilla de examen con cabecera reclinable y portarrollos para papel.",
                    camillas, TipoEspacio.CONSULTORIO, "240.00", true,
                    "Estructura metálica con pintura electrostática, colchoneta en semicuero.",
                    "1.85m x 0.65m x 0.75m",
                    "https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?q=80&w=800"));

            productoRepository.save(producto(
                    "Camilla de Masajes Plegable",
                    "Camilla portátil con apoyacabeza ajustable y bolso de transporte.",
                    camillas, TipoEspacio.SALON_BELLEZA, "180.00", true,
                    "Estructura de madera, colchoneta de alta densidad en semicuero.",
                    "1.85m x 0.70m x 0.62m–0.85m",
                    "https://images.unsplash.com/photo-1544161515-4ab6ce6db874?q=80&w=800"));

            productoRepository.save(producto(
                    "Camilla Estética con Gavetero",
                    "Camilla para tratamientos faciales y corporales con gavetero inferior para insumos.",
                    camillas, TipoEspacio.SALON_BELLEZA, "420.00", false,
                    "Base en melamina 18mm, colchoneta tapizada en semicuero.",
                    "1.90m x 0.70m x 0.75m",
                    "https://images.unsplash.com/photo-1600334089648-b0d9d3028eb2?q=80&w=800"));

            System.out.println("✅ Datos semilla cargados: 5 categorías y 15 productos.");
        };
    }





    private Categoria categoria(String nombre, String slug, String descripcion) {
        return Categoria.builder()
                .nombre(nombre)
                .slug(slug)
                .descripcion(descripcion)
                .build();
    }

    private Producto producto(String nombre, String descripcion, Categoria categoria,
                              TipoEspacio tipoEspacio, String precioUsd, boolean enShowroom,
                              String material, String dimensiones, String... imagenesUrl) {
        Producto producto = Producto.builder()
                .nombre(nombre)
                .descripcion(descripcion)
                .categoria(categoria)
                .tipoEspacio(tipoEspacio)
                .precioReferencialUsd(new BigDecimal(precioUsd))
                .enShowroom(enShowroom)
                .material(material)
                .dimensionesEstandar(dimensiones)
                .build();

        for (String url : imagenesUrl) {

            producto.agregarImagen(ProductoImagen.builder()
                    .urlImagen(url)
                    .build());
        }
        return producto;
    }
}