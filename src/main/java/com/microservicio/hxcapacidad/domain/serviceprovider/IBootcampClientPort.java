package com.microservicio.hxcapacidad.domain.serviceprovider;

import com.microservicio.hxcapacidad.application.dto.request.CapacidadFilterRequestDto;
import com.microservicio.hxcapacidad.application.dto.request.NewBootcampRequestDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampCapacidadResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampPaginacionResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampResponseDto;
import reactor.core.publisher.Mono;

public interface IBootcampClientPort {
    Mono<BootcampPaginacionResponseDto<BootcampResponseDto>> consultarBootcampPaginado(CapacidadFilterRequestDto filter);
    Mono<BootcampCapacidadResponseDto> guardarBootcamp(NewBootcampRequestDto nuevoBootcamp);
}
