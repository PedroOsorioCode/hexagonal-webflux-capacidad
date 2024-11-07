package com.microservicio.hxcapacidad.infrastructure.out.r2dbc.repository;

import com.microservicio.hxcapacidad.infrastructure.out.r2dbc.entity.CapacidadBootcampEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface CapacidadBootcampRepository  extends R2dbcRepository<CapacidadBootcampEntity, Long> {
    Flux<CapacidadBootcampEntity> findAllByidBootcampIn(List<Long> idBootcamp);
}
