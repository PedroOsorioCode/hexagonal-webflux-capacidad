package com.microservicio.hxcapacidad.infrastructure.out.r2dbc.adapter;

import com.microservicio.hxcapacidad.common.PersistenceAdapter;
import com.microservicio.hxcapacidad.domain.model.CapacidadBootcampModel;
import com.microservicio.hxcapacidad.domain.serviceprovider.ICapacidadBootcampPersistencePort;
import com.microservicio.hxcapacidad.infrastructure.out.r2dbc.entity.CapacidadBootcampEntity;
import com.microservicio.hxcapacidad.infrastructure.out.r2dbc.mapper.ICapacidadBootcampEntityMapper;
import com.microservicio.hxcapacidad.infrastructure.out.r2dbc.repository.CapacidadBootcampRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import java.util.List;

@PersistenceAdapter
@Slf4j
@RequiredArgsConstructor
public class CapacidadBootcampR2dbcAdapter implements ICapacidadBootcampPersistencePort {
    private final CapacidadBootcampRepository capacidadBootcampRepository;
    private final ICapacidadBootcampEntityMapper capacidadBootcampEntityMapper;

    @Override
    public Flux<CapacidadBootcampModel> guardarRelacion(List<CapacidadBootcampModel> lista) {
        List<CapacidadBootcampEntity> listaEntity =
                capacidadBootcampEntityMapper.toEntityFromModelList(lista);
        return capacidadBootcampRepository.saveAll(listaEntity)
                .map(capacidadBootcampEntityMapper::toModelFromEntity);
    }

    @Override
    public Flux<CapacidadBootcampModel> consultarPorBootcamp(List<Long> listaIdBootcamp) {
        return capacidadBootcampRepository.findAllByidBootcampIn(listaIdBootcamp)
                .map(capacidadBootcampEntityMapper::toModelFromEntity);
    }
}
