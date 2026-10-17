package java27.backend.mobiliariodeoficina.service;

import java27.backend.mobiliariodeoficina.models.dto.CitaRequest;
import java27.backend.mobiliariodeoficina.models.CitaShowroom;
import java27.backend.mobiliariodeoficina.models.TipoAsesoria;
import java27.backend.mobiliariodeoficina.models.user.User;
import java27.backend.mobiliariodeoficina.models.user.UserRepository;
import java27.backend.mobiliariodeoficina.models.CitaShowroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaShowroomRepository citaRepository;
    private final UserRepository userRepository;

    @Transactional
    public String agendarCita(CitaRequest dto) {
        // 1. Obtener el username del token JWT activo
        String usernameLogueado = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Buscar al usuario en la base de datos
        User usuario = userRepository.findByUsername(usernameLogueado)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. Generar el código único
        String codigoUnico = "CITA-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        // 4. Crear la cita amarrada al usuario
        CitaShowroom nuevaCita = CitaShowroom.builder()
                .codigoCita(codigoUnico)
                .usuario(usuario) // <-- ¡Aquí enlazamos la cita con la cuenta del cliente!
                .empresa(dto.getEmpresa())
                .telefono(dto.getTelefono())
                .fechaHora(dto.getFechaHora())
                .tipoAsesoria(TipoAsesoria.valueOf(dto.getTipoAsesoria().toUpperCase()))
                .notas(dto.getNotas())
                .build();

        citaRepository.save(nuevaCita);

        return codigoUnico;
    }
}