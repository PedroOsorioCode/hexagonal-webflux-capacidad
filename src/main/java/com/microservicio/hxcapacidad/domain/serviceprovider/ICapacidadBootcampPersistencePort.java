package com.microservicio.hxcapacidad.domain.serviceprovider;

import com.microservicio.hxcapacidad.domain.model.CapacidadBootcampModel;
import reactor.core.publisher.Flux;
import java.util.List;

public interface ICapacidadBootcampPersistencePort {
    Flux<CapacidadBootcampModel> guardarRelacion(List<CapacidadBootcampModel> lista);
    Flux<CapacidadBootcampModel> consultarPorBootcamp(List<Long> listaIdBootcamp);
}
