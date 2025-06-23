package com.pragma.powerup.trazabilidad.infraestructure.input.rest;

import com.pragma.powerup.trazabilidad.application.dto.RankingEficienciaEmpleadoDto;
import com.pragma.powerup.trazabilidad.application.dto.TiempoAtencionPorPedidoDto;
import com.pragma.powerup.trazabilidad.domain.api.HistorialEstadoServicePort;
import com.pragma.powerup.trazabilidad.domain.model.HistorialEstado;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trazabilidad")
public class HistorialEstadoRestController {

    private final HistorialEstadoServicePort historialEstadoServicePort;

    public HistorialEstadoRestController(HistorialEstadoServicePort historialEstadoServicePort) {
        this.historialEstadoServicePort = historialEstadoServicePort;
    }

    @GetMapping("/historial")
    public List<HistorialEstado> consultarHistorial(
            @RequestParam Long idPedido,
            @RequestParam Long idCliente
    ) {
        return historialEstadoServicePort.obtenerHistorial(idPedido, idCliente);
    }

    @PostMapping("/guardar-historial")
    public ResponseEntity<Void> guardarHistorial(@RequestBody HistorialEstado historial) {
        historialEstadoServicePort.guardarHistorial(historial);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tiempo-pedidos")
    public List<TiempoAtencionPorPedidoDto> obtenerTiemposPorPedido(@RequestParam Long idRestaurante) {
        return historialEstadoServicePort.obtenerTiemposPorPedido(idRestaurante);
    }

    @GetMapping("/ranking-empleados")
    public List<RankingEficienciaEmpleadoDto> obtenerRankingPorEmpleado(@RequestParam Long idRestaurante) {
        return historialEstadoServicePort.obtenerRankingPorEmpleado(idRestaurante);
    }


}