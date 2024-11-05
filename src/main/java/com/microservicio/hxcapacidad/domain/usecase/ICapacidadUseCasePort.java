package com.microservicio.hxcapacidad.domain.usecase;

import com.microservicio.hxcapacidad.domain.model.CapacidadModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICapacidadUseCasePort {
    Mono<CapacidadModel> guardar(CapacidadModel capacidadModel);
    Mono<Boolean> existePorNombre(String nombre);
    Flux<CapacidadModel> obtenerTodos();
    Flux<CapacidadModel> obtenerTodosPorId(List<Long> idsCapacidad);
}
