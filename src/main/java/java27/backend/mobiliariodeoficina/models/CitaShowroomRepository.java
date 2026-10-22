package java27.backend.mobiliariodeoficina.models;

import java27.backend.mobiliariodeoficina.models.CitaShowroom;
import java27.backend.mobiliariodeoficina.models.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaShowroomRepository extends JpaRepository<CitaShowroom, Long> {

    // Para buscar una cita específica por su código (ej. CITA-2026-0042)
    Optional<CitaShowroom> findByCodigoCita(String codigoCita);

    // Para que el administrador filtre citas pendientes, confirmadas, etc.
    List<CitaShowroom> findByEstado(EstadoCita estado);

    // Para ver las citas programadas a partir de hoy
    List<CitaShowroom> findByFechaHoraAfterOrderByFechaHoraAsc(LocalDateTime fecha);

    List<CitaShowroom> findByEmailOrderByFechaCreacionDesc(String email);
}