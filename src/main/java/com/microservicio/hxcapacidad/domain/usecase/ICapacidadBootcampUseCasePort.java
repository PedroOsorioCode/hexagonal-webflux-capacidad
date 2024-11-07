package com.microservicio.hxcapacidad.domain.usecase;

import com.microservicio.hxcapacidad.application.dto.request.CapacidadFilterRequestDto;
import com.microservicio.hxcapacidad.application.dto.request.NewBootcampRequestDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampCapacidadResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampPaginacionResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampResponseDto;
import com.microservicio.hxcapacidad.domain.model.CapacidadBootcampModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICapacidadBootcampUseCasePort {
    Flux<CapacidadBootcampModel> guardarRelacion(List<CapacidadBootcampModel> lista);
    Flux<CapacidadBootcampModel> consultarPorBootcamp(List<Long> listaIdBootcamp);
    Mono<BootcampPaginacionResponseDto<BootcampResponseDto>> consultarBootcampPaginado(CapacidadFilterRequestDto filter);
    Mono<BootcampCapacidadResponseDto> guardarBootcamp(NewBootcampRequestDto nuevoBootcamp);
}
