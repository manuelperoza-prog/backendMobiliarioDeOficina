package java27.backend.mobiliariodeoficina.auth;

import java27.backend.mobiliariodeoficina.models.dto.PerfilResponse;
import java27.backend.mobiliariodeoficina.models.user.User;
import java27.backend.mobiliariodeoficina.models.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java27.backend.mobiliariodeoficina.auth.DTO.AuthResponse;
import java27.backend.mobiliariodeoficina.auth.DTO.GoogleLoginRequest;
import java27.backend.mobiliariodeoficina.auth.DTO.LoginRequest;
import java27.backend.mobiliariodeoficina.auth.DTO.RegisterRequest;
import java27.backend.mobiliariodeoficina.auth.service.AuthGoogleService;
import java27.backend.mobiliariodeoficina.auth.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController 
@RequiredArgsConstructor
@RequestMapping("/auth/v1")
public class AuthController {

    UserRepository userRepository;
    private final AuthService authService;
    private final AuthGoogleService authGService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
    

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterRequest request) {
        return ResponseEntity.created(null).body(authService.register(request));
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleUser(@RequestBody GoogleLoginRequest request) {
        return ResponseEntity.ok(authGService.enter(request));
    }


    @GetMapping("/me")
    public ResponseEntity<PerfilResponse> obtenerMiPerfil(Principal principal) {
        // Busca el usuario en la BD usando el email extraído del Token
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Retornas los datos limpios al frontend
        return ResponseEntity.ok(new PerfilResponse(
                user.getUsername(), // Ajusta según las propiedades exactas de tu clase User
                user.getEmail(),
                user.getRole().name()
        ));
    }
}
