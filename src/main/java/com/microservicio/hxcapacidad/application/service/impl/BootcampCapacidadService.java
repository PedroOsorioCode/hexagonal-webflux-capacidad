package com.microservicio.hxcapacidad.application.service.impl;

import com.microservicio.hxcapacidad.application.common.ConstantesAplicacion;
import com.microservicio.hxcapacidad.application.common.MensajeError;
import com.microservicio.hxcapacidad.application.dto.request.BootcampCapacidadRequestDto;
import com.microservicio.hxcapacidad.application.dto.request.BootcampRequestDto;
import com.microservicio.hxcapacidad.application.dto.request.NewBootcampCapacidadRequestDto;
import com.microservicio.hxcapacidad.application.dto.request.NewBootcampRequestDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampCapacidadResponseDto;
import com.microservicio.hxcapacidad.application.mapper.ICapacidadBootcampModelMapper;
import com.microservicio.hxcapacidad.application.service.IBootcampCapacidadService;
import com.microservicio.hxcapacidad.domain.model.CapacidadBootcampModel;
import com.microservicio.hxcapacidad.domain.usecase.ICapacidadBootcampUseCasePort;
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
public class BootcampCapacidadService implements IBootcampCapacidadService {
    private final WebClient webClientBootcamp;
    private final ICapacidadBootcampUseCasePort capacidadBootcampUseCasePort;
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
                                            // Crea el DTO de respuesta y asigna la lista de capacidades
                                            BootcampCapacidadResponseDto responseDto = new BootcampCapacidadResponseDto();
                                            responseDto.setId(respuesta.getId());
                                            responseDto.setNombre(respuesta.getNombre());
                                            responseDto.setDescripcion(respuesta.getDescripcion());
                                            responseDto.setListaCapacidades(capacidadBootcampModelMapper.toResponseFromModelList(listaCapacidades)); // Asignar la lista de capacidades

                                            return responseDto; // Retorna el DTO completo
                                        });
                            } else {
                                // Manejo de respuesta vacía o error
                                return Mono.just(new BootcampCapacidadResponseDto());
                            }
                        })
                );
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
            if (req.getListaCapacidad().size() < 1)
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
