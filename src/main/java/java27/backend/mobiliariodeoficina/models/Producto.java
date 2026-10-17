package java27.backend.mobiliariodeoficina.models;

import java27.backend.mobiliariodeoficina.models.TipoEspacio;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_espacio", nullable = false, length = 30)
    private TipoEspacio tipoEspacio;

    @Column(name = "precio_referencial_usd", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioReferencialUsd;

    @Column(name = "en_showroom", nullable = false)
    @Builder.Default
    private Boolean enShowroom = true;

    @Column(columnDefinition = "TEXT")
    private String material;

    @Column(name = "dimensiones_estandar", length = 120)
    private String dimensionesEstandar;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<ProductoImagen> imagenes = new ArrayList<>();

    // Método helper para vincular imágenes limpiamente
    public void agregarImagen(ProductoImagen imagen) {
        imagenes.add(imagen);
        imagen.setProducto(this);
    }

    public void eliminarImagen(ProductoImagen imagen) {
        imagenes.remove(imagen);
        imagen.setProducto(null);
    }
}