package java27.backend.mobiliariodeoficina.jwt;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

@Service
public class GoogleTokenVerifierService {


    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;





    public GoogleIdToken.Payload verifyToken(String idTokenString) {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())

                .setAudience(Collections.singletonList(clientId))

                .build();

        try {

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();






                return payload;
            } else {
                System.out.println("El token es inválido, el 'aud' no coincide, o ha expirado.");
                throw new IllegalArgumentException("El token es inválido, el 'aud' no coincide, o ha expirado.");
            }

        } catch (Exception e) {
            System.out.println("Error de seguridad o red al validar el token: " + e.getMessage());
            throw new IllegalArgumentException("Error de seguridad o red al validar el token: " + e.getMessage());
        }
    }
}