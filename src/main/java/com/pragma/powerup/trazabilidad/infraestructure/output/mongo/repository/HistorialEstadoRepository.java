package com.pragma.powerup.trazabilidad.infraestructure.output.mongo.repository;

import com.pragma.powerup.trazabilidad.infraestructure.output.mongo.document.HistorialEstadoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HistorialEstadoRepository extends MongoRepository<HistorialEstadoDocument, String> {
    List<HistorialEstadoDocument> findByIdPedidoAndIdCliente(Long idPedido, Long idCliente);
}