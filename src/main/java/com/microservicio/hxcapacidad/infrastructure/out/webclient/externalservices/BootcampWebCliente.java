package com.microservicio.hxcapacidad.infrastructure.out.webclient.externalservices;

import com.microservicio.hxcapacidad.application.dto.request.CapacidadFilterRequestDto;
import com.microservicio.hxcapacidad.application.dto.request.NewBootcampRequestDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampCapacidadResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampPaginacionResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampResponseDto;
import com.microservicio.hxcapacidad.common.WebClientAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@WebClientAdapter
@RequiredArgsConstructor
public class BootcampWebCliente {
    private final WebClient webClientBootcampExternal;

    @Value("${addressservice.base.urlbootcamp.listar}")
    private String addressBaseBootcampListar;

    public Mono<BootcampPaginacionResponseDto<BootcampResponseDto>> consultarBootcampPaginado(CapacidadFilterRequestDto filter){
        return webClientBootcampExternal.post()
                .uri(this.addressBaseBootcampListar)
                .bodyValue(filter)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>(){});
    }

    public Mono<BootcampCapacidadResponseDto> guardarBootcamp(NewBootcampRequestDto nuevoBootcamp){
        return webClientBootcampExternal.post()
                .bodyValue(nuevoBootcamp)
                .retrieve()
                .bodyToMono(BootcampCapacidadResponseDto.class);
    }

}
