package com.pragma.powerup.trazabilidad.infraestructure.output.mongo.adapter;

import com.pragma.powerup.trazabilidad.application.dto.RankingEficienciaEmpleadoDto;
import com.pragma.powerup.trazabilidad.application.dto.TiempoAtencionPorPedidoDto;
import com.pragma.powerup.trazabilidad.domain.model.HistorialEstado;
import com.pragma.powerup.trazabilidad.domain.spi.HistorialEstadoPersistencePort;
import com.pragma.powerup.trazabilidad.infraestructure.output.mongo.document.HistorialEstadoDocument;
import com.pragma.powerup.trazabilidad.infraestructure.output.mongo.repository.HistorialEstadoRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HistorialEstadoMongoAdapter implements HistorialEstadoPersistencePort {

    private final HistorialEstadoRepository historialEstadoRepository;

    public HistorialEstadoMongoAdapter(HistorialEstadoRepository historialEstadoRepository) {
        this.historialEstadoRepository = historialEstadoRepository;
    }

    @Override
    public void guardarHistorial(HistorialEstado historial) {
        HistorialEstadoDocument document = new HistorialEstadoDocument();
        document.setIdPedido(historial.getIdPedido());
        document.setIdCliente(historial.getIdCliente());
        document.setEstadoNuevo(historial.getEstado());
        document.setIdEmpleado(historial.getIdEmpleado());
        document.setFechaCambio(LocalDateTime.now());
        historialEstadoRepository.save(document);
    }

    @Override
    public List<HistorialEstado> obtenerPorPedidoYCliente(Long idPedido, Long idCliente) {
        return historialEstadoRepository.findByIdPedidoAndIdCliente(idPedido, idCliente).stream()
                .map(doc -> {
                    HistorialEstado historial = new HistorialEstado();
                    historial.setId(doc.getId());
                    historial.setIdPedido(doc.getIdPedido());
                    historial.setIdCliente(doc.getIdCliente());
                    historial.setEstado(doc.getEstadoNuevo());
                    historial.setFechaCambio(Date.from(doc.getFechaCambio()
                            .atZone(ZoneId.systemDefault()).toInstant()));
                    return historial;
                }).toList();
    }


    @Override
    public List<HistorialEstado> findByEstadoNuevo(String estado) {
        return historialEstadoRepository.findByEstadoNuevo(estado).stream()
                .map(doc -> {
                    HistorialEstado historial = new HistorialEstado();
                    historial.setId(doc.getId());
                    historial.setIdPedido(doc.getIdPedido());
                    historial.setIdCliente(doc.getIdCliente());
                    historial.setEstado(doc.getEstadoNuevo());
                    historial.setFechaCambio(Date.from(doc.getFechaCambio().atZone(ZoneId.systemDefault()).toInstant()));
                    historial.setIdEmpleado(doc.getIdEmpleado());
                    return historial;
                })
                .toList();
    }

}