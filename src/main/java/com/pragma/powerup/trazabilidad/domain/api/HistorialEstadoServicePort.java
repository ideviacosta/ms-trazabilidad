package com.pragma.powerup.trazabilidad.domain.api;

import com.pragma.powerup.trazabilidad.application.dto.RankingEficienciaEmpleadoDto;
import com.pragma.powerup.trazabilidad.application.dto.TiempoAtencionPorPedidoDto;
import com.pragma.powerup.trazabilidad.domain.model.HistorialEstado;

import java.util.List;

public interface HistorialEstadoServicePort {
    List<HistorialEstado> obtenerHistorial(Long idPedido, Long idCliente);
    void guardarHistorial(HistorialEstado historial);
    List<TiempoAtencionPorPedidoDto> obtenerTiemposPorPedido(Long idRestaurante);
    List<RankingEficienciaEmpleadoDto> obtenerRankingPorEmpleado(Long idRestaurante);
}