package com.microservicio.hxcapacidad.infrastructure.out.r2dbc.repository;

import com.microservicio.hxcapacidad.infrastructure.out.r2dbc.entity.CapacidadBootcampEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CapacidadBootcampRepository  extends R2dbcRepository<CapacidadBootcampEntity, Long> {
}
