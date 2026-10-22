package java27.backend.mobiliariodeoficina.models;

import java27.backend.mobiliariodeoficina.models.Cotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {
    long count();
    List<Cotizacion> findByEmailOrderByFechaCreacionDesc(String email);
}