package java27.backend.mobiliariodeoficina.models;

import jakarta.persistence.*;
import java27.backend.mobiliariodeoficina.models.enums.EstadoCotizacion;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "cotizaciones")
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigoCotizacion; // Ej: COT-0001

    @Column(nullable = false)
    private String nombreCliente;

    private String empresa;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(nullable = false)
    private Double totalUsd;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCotizacion estado;

    // Relación uno a muchos con los detalles. Si se borra la cotización, se borran sus detalles.
    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCotizacion> detalles = new ArrayList<>();

    // Puedes generar los Getters y Setters con tu IDE, o usar @Data si tienes Lombok
    public void addDetalle(DetalleCotizacion detalle) {
        detalles.add(detalle);
        detalle.setCotizacion(this);
    }

    // --- GETTERS Y SETTERS AQUÍ ---
}