package java27.backend.mobiliariodeoficina.models;

import java27.backend.mobiliariodeoficina.models.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "citas_showroom")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaShowroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_cita", nullable = false, unique = true, length = 30)
    private String codigoCita;

    // Relación directa: Muchas citas pueden pertenecer a un mismo Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Column(length = 150)
    private String empresa;

    @Column(nullable = false, length = 30)
    private String telefono; // Lo mantenemos porque en User no tienes teléfono

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_asesoria", nullable = false, length = 40)
    private TipoAsesoria tipoAsesoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private EstadoCita estado = EstadoCita.PENDIENTE;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    public void prePersist() {
        if (this.creadoEn == null) {
            this.creadoEn = LocalDateTime.now();
        }
        if (this.estado == null) {
            this.estado = EstadoCita.PENDIENTE;
        }
    }
}