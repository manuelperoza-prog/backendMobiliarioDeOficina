package java27.backend.mobiliariodeoficina.models.interfaces;

import java27.backend.mobiliariodeoficina.models.CitaShowroom;
import java27.backend.mobiliariodeoficina.models.enums.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaShowroomRepository extends JpaRepository<CitaShowroom, Long> {


    Optional<CitaShowroom> findByCodigoCita(String codigoCita);


    List<CitaShowroom> findByEstado(EstadoCita estado);


    List<CitaShowroom> findByFechaHoraAfterOrderByFechaHoraAsc(LocalDateTime fecha);

    List<CitaShowroom> findByUsuarioEmailOrderByFechaHoraDesc(String email);
}