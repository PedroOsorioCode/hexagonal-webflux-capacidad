package com.microservicio.hxcapacidad.infrastructure.out.webclient.adapter;

import com.microservicio.hxcapacidad.application.dto.request.CapacidadTecnologiaRequestDto;
import com.microservicio.hxcapacidad.application.dto.response.CapacidadResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.TecnologiaResponseDto;
import com.microservicio.hxcapacidad.common.WebClientAdapter;
import com.microservicio.hxcapacidad.domain.serviceprovider.ITecnologiaClientPort;
import com.microservicio.hxcapacidad.infrastructure.out.webclient.externalservices.TecnologiaWebCliente;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;

@WebClientAdapter
@RequiredArgsConstructor
public class TecnologiaWebclientAdapter implements ITecnologiaClientPort {
    private final TecnologiaWebCliente tecnologiaWebCliente;

    @Override
    public Flux<TecnologiaResponseDto> relacionarCapacidadTecnologia(CapacidadTecnologiaRequestDto req) {
        return tecnologiaWebCliente.relacionarCapacidadTecnologia(req);
    }

    @Override
    public Flux<CapacidadResponseDto> consultarRelacionCapacidadTecnologia(List<Long> idListaCapacidad) {
        return tecnologiaWebCliente.consultarRelacionCapacidadTecnologia(idListaCapacidad);
    }
}
