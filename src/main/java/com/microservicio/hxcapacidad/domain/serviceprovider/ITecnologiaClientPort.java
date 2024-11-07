package com.microservicio.hxcapacidad.domain.serviceprovider;

import com.microservicio.hxcapacidad.application.dto.request.CapacidadTecnologiaRequestDto;
import com.microservicio.hxcapacidad.application.dto.response.CapacidadResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.TecnologiaResponseDto;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ITecnologiaClientPort {
    Flux<TecnologiaResponseDto> relacionarCapacidadTecnologia(CapacidadTecnologiaRequestDto req);
    Flux<CapacidadResponseDto> consultarRelacionCapacidadTecnologia(List<Long> idListaCapacidad);
}
