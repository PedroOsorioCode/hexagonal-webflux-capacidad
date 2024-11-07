package com.microservicio.hxcapacidad.infrastructure.out.r2dbc.repository;

import com.microservicio.hxcapacidad.infrastructure.out.r2dbc.entity.CapacidadEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface CapacidadRepository extends R2dbcRepository<CapacidadEntity, Long> {
    Mono<CapacidadEntity> findByNombre(String nombre);
}
