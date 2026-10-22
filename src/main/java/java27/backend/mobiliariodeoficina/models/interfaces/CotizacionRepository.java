package java27.backend.mobiliariodeoficina.models.interfaces;

import java27.backend.mobiliariodeoficina.models.Cotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {
    long count();
    List<Cotizacion> findByEmailOrderByFechaCreacionDesc(String email);

    Optional<Cotizacion> findByCodigoCotizacion(String codigo);
}