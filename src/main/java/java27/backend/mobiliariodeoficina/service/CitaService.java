package java27.backend.mobiliariodeoficina.service;

import java27.backend.mobiliariodeoficina.models.EstadoCita;
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

import java.util.List;
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

    @Transactional
    public void eliminarCita(String codigoCita) {
        CitaShowroom cita = obtenerCitaValidada(codigoCita);
        citaRepository.delete(cita);
    }

    @Transactional
    public String modificarCita(String codigoCita, CitaRequest dto) {
        CitaShowroom cita = obtenerCitaValidada(codigoCita);

        cita.setEmpresa(dto.getEmpresa());
        cita.setTelefono(dto.getTelefono());
        cita.setFechaHora(dto.getFechaHora());
        cita.setTipoAsesoria(TipoAsesoria.valueOf(dto.getTipoAsesoria().toUpperCase()));
        cita.setNotas(dto.getNotas());

        citaRepository.save(cita);
        return cita.getCodigoCita();
    }

    // Método centralizado de seguridad y reglas de negocio
    private CitaShowroom obtenerCitaValidada(String codigoCita) {
        String identificadorLogueado = SecurityContextHolder.getContext().getAuthentication().getName();
        User usuario = userRepository.findByUsername(identificadorLogueado)
                .or(() -> userRepository.findByEmail(identificadorLogueado))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        CitaShowroom cita = citaRepository.findByCodigoCita(codigoCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada: " + codigoCita));

        // 1. Regla de Seguridad: La cita debe ser de este cliente
        if (!cita.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Acceso denegado: No puedes gestionar citas de otros clientes");
        }

        // 2. Regla de Negocio: Solo se edita/borra si está Pendiente o Confirmada
        if (cita.getEstado() == EstadoCita.COMPLETADA || cita.getEstado() == EstadoCita.CANCELADA) {
            throw new RuntimeException("Operación no permitida: La cita ya está " + cita.getEstado().name());
        }

        return cita;
    }

    public List<CitaShowroom> obtenerMisCitas(String email) {
        return citaRepository.findByUsuarioEmailOrderByFechaHoraDesc(email);
    }
}