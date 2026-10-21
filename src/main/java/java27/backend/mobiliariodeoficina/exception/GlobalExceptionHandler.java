package java27.backend.mobiliariodeoficina.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Tu manejador actual para los tokens/credenciales
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalidToken(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales invalidas " + ex.getMessage());
    }

    // El NUEVO manejador para evitar el 401 cuando el JSON (o Enum) viene mal escrito
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonErrors(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Bad Request");
        error.put("mensaje", "Error en el formato de los datos enviados. Verifica que los valores (como el tipoAsesoria o la fecha) estén escritos correctamente.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}