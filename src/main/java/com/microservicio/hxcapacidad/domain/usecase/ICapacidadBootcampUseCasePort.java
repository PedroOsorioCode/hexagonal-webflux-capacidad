package com.microservicio.hxcapacidad.domain.usecase;

import com.microservicio.hxcapacidad.domain.model.CapacidadBootcampModel;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ICapacidadBootcampUseCasePort {
    Flux<CapacidadBootcampModel> guardarRelacion(List<CapacidadBootcampModel> lista);
}
