package com.microservicio.hxcapacidad.domain.usecase.impl;

import com.microservicio.hxcapacidad.application.dto.request.CapacidadTecnologiaRequestDto;
import com.microservicio.hxcapacidad.application.dto.response.CapacidadResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.TecnologiaResponseDto;
import com.microservicio.hxcapacidad.common.UseCase;
import com.microservicio.hxcapacidad.domain.model.CapacidadModel;
import com.microservicio.hxcapacidad.domain.serviceprovider.ICapacidadPersistencePort;
import com.microservicio.hxcapacidad.domain.serviceprovider.ITecnologiaClientPort;
import com.microservicio.hxcapacidad.domain.usecase.ICapacidadUseCasePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class CapacidadUseCase implements ICapacidadUseCasePort {
    private final ICapacidadPersistencePort capacidadPersistencePort;
    private final ITecnologiaClientPort tecnologiaClientPort;

    @Override
    public Mono<CapacidadModel> guardar(CapacidadModel capacidadModel) {
        return capacidadPersistencePort.guardar(capacidadModel);
    }

    @Override
    public Mono<Boolean> existePorNombre(String nombre) {
        return capacidadPersistencePort.existePorNombre(nombre);
    }

    @Override
    public Flux<CapacidadModel> obtenerTodos() {
        return capacidadPersistencePort.obtenerTodos();
    }

    @Override
    public Flux<CapacidadModel> obtenerTodosPorId(List<Long> idsCapacidad) {
        return capacidadPersistencePort.obtenerTodosPorId(idsCapacidad);
    }

    @Override
    public Flux<TecnologiaResponseDto> relacionarCapacidadTecnologia(CapacidadTecnologiaRequestDto req) {
        return tecnologiaClientPort.relacionarCapacidadTecnologia(req);
    }

    @Override
    public Flux<CapacidadResponseDto> consultarRelacionCapacidadTecnologia(List<Long> idListaCapacidad) {
        return tecnologiaClientPort.consultarRelacionCapacidadTecnologia(idListaCapacidad);
    }
}
