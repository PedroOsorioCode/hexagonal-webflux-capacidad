package com.microservicio.hxcapacidad.domain.usecase.impl;

import com.microservicio.hxcapacidad.common.UseCase;
import com.microservicio.hxcapacidad.domain.model.CapacidadBootcampModel;
import com.microservicio.hxcapacidad.domain.serviceprovider.ICapacidadBootcampPersistencePort;
import com.microservicio.hxcapacidad.domain.usecase.ICapacidadBootcampUseCasePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import java.util.List;

@UseCase
@RequiredArgsConstructor
public class CapacidadBootcampUseCase implements ICapacidadBootcampUseCasePort {
    private final ICapacidadBootcampPersistencePort capacidadBootcampPersistencePort;
    @Override
    public Flux<CapacidadBootcampModel> guardarRelacion(List<CapacidadBootcampModel> lista) {
        return capacidadBootcampPersistencePort.guardarRelacion(lista);
    }

    @Override
    public Flux<CapacidadBootcampModel> consultarPorBootcamp(List<Long> listaIdBootcamp) {
        return capacidadBootcampPersistencePort.consultarPorBootcamp(listaIdBootcamp);
    }
}
