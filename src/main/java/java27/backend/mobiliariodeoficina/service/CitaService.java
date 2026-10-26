package java27.backend.mobiliariodeoficina.service;

import java27.backend.mobiliariodeoficina.models.enums.EstadoCita;
import java27.backend.mobiliariodeoficina.models.dto.CitaRequest;
import java27.backend.mobiliariodeoficina.models.dto.CitaResponse;
import java27.backend.mobiliariodeoficina.models.CitaShowroom;
import java27.backend.mobiliariodeoficina.models.enums.TipoAsesoria;
import java27.backend.mobiliariodeoficina.models.User;
import java27.backend.mobiliariodeoficina.models.interfaces.UserRepository;
import java27.backend.mobiliariodeoficina.models.interfaces.CitaShowroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaShowroomRepository citaRepository;
    private final UserRepository userRepository;

    @Transactional
    public String agendarCita(CitaRequest dto) {
        String usernameLogueado = SecurityContextHolder.getContext().getAuthentication().getName();

        User usuario = userRepository.findByUsername(usernameLogueado)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String codigoUnico = "CITA-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        CitaShowroom nuevaCita = CitaShowroom.builder()
                .codigoCita(codigoUnico)
                .usuario(usuario)
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

    private CitaShowroom obtenerCitaValidada(String codigoCita) {
        String identificadorLogueado = SecurityContextHolder.getContext().getAuthentication().getName();
        User usuario = userRepository.findByUsername(identificadorLogueado)
                .or(() -> userRepository.findByEmail(identificadorLogueado))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        CitaShowroom cita = citaRepository.findByCodigoCita(codigoCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada: " + codigoCita));

        if (!cita.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Acceso denegado: No puedes gestionar citas de otros clientes");
        }

        if (cita.getEstado() == EstadoCita.COMPLETADA || cita.getEstado() == EstadoCita.CANCELADA) {
            throw new RuntimeException("Operación no permitida: La cita ya está " + cita.getEstado().name());
        }

        return cita;
    }

    public List<CitaResponse> obtenerMisCitas(String email) {
        return citaRepository.findByUsuarioEmailOrderByFechaHoraDesc(email)
                .stream()
                .map(this::mapearADTO)
                .toList();
    }

    public List<CitaResponse> obtenerTodasLasCitas() {
        return citaRepository.findAll()
                .stream()
                .map(this::mapearADTO)
                .toList();
    }

    @Transactional
    public void actualizarEstadoCita(String codigoCita, EstadoCita nuevoEstado) {
        CitaShowroom cita = citaRepository.findByCodigoCita(codigoCita)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada: " + codigoCita));

        cita.setEstado(nuevoEstado);
        citaRepository.save(cita);
    }

    private CitaResponse mapearADTO(CitaShowroom cita) {
        String nombreCliente = cita.getUsuario() != null ?
                cita.getUsuario().getUsername() : "Cliente Desconocido";
        String emailUsuario = cita.getUsuario() != null ?
                cita.getUsuario().getEmail() : "Sin email";

        return CitaResponse.builder()
                .codigoCita(cita.getCodigoCita())
                .nombreCliente(nombreCliente)
                .email(emailUsuario)
                .empresa(cita.getEmpresa())
                .telefono(cita.getTelefono())
                .fechaHora(cita.getFechaHora())
                .tipoAsesoria(cita.getTipoAsesoria() != null ? cita.getTipoAsesoria().name() : null)
                .estado(cita.getEstado())
                .notas(cita.getNotas())
                .build();
    }
}