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

    // Filtro: Muebles que están físicamente en el showroom de San Diego
    List<Producto> findByEnShowroomTrue();

    // Filtro: Productos por categoría usando el slug de la URL
    List<Producto> findByCategoriaSlug(String slug);

    // Filtro: Productos por el tipo de espacio (Gerencial, Operativo, etc.)
    List<Producto> findByTipoEspacio(TipoEspacio tipoEspacio);

    // Buscador de texto libre para el navbar del frontend
    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Producto> buscarPorNombre(@Param("termino") String termino);
}