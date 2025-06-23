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
    private static final String ESTADO_PENDIENTE = "PENDIENTE";
    private static final String ESTADO_ENTREGADO = "ENTREGADO";

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
    public List<TiempoAtencionPorPedidoDto> obtenerTiemposPorPedido(Long idRestaurante) {
        List<HistorialEstadoDocument> pendientes = historialEstadoRepository.findByEstadoNuevo(ESTADO_PENDIENTE);
        List<HistorialEstadoDocument> entregados = historialEstadoRepository.findByEstadoNuevo(ESTADO_ENTREGADO);

        Map<Long, LocalDateTime> pendientesMap = pendientes.stream()
                .collect(Collectors.toMap(HistorialEstadoDocument::getIdPedido, HistorialEstadoDocument::getFechaCambio));

        return entregados.stream()
                .filter(ent -> pendientesMap.containsKey(ent.getIdPedido()))
                .map(ent -> {
                    long minutos = Duration.between(pendientesMap.get(ent.getIdPedido()), ent.getFechaCambio()).toMinutes();
                    return new TiempoAtencionPorPedidoDto(
                            ent.getIdPedido(),
                            ent.getIdEmpleado(),
                            ent.getIdCliente(),
                            minutos
                    );
                }).toList();
    }

    @Override
    public List<RankingEficienciaEmpleadoDto> obtenerRankingPorEmpleado(Long idRestaurante) {
        List<HistorialEstadoDocument> pendientes = historialEstadoRepository.findByEstadoNuevo(ESTADO_PENDIENTE);
        List<HistorialEstadoDocument> entregados = historialEstadoRepository.findByEstadoNuevo(ESTADO_ENTREGADO);

        Map<Long, LocalDateTime> pendientesMap = pendientes.stream()
                .filter(p -> p.getIdPedido() != null && p.getFechaCambio() != null)
                .collect(Collectors.toMap(HistorialEstadoDocument::getIdPedido, HistorialEstadoDocument::getFechaCambio));

        // Map de empleado a lista de duraciones
        Map<Long, List<Long>> tiemposPorEmpleado = entregados.stream()
                .filter(ent -> pendientesMap.containsKey(ent.getIdPedido()) && ent.getIdEmpleado() != null)
                .collect(Collectors.groupingBy(
                        HistorialEstadoDocument::getIdEmpleado,
                        Collectors.mapping(ent -> Duration.between(
                                        pendientesMap.get(ent.getIdPedido()),
                                        ent.getFechaCambio()).toMinutes(),
                                Collectors.toList()
                        )
                ));

        // Calcular promedio por empleado
        return tiemposPorEmpleado.entrySet().stream()
                .map(entry -> {
                    Long idEmpleado = entry.getKey();
                    List<Long> tiempos = entry.getValue();
                    double promedio = tiempos.stream().mapToLong(Long::longValue).average().orElse(0.0);
                    return new RankingEficienciaEmpleadoDto(idEmpleado, promedio);
                })
                .sorted(Comparator.comparing(RankingEficienciaEmpleadoDto::getPromedioMinutos))
                .toList();

    }
}