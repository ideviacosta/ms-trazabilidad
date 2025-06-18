package com.pragma.powerup.trazabilidad.domain.spi;


import com.pragma.powerup.trazabilidad.domain.model.HistorialEstado;

import java.util.List;

public interface HistorialEstadoPersistencePort {
    void guardarHistorial(HistorialEstado historial);
    List<HistorialEstado> obtenerPorPedidoYCliente(Long idPedido, Long idCliente);
}