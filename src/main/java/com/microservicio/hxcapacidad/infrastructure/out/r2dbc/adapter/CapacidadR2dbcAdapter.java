package com.microservicio.hxcapacidad.infrastructure.out.r2dbc.adapter;

import com.microservicio.hxcapacidad.common.PersistenceAdapter;
import com.microservicio.hxcapacidad.domain.model.CapacidadModel;
import com.microservicio.hxcapacidad.domain.serviceprovider.ICapacidadPersistencePort;
import com.microservicio.hxcapacidad.infrastructure.out.r2dbc.mapper.ICapacidadEntityMapper;
import com.microservicio.hxcapacidad.infrastructure.out.r2dbc.repository.CapacidadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@PersistenceAdapter
@Slf4j
@RequiredArgsConstructor
public class CapacidadR2dbcAdapter implements ICapacidadPersistencePort {
    private final ICapacidadEntityMapper capacidadEntityMapper;
    private final CapacidadRepository capacidadRepository;

    @Override
    public Mono<CapacidadModel> guardar(CapacidadModel capacidadModel) {
        return capacidadRepository.save(capacidadEntityMapper.toEntityFromModel(capacidadModel))
                .map(capacidadEntityMapper::toModelFromEntity);
    }

    @Override
    public Mono<Boolean> existePorNombre(String nombre) {
        return capacidadRepository.findByNombre(nombre).hasElement();
    }
}
