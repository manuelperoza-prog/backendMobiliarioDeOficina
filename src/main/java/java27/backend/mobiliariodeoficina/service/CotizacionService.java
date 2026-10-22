package java27.backend.mobiliariodeoficina.service;

import java27.backend.mobiliariodeoficina.models.dto.CotizacionRequest;
import java27.backend.mobiliariodeoficina.models.dto.CotizacionResponse;
import java27.backend.mobiliariodeoficina.models.dto.ItemCotizacion;
import java27.backend.mobiliariodeoficina.models.Cotizacion;
import java27.backend.mobiliariodeoficina.models.DetalleCotizacion;
import java27.backend.mobiliariodeoficina.models.Producto;
import java27.backend.mobiliariodeoficina.models.EstadoCotizacion;
import java27.backend.mobiliariodeoficina.models.CotizacionRepository;
import java27.backend.mobiliariodeoficina.models.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class CotizacionService {

    @Autowired
    private CotizacionRepository cotizacionRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    public CotizacionResponse crearCotizacion(CotizacionRequest request) {
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setNombreCliente(request.getNombreCliente());
        cotizacion.setEmpresa(request.getEmpresa());
        cotizacion.setTelefono(request.getTelefono());
        cotizacion.setEmail(request.getEmail());
        cotizacion.setNotas(request.getNotas());

        // Asignamos la hora de Caracas y el estado por defecto
        cotizacion.setFechaCreacion(LocalDateTime.now(ZoneId.of("America/Caracas")));
        cotizacion.setEstado(EstadoCotizacion.PENDIENTE);


        long contador = cotizacionRepository.count() + 1;
        String codigo = String.format("COT-%04d", contador);
        cotizacion.setCodigoCotizacion(codigo);

        double total = 0.0;

        // Recorremos los ítems que mandó el frontend
        for (ItemCotizacion item : request.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "El producto con ID " + item.getProductoId() + " no existe."));

            DetalleCotizacion detalle = new DetalleCotizacion();
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecioReferencialUsd().doubleValue());

            cotizacion.addDetalle(detalle);
            total += (producto.getPrecioReferencialUsd().doubleValue() * item.getCantidad());
        }

        cotizacion.setTotalUsd(total);
        cotizacionRepository.save(cotizacion);

        return new CotizacionResponse(codigo);
    }

    public List<Cotizacion> obtenerTodas() {
        return cotizacionRepository.findAll();
    }

    @Transactional
    public Cotizacion actualizarEstado(Long id, EstadoCotizacion nuevoEstado) {
        Cotizacion cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cotización no encontrada con el ID: " + id));

        cotizacion.setEstado(nuevoEstado);
        return cotizacionRepository.save(cotizacion);
    }
}