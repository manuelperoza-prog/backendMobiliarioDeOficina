package java27.backend.mobiliariodeoficina.models.interfaces;

import java27.backend.mobiliariodeoficina.models.Producto;
import java27.backend.mobiliariodeoficina.models.enums.TipoEspacio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {


    List<Producto> findByEnShowroomTrue();


    List<Producto> findByCategoriaSlug(String slug);


    List<Producto> findByTipoEspacio(TipoEspacio tipoEspacio);


    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Producto> buscarPorNombre(@Param("termino") String termino);

    boolean existsByCategoriaId(Long idCategoria);
}