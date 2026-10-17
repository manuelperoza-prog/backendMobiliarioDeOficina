package java27.backend.mobiliariodeoficina.service;

import java27.backend.mobiliariodeoficina.models.dto.CitaRequest;
import java27.backend.mobiliariodeoficina.models.CitaShowroom;
import java27.backend.mobiliariodeoficina.models.TipoAsesoria;
import java27.backend.mobiliariodeoficina.models.CitaShowroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaShowroomRepository citaRepository;

    @Transactional
    public String agendarCita(CitaRequest dto) {
        // Genera un código único alfanumérico de 6 caracteres (ej. CITA-A1B2C3)
        String codigoUnico = "CITA-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        CitaShowroom nuevaCita = CitaShowroom.builder()
                .codigoCita(codigoUnico)
                .nombreCliente(dto.getNombreCliente())
                .empresa(dto.getEmpresa())
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .fechaHora(dto.getFechaHora())
                .tipoAsesoria(TipoAsesoria.valueOf(dto.getTipoAsesoria().toUpperCase()))
                .notas(dto.getNotas())
                // El estado inicial (PENDIENTE) y la fecha de creación se gestionan solos en la Entidad (@PrePersist)
                .build();

        citaRepository.save(nuevaCita);

        // Devolvemos el código para que el frontend lance el modal: "Su cita CITA-XXXX ha sido agendada"
        return codigoUnico;
    }
}