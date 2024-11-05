package com.microservicio.hxcapacidad.application.service;

import com.microservicio.hxcapacidad.application.dto.request.BootcampRequestDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampCapacidadResponseDto;
import reactor.core.publisher.Mono;

public interface IBootcampCapacidadService {
    Mono<BootcampCapacidadResponseDto> guardarBootcamp(Mono<BootcampRequestDto> bootcampRequestDto);
}
