package java27.backend.mobiliariodeoficina.service;

import java27.backend.mobiliariodeoficina.models.Favorito;
import java27.backend.mobiliariodeoficina.models.Producto;
import java27.backend.mobiliariodeoficina.models.user.User;
import java27.backend.mobiliariodeoficina.models.FavoritoRepository;
import java27.backend.mobiliariodeoficina.models.ProductoRepository;
import java27.backend.mobiliariodeoficina.models.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final ProductoRepository productoRepository;
    private final UserRepository userRepository;

    // Método centralizado para buscar al usuario por username o email (Evita el error 401)
    private User obtenerUsuarioLogueado(String identificador) {
        return userRepository.findByUsername(identificador)
                .or(() -> userRepository.findByEmail(identificador))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));
    }

    public List<Long> obtenerIdsFavoritos(String identificador) {
        User user = obtenerUsuarioLogueado(identificador);

        return favoritoRepository.findByUser(user)
                .stream()
                .map(fav -> fav.getProducto().getId())
                .toList(); // Retorna un array limpio: [1, 4, 7]
    }

    @Transactional
    public void agregarFavorito(String identificador, Long productoId) {
        User user = obtenerUsuarioLogueado(identificador);

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto no existe."));

        Optional<Favorito> existente = favoritoRepository.findByUserAndProductoId(user, productoId);

        // Solo guardamos si no existe previamente (cumpliendo con la idempotencia)
        if (existente.isEmpty()) {
            favoritoRepository.save(new Favorito(user, producto));
        }
    }

    @Transactional
    public void eliminarFavorito(String identificador, Long productoId) {
        User user = obtenerUsuarioLogueado(identificador);

        if (!productoRepository.existsById(productoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto no existe.");
        }

        // Si existe en favoritos, lo elimina; si no, no hace nada y el controlador devolverá 204 igual
        favoritoRepository.findByUserAndProductoId(user, productoId)
                .ifPresent(favoritoRepository::delete);
    }
}