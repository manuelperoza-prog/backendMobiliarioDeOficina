package java27.backend.mobiliariodeoficina.auth;

import java27.backend.mobiliariodeoficina.models.dto.PerfilResponse;
import java27.backend.mobiliariodeoficina.models.User;
import java27.backend.mobiliariodeoficina.models.interfaces.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java27.backend.mobiliariodeoficina.models.dto.AuthResponse;
import java27.backend.mobiliariodeoficina.models.dto.GoogleLoginRequest;
import java27.backend.mobiliariodeoficina.models.dto.LoginRequest;
import java27.backend.mobiliariodeoficina.models.dto.RegisterRequest;
import java27.backend.mobiliariodeoficina.service.AuthGoogleService;
import java27.backend.mobiliariodeoficina.service.AuthService;

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
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        return ResponseEntity.ok(new PerfilResponse(
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        ));
    }
}
