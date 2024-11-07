package com.microservicio.hxcapacidad.infrastructure.out.webclient.adapter;

import com.microservicio.hxcapacidad.application.dto.request.CapacidadFilterRequestDto;
import com.microservicio.hxcapacidad.application.dto.request.NewBootcampRequestDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampCapacidadResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampPaginacionResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampResponseDto;
import com.microservicio.hxcapacidad.common.WebClientAdapter;
import com.microservicio.hxcapacidad.domain.serviceprovider.IBootcampClientPort;
import com.microservicio.hxcapacidad.infrastructure.out.webclient.externalservices.BootcampWebCliente;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@WebClientAdapter
@RequiredArgsConstructor
public class BootcampWebclientAdapter implements IBootcampClientPort {
    private final BootcampWebCliente bootcampWebCliente;

    @Override
    public Mono<BootcampPaginacionResponseDto<BootcampResponseDto>> consultarBootcampPaginado(
            CapacidadFilterRequestDto filter) {
        return bootcampWebCliente.consultarBootcampPaginado(filter);
    }

    @Override
    public Mono<BootcampCapacidadResponseDto> guardarBootcamp(NewBootcampRequestDto nuevoBootcamp) {
        return bootcampWebCliente.guardarBootcamp(nuevoBootcamp);
    }
}
