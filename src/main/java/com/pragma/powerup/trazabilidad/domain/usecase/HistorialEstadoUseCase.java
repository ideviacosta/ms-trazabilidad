package com.pragma.powerup.trazabilidad.domain.usecase;

import com.pragma.powerup.trazabilidad.application.dto.RankingEficienciaEmpleadoDto;
import com.pragma.powerup.trazabilidad.application.dto.TiempoAtencionPorPedidoDto;
import com.pragma.powerup.trazabilidad.domain.api.HistorialEstadoServicePort;
import com.pragma.powerup.trazabilidad.domain.model.HistorialEstado;
import com.pragma.powerup.trazabilidad.domain.spi.HistorialEstadoPersistencePort;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HistorialEstadoUseCase implements HistorialEstadoServicePort {

    private final HistorialEstadoPersistencePort historialEstadoPersistencePort;
    private static final String ESTADO_PENDIENTE = "PENDIENTE";
    private static final String ESTADO_ENTREGADO = "ENTREGADO";

    public HistorialEstadoUseCase(HistorialEstadoPersistencePort historialEstadoPersistencePort) {
        this.historialEstadoPersistencePort = historialEstadoPersistencePort;
    }

    @Override
    public void guardarHistorial(HistorialEstado historial) {
        historialEstadoPersistencePort.guardarHistorial(historial);
    }

    @Override
    public List<HistorialEstado> obtenerHistorial(Long idPedido, Long idCliente) {
        return historialEstadoPersistencePort.obtenerPorPedidoYCliente(idPedido, idCliente);
    }

    @Override
    public List<TiempoAtencionPorPedidoDto> obtenerTiemposPorPedido(Long idRestaurante) {
        List<HistorialEstado> pendientes = historialEstadoPersistencePort.findByEstadoNuevo(ESTADO_PENDIENTE);
        List<HistorialEstado> entregados = historialEstadoPersistencePort.findByEstadoNuevo(ESTADO_ENTREGADO);

        Map<Long, LocalDateTime> pendientesMap = pendientes.stream()
                .collect(Collectors.toMap(HistorialEstado::getIdPedido,
                        h -> h.getFechaCambio().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        (existing, replacement) -> existing)); // Resolver duplicados si existen

        return entregados.stream()
                .filter(ent -> pendientesMap.containsKey(ent.getIdPedido()))
                .map(ent -> {
                    long minutos = Duration.between(pendientesMap.get(ent.getIdPedido()),
                            ent.getFechaCambio().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).toMinutes();
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
        List<HistorialEstado> pendientes = historialEstadoPersistencePort.findByEstadoNuevo(ESTADO_PENDIENTE);
        List<HistorialEstado> entregados = historialEstadoPersistencePort.findByEstadoNuevo(ESTADO_ENTREGADO);

        Map<Long, LocalDateTime> pendientesMap = pendientes.stream()
                .filter(p -> p.getIdPedido() != null && p.getFechaCambio() != null)
                .collect(Collectors.toMap(HistorialEstado::getIdPedido,
                        h -> h.getFechaCambio().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        (existing, replacement) -> existing)); // Resolver duplicados si existen

        Map<Long, List<Long>> tiemposPorEmpleado = entregados.stream()
                .filter(ent -> pendientesMap.containsKey(ent.getIdPedido()) && ent.getIdEmpleado() != null)
                .collect(Collectors.groupingBy(
                        HistorialEstado::getIdEmpleado,
                        Collectors.mapping(ent -> Duration.between(
                                        pendientesMap.get(ent.getIdPedido()),
                                        ent.getFechaCambio().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).toMinutes(),
                                Collectors.toList()
                        )
                ));

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