package com.microservicio.hxcapacidad.application.service.impl;

import com.microservicio.hxcapacidad.application.common.ConstantesAplicacion;
import com.microservicio.hxcapacidad.application.common.MensajeError;
import com.microservicio.hxcapacidad.application.dto.request.CapacidadFilterRequestDto;
import com.microservicio.hxcapacidad.application.dto.request.CapacidadRequestDto;
import com.microservicio.hxcapacidad.application.dto.request.CapacidadTecnologiaRequestDto;
import com.microservicio.hxcapacidad.application.dto.request.TecnologiaRequestDto;
import com.microservicio.hxcapacidad.application.dto.response.CapacidadPaginacionResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.CapacidadResponseDto;
import com.microservicio.hxcapacidad.application.mapper.ICapacidadModelMapper;
import com.microservicio.hxcapacidad.application.service.ICapacidadService;
import com.microservicio.hxcapacidad.domain.model.CapacidadModel;
import com.microservicio.hxcapacidad.domain.usecase.ICapacidadUseCasePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CapacidadService implements ICapacidadService {
    private final ICapacidadModelMapper capacidadModelMapper;
    private final ICapacidadUseCasePort capacidadUseCasePort;

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

                return capacidadUseCasePort.relacionarCapacidadTecnologia(req)
                        .collectList()
                        .map(responseList -> {
                            savedCapacidad.setListaTecnologias(responseList);
                            return savedCapacidad;
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

                return capacidadUseCasePort.consultarRelacionCapacidadTecnologia(idListaCapacidad)
                        .collectList()
                        .map(res ->
                            crearRespuestaConsultarPaginado(filter, listaCapacidad, res, paginaCapacidades));
            }));
    }

    private CapacidadPaginacionResponseDto<CapacidadResponseDto> crearRespuestaConsultarPaginado(CapacidadFilterRequestDto filter, List<CapacidadResponseDto> listaCapacidad, List<CapacidadResponseDto> res, List<CapacidadResponseDto> paginaCapacidades) {
        res.forEach(capacidadRes -> {
            Optional<CapacidadResponseDto> opCapacidad = paginaCapacidades.stream()
                    .filter(capacidad -> capacidad.getId().equals(capacidadRes.getId()))
                    .findFirst();

            if (opCapacidad.isPresent()){
                capacidadRes.setNombre(opCapacidad.get().getNombre());
                capacidadRes.setCantidadTecnologia(opCapacidad.get().getCantidadTecnologia());
            }
        });

        List<CapacidadResponseDto> ordenadoRes = res.stream()
                .sorted(getComparator(filter))
                .toList();

        return new CapacidadPaginacionResponseDto<>(
                ordenadoRes,
                filter.getNumeroPagina(),
                filter.getTamanoPorPagina(),
                listaCapacidad.size());
    }

    private Comparator<CapacidadResponseDto> getComparator(CapacidadFilterRequestDto filter) {
        if (ConstantesAplicacion.COLUMN_NOMBRE.equalsIgnoreCase(filter.getColumnaOrdenamiento())) {
            return filter.getDireccionOrdenamiento().equalsIgnoreCase("asc")
                    ? Comparator.comparing(CapacidadResponseDto::getNombre, Comparator.nullsLast(String::compareTo)) // Manejo de nulls al final
                    : Comparator.comparing(CapacidadResponseDto::getNombre, Comparator.nullsLast(String::compareTo)).reversed(); // Manejo de nulls al final
        } else if (ConstantesAplicacion.COLUMN_CANTIDAD.equalsIgnoreCase(filter.getColumnaOrdenamiento())) {
            return filter.getDireccionOrdenamiento().equalsIgnoreCase("asc")
                    ? Comparator.comparingInt(CapacidadResponseDto::getCantidadTecnologia) // Evitar NullPointerException si es null
                    : Comparator.comparingInt(CapacidadResponseDto::getCantidadTecnologia).reversed(); // Evitar NullPointerException si es null
        }

        // Comparator por defecto en caso de que el campo no coincida
        return Comparator.comparing(CapacidadResponseDto::getNombre, Comparator.nullsLast(String::compareTo)); // Manejo de nulls al final

    }

    private Mono<CapacidadModel> validarGuardar(Mono<CapacidadRequestDto> request){
        return request.flatMap(req -> {
            String mensaje = "";
            if (req.getNombre().isEmpty())
                mensaje = MensajeError.DATOS_OBLIGATORIOS.formato(ConstantesAplicacion.NOMBRE);
            else if (req.getDescripcion().isEmpty())
                mensaje = MensajeError.DATOS_OBLIGATORIOS.formato(ConstantesAplicacion.DESCRIPCION);
            else if (req.getNombre().length() > ConstantesAplicacion.MAX_NOMBRE)
                mensaje = MensajeError.LONGITUD_PERMITIDA.formato(ConstantesAplicacion.NOMBRE, ConstantesAplicacion.MAX_NOMBRE);
            else if (req.getDescripcion().length() > ConstantesAplicacion.MAX_DESCRIPCION)
                mensaje = MensajeError.LONGITUD_PERMITIDA.formato(ConstantesAplicacion.DESCRIPCION, ConstantesAplicacion.MAX_DESCRIPCION);
            else if (req.getListaTecnologia().size() < 3)
                mensaje = MensajeError.TECNOLOGIA_INCOMPLETAS.formato(req.getNombre());
            else if (req.getListaTecnologia().size() > 20)
                mensaje = MensajeError.TECNOLOGIA_NO_PERMITIDAS.formato(req.getNombre());
            else if (this.existenTecnologiasRepetidas(req.getListaTecnologia()))
                mensaje = MensajeError.TECNOLOGIA_REPETIDAS.formato(req.getNombre());

            if (!mensaje.isEmpty())
                return Mono.error(new RuntimeException(mensaje));

            CapacidadModel capacidadModel = capacidadModelMapper.toModelFromRequest(req);
            capacidadModel.setCantidadTecnologia(req.getListaTecnologia().size());
            return capacidadUseCasePort.existePorNombre(req.getNombre())
                .flatMap(existe -> Boolean.TRUE.equals(existe) ?
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
