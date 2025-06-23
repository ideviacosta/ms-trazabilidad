package com.pragma.powerup.trazabilidad.application.usecase;

import com.pragma.powerup.trazabilidad.application.dto.RankingEficienciaEmpleadoDto;
import com.pragma.powerup.trazabilidad.application.dto.TiempoAtencionPorPedidoDto;
import com.pragma.powerup.trazabilidad.domain.api.HistorialEstadoServicePort;
import com.pragma.powerup.trazabilidad.domain.model.HistorialEstado;
import com.pragma.powerup.trazabilidad.domain.spi.HistorialEstadoPersistencePort;

import java.util.List;

public class HistorialEstadoUseCase implements HistorialEstadoServicePort {

    private final HistorialEstadoPersistencePort historialEstadoPersistencePort;

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
        return historialEstadoPersistencePort.obtenerTiemposPorPedido(idRestaurante);
    }

    @Override
    public List<RankingEficienciaEmpleadoDto> obtenerRankingPorEmpleado(Long idRestaurante) {
        return historialEstadoPersistencePort.obtenerRankingPorEmpleado(idRestaurante);
    }

}