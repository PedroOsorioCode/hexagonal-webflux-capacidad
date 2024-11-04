package com.microservicio.hxcapacidad.application.service;

import com.microservicio.hxcapacidad.application.dto.request.CapacidadRequestDto;
import com.microservicio.hxcapacidad.application.dto.response.CapacidadResponseDto;
import reactor.core.publisher.Mono;

public interface ICapacidadService {
    Mono<CapacidadResponseDto> guardar(Mono<CapacidadRequestDto> capacidadRequestDTO);
}
