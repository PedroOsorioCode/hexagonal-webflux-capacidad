package com.microservicio.hxcapacidad.domain.usecase.impl;

import com.microservicio.hxcapacidad.application.dto.request.CapacidadFilterRequestDto;
import com.microservicio.hxcapacidad.application.dto.request.NewBootcampRequestDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampCapacidadResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampPaginacionResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampResponseDto;
import com.microservicio.hxcapacidad.common.UseCase;
import com.microservicio.hxcapacidad.domain.model.CapacidadBootcampModel;
import com.microservicio.hxcapacidad.domain.serviceprovider.IBootcampClientPort;
import com.microservicio.hxcapacidad.domain.serviceprovider.ICapacidadBootcampPersistencePort;
import com.microservicio.hxcapacidad.domain.usecase.ICapacidadBootcampUseCasePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class CapacidadBootcampUseCase implements ICapacidadBootcampUseCasePort {
    private final ICapacidadBootcampPersistencePort capacidadBootcampPersistencePort;
    private final IBootcampClientPort bootcampClientPort;

    @Override
    public Flux<CapacidadBootcampModel> guardarRelacion(List<CapacidadBootcampModel> lista) {
        return capacidadBootcampPersistencePort.guardarRelacion(lista);
    }

    @Override
    public Flux<CapacidadBootcampModel> consultarPorBootcamp(List<Long> listaIdBootcamp) {
        return capacidadBootcampPersistencePort.consultarPorBootcamp(listaIdBootcamp);
    }

    @Override
    public Mono<BootcampPaginacionResponseDto<BootcampResponseDto>> consultarBootcampPaginado(CapacidadFilterRequestDto filter) {
        return bootcampClientPort.consultarBootcampPaginado(filter);
    }

    @Override
    public Mono<BootcampCapacidadResponseDto> guardarBootcamp(NewBootcampRequestDto nuevoBootcamp) {
        return bootcampClientPort.guardarBootcamp(nuevoBootcamp);
    }
}
