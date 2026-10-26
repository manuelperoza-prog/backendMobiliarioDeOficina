package java27.backend.mobiliariodeoficina.service;

import java27.backend.mobiliariodeoficina.models.Favorito;
import java27.backend.mobiliariodeoficina.models.Producto;
import java27.backend.mobiliariodeoficina.models.User;
import java27.backend.mobiliariodeoficina.models.interfaces.FavoritoRepository;
import java27.backend.mobiliariodeoficina.models.interfaces.ProductoRepository;
import java27.backend.mobiliariodeoficina.models.interfaces.UserRepository;
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
                .toList();
    }

    @Transactional
    public void agregarFavorito(String identificador, Long productoId) {
        User user = obtenerUsuarioLogueado(identificador);

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto no existe."));

        Optional<Favorito> existente = favoritoRepository.findByUserAndProductoId(user, productoId);


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


        favoritoRepository.findByUserAndProductoId(user, productoId)
                .ifPresent(favoritoRepository::delete);
    }
}