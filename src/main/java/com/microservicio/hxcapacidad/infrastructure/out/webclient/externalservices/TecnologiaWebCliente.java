package com.microservicio.hxcapacidad.infrastructure.out.webclient.externalservices;

import com.microservicio.hxcapacidad.application.dto.request.CapacidadTecnologiaRequestDto;
import com.microservicio.hxcapacidad.application.dto.response.CapacidadResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.TecnologiaResponseDto;
import com.microservicio.hxcapacidad.common.WebClientAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@WebClientAdapter
@RequiredArgsConstructor
public class TecnologiaWebCliente {
    private final WebClient webClientExternal;

    @Value("${addressservice.base.url.relacionarcapacidad}")
    private String addressBaseTecnologiaRelacionar;

    @Value("${addressservice.base.url.consultarrelacionarcapacidad}")
    private String addressBaseTecnologiaConsultarRelacion;

    public Flux<TecnologiaResponseDto> relacionarCapacidadTecnologia(CapacidadTecnologiaRequestDto req){
        return webClientExternal.post()
            .uri(this.addressBaseTecnologiaRelacionar)
            .bodyValue(req)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError() || status.is5xxServerError(), // Captura errores 4xx y 5xx
                clientResponse -> clientResponse.bodyToMono(String.class) // Extrae el mensaje de error
                    .map(errorMessage -> new RuntimeException("Error en la llamada: " + errorMessage))
            )
            .bodyToFlux(TecnologiaResponseDto .class)
            .onErrorResume(e ->
                    Mono.error(new RuntimeException("No se pudo relacionar capacidad con tecnología",e))
            );
    }

    public Flux<CapacidadResponseDto> consultarRelacionCapacidadTecnologia(List<Long> idListaCapacidad){
        return webClientExternal.post()
                .uri(this.addressBaseTecnologiaConsultarRelacion)
                .bodyValue(idListaCapacidad)
                .retrieve()
                .bodyToFlux(CapacidadResponseDto.class);
    }
}
