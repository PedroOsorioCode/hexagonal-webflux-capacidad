package com.microservicio.hxcapacidad.application.service.impl;

import com.microservicio.hxcapacidad.application.common.ConstantesAplicacion;
import com.microservicio.hxcapacidad.application.common.MensajeError;
import com.microservicio.hxcapacidad.application.dto.request.*;
import com.microservicio.hxcapacidad.application.dto.response.BootcampCapacidadResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.CapacidadPaginacionResponseDto;
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

import java.util.Comparator;
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
        return capacidadRequestDTO.flatMap(capacidad -> this.validarGuardar(Mono.just(capacidad))
            .onErrorResume(Mono::error)
            .flatMap(data ->
                    capacidadUseCasePort.guardar(data).map(capacidadModelMapper::toResponseFromModel)
            )
            .flatMap(savedCapacidad -> {
                Long capacidadId = savedCapacidad.getId();

            List<Long> listaTec = capacidad.getListaTecnologia().stream().map(data -> data.getId()).toList();
            CapacidadTecnologiaRequestDto req = new CapacidadTecnologiaRequestDto(capacidadId, listaTec);

            return webClient.post()
                    .uri("/relacionar-capacidad-tecnologia")
                    .bodyValue(req)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(), // Captura errores 4xx y 5xx
                            clientResponse -> clientResponse.bodyToMono(String.class) // Extrae el mensaje de error
                                    .map(errorMessage -> new RuntimeException("Error en la llamada: " + errorMessage))
                    )
                    .bodyToFlux(TecnologiaResponseDto.class)
                    .collectList()
                    .map(responseList -> {
                        // Asigna la lista de respuestas a un atributo de savedCapacidad
                        savedCapacidad.setListaTecnologias(responseList);
                        return savedCapacidad;
                    }).onErrorResume(e -> {
                        // Manejo general de errores en caso de excepciones imprevistas
                        System.err.println("Error durante la llamada: " + e.getMessage());
                        // Retorna un valor por defecto, un Mono vacío, o una excepción controlada, según tu necesidad
                        return Mono.error(new RuntimeException("No se pudo relacionar capacidad con tecnología", e));
                    });
            }));
    }

    @Override
    public Mono<CapacidadPaginacionResponseDto<CapacidadResponseDto>>
    consultarTodosPaginado(Mono<CapacidadFilterRequestDto> capacidadFilterRequestDTO) {
        return capacidadFilterRequestDTO.flatMap(filter -> capacidadUseCasePort.obtenerTodos()
            .switchIfEmpty(Mono.empty())
            .map(capacidad ->
                    new CapacidadResponseDto(capacidad.getId(), capacidad.getNombre(), capacidad.getDescripcion(), capacidad.getCantidadTecnologia()))
            .sort((getComparator(filter)))
            .collectList()
            .flatMap(listaCapacidad -> {
                // Calcular la paginación
                int skip = filter.getNumeroPagina() * filter.getTamanoPorPagina();
                List<CapacidadResponseDto> paginaCapacidades = listaCapacidad.stream()
                        .skip(skip)
                        .limit(filter.getTamanoPorPagina())
                        .toList();

                List<Long> idListaCapacidad = paginaCapacidades.stream()
                        .map(CapacidadResponseDto::getId)
                        .toList();

                return webClient.post()
                        .uri("/consultar-relacion-capacidad-tecnologia")
                        .bodyValue(idListaCapacidad)
                        .retrieve()
                        .bodyToFlux(CapacidadResponseDto.class)
                        .collectList()
                        .map(res -> {
                            res.forEach(capacidadRes -> {
                                String nombre = paginaCapacidades.stream()
                                        .filter(capacidad -> capacidad.getId().equals(capacidadRes.getId()))
                                        .map(CapacidadResponseDto::getNombre)
                                        .findFirst()
                                        .orElse("");

                                capacidadRes.setNombre(nombre);
                            });

                            return new CapacidadPaginacionResponseDto<>(
                                res,
                                filter.getNumeroPagina(),
                                filter.getTamanoPorPagina(),
                                paginaCapacidades.size());
                        });
            }));
    }

    private Comparator<CapacidadResponseDto> getComparator(CapacidadFilterRequestDto filter) {
        if ("nombre".equalsIgnoreCase(filter.getColumnaOrdenamiento())) {
            return filter.getDireccionOrdenamiento().equalsIgnoreCase("asc")
                    ? Comparator.comparing(CapacidadResponseDto::getNombre)
                    : Comparator.comparing(CapacidadResponseDto::getNombre).reversed();
        } else if ("nrotecnologia".equalsIgnoreCase(filter.getColumnaOrdenamiento())) {
            return filter.getDireccionOrdenamiento().equalsIgnoreCase("asc")
                    ? Comparator.comparingInt(CapacidadResponseDto::getCantidadTecnologia)
                    : Comparator.comparingInt(CapacidadResponseDto::getCantidadTecnologia).reversed();
        }

        // Comparator por defecto en caso de que el campo no coincida
        return Comparator.comparing(CapacidadResponseDto::getNombre);
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

            if (!mensaje.isEmpty())
                return Mono.error(new RuntimeException(mensaje));

            CapacidadModel capacidadModel = capacidadModelMapper.toModelFromRequest(req);
            capacidadModel.setCantidadTecnologia(req.getListaTecnologia().size());
            return capacidadUseCasePort.existePorNombre(req.getNombre())
                .flatMap(existe -> existe ?
                    Mono.error(new RuntimeException(MensajeError.NOMBRE_DUPLICADO.getMensaje()))
                    : Mono.just(capacidadModel));
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
