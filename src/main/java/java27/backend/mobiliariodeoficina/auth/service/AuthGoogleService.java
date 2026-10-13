package java27.backend.mobiliariodeoficina.auth.service;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import java27.backend.mobiliariodeoficina.auth.DTO.AuthResponse;
import java27.backend.mobiliariodeoficina.auth.DTO.GoogleLoginRequest;
import java27.backend.mobiliariodeoficina.jwt.GoogleTokenVerifierService;
import java27.backend.mobiliariodeoficina.jwt.JwtService;
import java27.backend.mobiliariodeoficina.models.user.Role;
import java27.backend.mobiliariodeoficina.models.user.User;
import java27.backend.mobiliariodeoficina.models.user.UserRepository;
import lombok.RequiredArgsConstructor;


@Service 
@RequiredArgsConstructor 
public class AuthGoogleService {

    private final GoogleTokenVerifierService service;
    private final UserRepository repo;
    private final JwtService serv;
    private final PasswordEncoder encoder;

    public AuthResponse enter(GoogleLoginRequest request){
        GoogleIdToken.Payload payload = service.verifyToken(request.getToken());

        var user = repo.findByEmail(payload.getEmail());

        if (user.isPresent()) {
        UserDetails userD = user.get();

        return AuthResponse.builder()
                .token(serv.getToken(userD)) //
                .build();
    } else {
        User newUser = User.builder()
            .username(payload.getEmail())
            .password(encoder.encode(UUID.randomUUID().toString()))
            .email(payload.getEmail())
            .role(Role.USER)
            .build();

        repo.save(newUser);

        return AuthResponse.builder()
            .token(serv.getToken(newUser)).build();
        }
    }
}
