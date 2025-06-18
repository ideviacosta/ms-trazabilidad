package com.pragma.powerup.trazabilidad.infraestructure.config;

import com.pragma.powerup.trazabilidad.application.usecase.HistorialEstadoUseCase;
import com.pragma.powerup.trazabilidad.domain.api.HistorialEstadoServicePort;
import com.pragma.powerup.trazabilidad.domain.spi.HistorialEstadoPersistencePort;
import com.pragma.powerup.trazabilidad.infraestructure.output.mongo.adapter.HistorialEstadoMongoAdapter;
import com.pragma.powerup.trazabilidad.infraestructure.output.mongo.repository.HistorialEstadoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public HistorialEstadoPersistencePort historialEstadoPersistencePort(HistorialEstadoRepository repository) {
        return new HistorialEstadoMongoAdapter(repository);
    }

    @Bean
    public HistorialEstadoServicePort historialEstadoServicePort(HistorialEstadoPersistencePort persistencePort) {
        return new HistorialEstadoUseCase(persistencePort);
    }
}