package com.microservicio.hxcapacidad.application.service.impl;

import com.microservicio.hxcapacidad.application.common.ConstantesAplicacion;
import com.microservicio.hxcapacidad.application.common.MensajeError;
import com.microservicio.hxcapacidad.application.dto.request.CapacidadRequestDto;
import com.microservicio.hxcapacidad.application.dto.request.CapacidadTecnologiaRequestDto;
import com.microservicio.hxcapacidad.application.dto.request.TecnologiaRequestDto;
import com.microservicio.hxcapacidad.application.dto.response.CapacidadResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.TecnologiaResponseDto;
import com.microservicio.hxcapacidad.application.mapper.ICapacidadModelMapper;
import com.microservicio.hxcapacidad.application.service.ICapacidadService;
import com.microservicio.hxcapacidad.domain.model.CapacidadModel;
import com.microservicio.hxcapacidad.domain.usecase.ICapacidadUseCasePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CapacidadService implements ICapacidadService {
    private final ICapacidadModelMapper capacidadModelMapper;
    private final ICapacidadUseCasePort capacidadUseCasePort;
    private final WebClient webClient;

    @Override
    public Mono<CapacidadResponseDto> guardar(Mono<CapacidadRequestDto> capacidadRequestDTO) {
        return this.validarGuardar(capacidadRequestDTO)
                .onErrorResume(Mono::error)
                .flatMap(data ->
                    capacidadUseCasePort.guardar(data).map(capacidadModelMapper::toResponseFromModel)
                )
                .flatMap(savedCapacidad -> {
                    Long capacidadId = savedCapacidad.getId();

                    return capacidadRequestDTO.flatMap(capacidad -> {
                        List<Long> listaTec = capacidad.getListaTecnologia().stream().map(data -> data.getId()).toList();
                        CapacidadTecnologiaRequestDto req = new CapacidadTecnologiaRequestDto(capacidadId, listaTec);

                        return webClient.post()
                            .uri("/relacionar-capacidad-tecnologia")
                            .bodyValue(req)
                            .retrieve()
                            .bodyToFlux(TecnologiaResponseDto.class)
                            .collectList()
                            .then(Mono.fromCallable(() -> savedCapacidad));
                    });
                });
    }

    private Mono<CapacidadModel> validarGuardar(Mono<CapacidadRequestDto> request){
        return request.flatMap(req -> {
            String mensaje = "";
            if (req.getNombre().isEmpty())
                mensaje = MensajeError.DATOS_OBLIGATORIOS.formato(ConstantesAplicacion.NOMBRE);
            if (req.getDescripcion().isEmpty())
                mensaje = MensajeError.DATOS_OBLIGATORIOS.formato(ConstantesAplicacion.DESCRIPCION);
            if (req.getNombre().length() > ConstantesAplicacion.MAX_NOMBRE)
                mensaje = MensajeError.LONGITUD_PERMITIDA.formato(ConstantesAplicacion.NOMBRE, ConstantesAplicacion.MAX_NOMBRE);
            if (req.getDescripcion().length() > ConstantesAplicacion.MAX_DESCRIPCION)
                mensaje = MensajeError.LONGITUD_PERMITIDA.formato(ConstantesAplicacion.DESCRIPCION, ConstantesAplicacion.MAX_DESCRIPCION);
            if (req.getListaTecnologia().size() < 3)
                mensaje = MensajeError.TECNOLOGIA_INCOMPLETAS.formato(req.getNombre());
            if (req.getListaTecnologia().size() > 20)
                mensaje = MensajeError.TECNOLOGIA_NO_PERMITIDAS.formato(req.getNombre());
            if (this.existenTecnologiasRepetidas(req.getListaTecnologia()))
                mensaje = MensajeError.TECNOLOGIA_REPETIDAS.formato(req.getNombre());

            if (!mensaje.isEmpty()) {
                return Mono.error(new RuntimeException(mensaje));
            }

            return capacidadUseCasePort.existePorNombre(req.getNombre())
                    .flatMap(existe -> existe ?
                            Mono.error(new RuntimeException(MensajeError.NOMBRE_DUPLICADO.getMensaje()))
                            : Mono.just(capacidadModelMapper.toModelFromRequest(req)));
        });
    }

    private boolean existenTecnologiasRepetidas(List<TecnologiaRequestDto> listaTecnologia) {
        Set<Long> ids = new HashSet<>();
        for (TecnologiaRequestDto tecnologia : listaTecnologia) {
            if (!ids.add(tecnologia.getId())) {
                return true;
            }
        }
        return false;
    }
}
