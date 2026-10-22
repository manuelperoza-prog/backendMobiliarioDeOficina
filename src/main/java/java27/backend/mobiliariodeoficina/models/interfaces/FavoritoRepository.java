package java27.backend.mobiliariodeoficina.models.interfaces;

import java27.backend.mobiliariodeoficina.models.Favorito;
import java27.backend.mobiliariodeoficina.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    // Para el GET /api/v1/favoritos
    List<Favorito> findByUser(User user);

    // Para el POST y DELETE /api/v1/favoritos/{productoId}
    Optional<Favorito> findByUserAndProductoId(User user, Long productoId);
}