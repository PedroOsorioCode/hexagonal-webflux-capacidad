package com.microservicio.hxcapacidad.application.service;

import com.microservicio.hxcapacidad.application.dto.request.BootcampRequestDto;
import com.microservicio.hxcapacidad.application.dto.request.CapacidadFilterRequestDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampCapacidadResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampPaginacionResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.CapacidadPaginacionResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.CapacidadResponseDto;
import reactor.core.publisher.Mono;

public interface IBootcampCapacidadService {
    Mono<BootcampCapacidadResponseDto> guardarBootcamp(Mono<BootcampRequestDto> bootcampRequestDto);
    Mono<BootcampPaginacionResponseDto<BootcampCapacidadResponseDto>> consultarBootcampTodosPaginado(Mono<CapacidadFilterRequestDto> capacidadFilterRequestDTO);
}
