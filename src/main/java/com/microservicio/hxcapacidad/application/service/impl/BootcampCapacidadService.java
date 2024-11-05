package com.microservicio.hxcapacidad.application.service.impl;

import com.microservicio.hxcapacidad.application.common.ConstantesAplicacion;
import com.microservicio.hxcapacidad.application.common.MensajeError;
import com.microservicio.hxcapacidad.application.dto.request.*;
import com.microservicio.hxcapacidad.application.dto.response.BootcampCapacidadResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampPaginacionResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.CapacidadResponseDto;
import com.microservicio.hxcapacidad.application.mapper.ICapacidadBootcampModelMapper;
import com.microservicio.hxcapacidad.application.service.IBootcampCapacidadService;
import com.microservicio.hxcapacidad.domain.model.CapacidadBootcampModel;
import com.microservicio.hxcapacidad.domain.model.CapacidadModel;
import com.microservicio.hxcapacidad.domain.usecase.ICapacidadBootcampUseCasePort;
import com.microservicio.hxcapacidad.domain.usecase.ICapacidadUseCasePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BootcampCapacidadService implements IBootcampCapacidadService {
    private final WebClient webClientBootcamp;
    private final WebClient webClient;
    private final ICapacidadBootcampUseCasePort capacidadBootcampUseCasePort;
    private final ICapacidadUseCasePort capacidadUseCasePort;
    private final ICapacidadBootcampModelMapper capacidadBootcampModelMapper;

    @Override
    public Mono<BootcampCapacidadResponseDto> guardarBootcamp(Mono<BootcampRequestDto> bootcampRequestDto) {
        return bootcampRequestDto
            .flatMap(bootcamp -> this.validarGuardar(Mono.just(bootcamp)))
            .onErrorResume(Mono::error)
            .flatMap(data -> webClientBootcamp.post()
                .uri("")
                .bodyValue(data.getNuevoBootcamp())
                .retrieve()
                .toEntity(BootcampCapacidadResponseDto.class) // Captura toda la respuesta
                .flatMap(responseEntity -> {
                    // Extrae el cuerpo y el ID necesario
                    BootcampCapacidadResponseDto respuesta = responseEntity.getBody();
                    if (respuesta != null && respuesta.getId() != null) {
                        Long idBootcamp = respuesta.getId();

                        // Crear lista de CapacidadBootcampModel usando el idBootcamp
                        List<CapacidadBootcampModel> listaCapacidadBootcamModel = data.getListaCapacidad().stream()
                                .map(dataIdCapacidad -> new CapacidadBootcampModel(dataIdCapacidad.getId(), idBootcamp))
                                .toList();

                        // Procesa el fluxRelacion y transforma en BootcampCapacidadResponseDto
                        return capacidadBootcampUseCasePort.guardarRelacion(listaCapacidadBootcamModel)
                                .collectList() // Convierte el Flux en un Mono<List<CapacidadBootcampModel>>
                                .map(listaCapacidades -> {

                                    /*List<Long> idCapacidad = listaCapacidades.stream()
                                            .map(CapacidadBootcampModel::getIdCapacidad)
                                            .toList();

                                    return capacidadUseCasePort.obtenerTodosPorId(idCapacidad)
                                            .collectList()
                                            .flatMap(listaCapacidadesDto ->{

                                            });*/

                                    // Crea el DTO de respuesta y asigna la lista de capacidades
                                    BootcampCapacidadResponseDto responseDto = new BootcampCapacidadResponseDto();
                                    responseDto.setId(respuesta.getId());
                                    responseDto.setNombre(respuesta.getNombre());
                                    responseDto.setDescripcion(respuesta.getDescripcion());

                                    return responseDto; // Retorna el DTO completo
                                });
                    } else {
                        // Manejo de respuesta vacía o error
                        return Mono.just(new BootcampCapacidadResponseDto());
                    }
                })
            );
    }

    @Override
    public Mono<BootcampPaginacionResponseDto<BootcampCapacidadResponseDto>>
        consultarBootcampTodosPaginado(Mono<CapacidadFilterRequestDto> capacidadFilterRequestDTO) {

        return capacidadFilterRequestDTO.flatMap(filter ->
            webClientBootcamp.post()
            .uri("/listar")
            .bodyValue(filter)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<BootcampPaginacionResponseDto<BootcampResponseDto>>() {})
            .flatMap(responseList -> {
                List<Long> idBootcamp = responseList.getContent().stream()
                        .map(BootcampResponseDto::getId)
                        .toList();

                return capacidadBootcampUseCasePort.consultarPorBootcamp(idBootcamp)
                    .collectList() // Convertir a Mono<List<CapacidadBootcampModel>>
                    .flatMap(listaRelacionBootcamp ->
                            consultarCapacidadesConTecnologia(filter, responseList, listaRelacionBootcamp));
            })
        );

    }

    private Mono<BootcampPaginacionResponseDto<BootcampCapacidadResponseDto>>
        consultarCapacidadesConTecnologia(CapacidadFilterRequestDto filter,
                  BootcampPaginacionResponseDto<BootcampResponseDto> responseList,
                  List<CapacidadBootcampModel> listaRelacionBootcamp) {

        List<Long> idCapacidad = listaRelacionBootcamp.stream()
                .map(CapacidadBootcampModel::getIdCapacidad)
                .toList();

        // Consultar todas las capacidades y crear la respuesta final
        return capacidadUseCasePort.obtenerTodosPorId(idCapacidad)
                .collectList()
                .flatMap(listaCapacidades -> webClient.post()
                        .uri("/consultar-relacion-capacidad-tecnologia")
                        .bodyValue(idCapacidad)
                        .retrieve()
                        .bodyToFlux(CapacidadResponseDto.class)
                        .collectList()
                        .map(res -> crearRespuestaPaginada(filter, responseList, listaRelacionBootcamp, listaCapacidades, res)));
    }

    private BootcampPaginacionResponseDto<BootcampCapacidadResponseDto>
        crearRespuestaPaginada(CapacidadFilterRequestDto filter, BootcampPaginacionResponseDto<BootcampResponseDto> responseList, List<CapacidadBootcampModel> listaRelacionBootcamp, List<CapacidadModel> listaCapacidades, List<CapacidadResponseDto> res) {
        res.forEach(capacidadRes -> {
            Optional<CapacidadModel> capacidadModel = listaCapacidades.stream()
                    .filter(capacidad -> capacidad.getId().equals(capacidadRes.getId()))
                    .findFirst();

            if (capacidadModel.isPresent()) {
                capacidadRes.setNombre(capacidadModel.get().getNombre());
                capacidadRes.setListaTecnologias(capacidadRes.getListaTecnologias());
            }
        });

        List<BootcampCapacidadResponseDto> listaBoot = new ArrayList<>();
        responseList.getContent().stream().forEach(resBoot -> {
            List<Long> idCapacidades = listaRelacionBootcamp.stream()
                    .filter(bootcamp -> resBoot.getId().equals(bootcamp.getIdBootcamp()))
                    .map(CapacidadBootcampModel::getIdCapacidad)
                    .toList();

            List<CapacidadResponseDto> resCapa = res.stream().filter(d -> idCapacidades.contains(d.getId()))
                    .toList();

            BootcampCapacidadResponseDto nuevo = capacidadBootcampModelMapper.toResponseFromResponseBootcamp(resBoot);
            nuevo.setListaCapacidades(resCapa);
            listaBoot.add(nuevo);

        });

        return new BootcampPaginacionResponseDto<>(
                listaBoot,
                filter.getNumeroPagina(),
                filter.getTamanoPorPagina(),
                responseList.getTotalElements());
    }

    private Mono<NewBootcampCapacidadRequestDto> validarGuardar(Mono<BootcampRequestDto> request){
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
            if (req.getListaCapacidad().isEmpty())
                mensaje = MensajeError.CAPACIDAD_INCOMPLETAS.formato(req.getNombre());
            if (req.getListaCapacidad().size() > 4)
                mensaje = MensajeError.CAPACIDAD_NO_PERMITIDAS.formato(req.getNombre());
            if (this.existenCapacidadesRepetidas(req.getListaCapacidad()))
                mensaje = MensajeError.CAPACIDAD_REPETIDAS.formato(req.getNombre());

            if (!mensaje.isEmpty())
                return Mono.error(new RuntimeException(mensaje));

            NewBootcampRequestDto newBootcamp = new NewBootcampRequestDto();
            newBootcamp.setNombre(req.getNombre());
            newBootcamp.setDescripcion(req.getDescripcion());
            newBootcamp.setCantidadCapacidad(req.getListaCapacidad().size());

            NewBootcampCapacidadRequestDto nuevos = new NewBootcampCapacidadRequestDto(newBootcamp, req.getListaCapacidad());
            return Mono.just(nuevos);
        });
    }

    private boolean existenCapacidadesRepetidas(List<BootcampCapacidadRequestDto> listaCapacidad) {
        Set<Long> ids = new HashSet<>();
        for (BootcampCapacidadRequestDto capacidad : listaCapacidad) {
            if (!ids.add(capacidad.getId())) {
                return true;
            }
        }
        return false;
    }
}
