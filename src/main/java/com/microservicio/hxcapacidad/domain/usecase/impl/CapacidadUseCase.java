package com.microservicio.hxcapacidad.domain.usecase.impl;

import com.microservicio.hxcapacidad.common.UseCase;
import com.microservicio.hxcapacidad.domain.model.CapacidadModel;
import com.microservicio.hxcapacidad.domain.serviceprovider.ICapacidadPersistencePort;
import com.microservicio.hxcapacidad.domain.usecase.ICapacidadUseCasePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@UseCase
@RequiredArgsConstructor
public class CapacidadUseCase implements ICapacidadUseCasePort {
    private final ICapacidadPersistencePort capacidadPersistencePort;
    @Override
    public Mono<CapacidadModel> guardar(CapacidadModel capacidadModel) {
        return capacidadPersistencePort.guardar(capacidadModel);
    }

    @Override
    public Mono<Boolean> existePorNombre(String nombre) {
        return capacidadPersistencePort.existePorNombre(nombre);
    }
}
