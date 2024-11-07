package com.microservicio.hxcapacidad.application.service;

import com.microservicio.hxcapacidad.application.common.ConstantesAplicacion;
import com.microservicio.hxcapacidad.application.common.MensajeError;
import com.microservicio.hxcapacidad.application.dto.request.CapacidadRequestDto;
import com.microservicio.hxcapacidad.application.dto.request.TecnologiaRequestDto;
import com.microservicio.hxcapacidad.application.mapper.ICapacidadModelMapper;
import com.microservicio.hxcapacidad.application.service.impl.CapacidadService;
import com.microservicio.hxcapacidad.domain.usecase.impl.CapacidadUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

class CapacidadServiceTest {
    @InjectMocks
    private CapacidadService capacidadService;
    @Mock
    private ICapacidadModelMapper capacidadModelMapper;
    @Mock
    private CapacidadUseCase capacidadUseCase;

    private String nombre = "test";
    private String descripcion = "testd";
    private String cadenaVacia = "";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }

    @Test
    void testValidarGuardarFaltanTecnologias() {
        CapacidadRequestDto requestDto = CapacidadRequestDto
                .builder().nombre(this.nombre).descripcion(this.descripcion)
                .listaTecnologia(List.of(new TecnologiaRequestDto())).build();

        Mono<CapacidadRequestDto> requestMono = Mono.just(requestDto);

        StepVerifier.create(capacidadService.guardar(requestMono))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals(MensajeError.TECNOLOGIA_INCOMPLETAS.formato(this.nombre)))
                .verify();
    }

    @Test
    void testValidarGuardarTecnologiasRepetidas() {
        CapacidadRequestDto requestDto = CapacidadRequestDto
                .builder().nombre(this.nombre).descripcion(this.descripcion)
                .listaTecnologia(List.of(new TecnologiaRequestDto(), new TecnologiaRequestDto(), new TecnologiaRequestDto())).build();

        Mono<CapacidadRequestDto> requestMono = Mono.just(requestDto);

        StepVerifier.create(capacidadService.guardar(requestMono))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals(MensajeError.TECNOLOGIA_REPETIDAS.formato(this.nombre)))
                .verify();
    }

    @Test void testValidarGuardarNombreVacio() {
        CapacidadRequestDto requestDto = CapacidadRequestDto
                .builder().nombre(this.cadenaVacia).descripcion(this.descripcion)
                .listaTecnologia(List.of(new TecnologiaRequestDto())).build();

        Mono<CapacidadRequestDto> requestMono = Mono.just(requestDto);

        StepVerifier.create(capacidadService.guardar(requestMono))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals(MensajeError.DATOS_OBLIGATORIOS.formato(ConstantesAplicacion.NOMBRE)))
                .verify();
    }

    @Test
    void testValidarGuardarDescripcionVacia() {
        CapacidadRequestDto requestDto = CapacidadRequestDto
                .builder().nombre(this.nombre).descripcion(this.cadenaVacia)
                .listaTecnologia(List.of(new TecnologiaRequestDto())).build();

        Mono<CapacidadRequestDto> requestMono = Mono.just(requestDto);

        StepVerifier.create(capacidadService.guardar(requestMono))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals(MensajeError.DATOS_OBLIGATORIOS.formato(ConstantesAplicacion.DESCRIPCION)))
                .verify();
    }

    @Test
    void testValidarGuardarNombreLargo() {
        CapacidadRequestDto requestDto = CapacidadRequestDto
                .builder().nombre(String.format("%-51s", "Texto").replace(' ', 'a')).descripcion(this.descripcion)
                .listaTecnologia(List.of(new TecnologiaRequestDto())).build();

        Mono<CapacidadRequestDto> requestMono = Mono.just(requestDto);

        StepVerifier.create(capacidadService.guardar(requestMono))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals(MensajeError.LONGITUD_PERMITIDA.formato(ConstantesAplicacion.NOMBRE, ConstantesAplicacion.MAX_NOMBRE)))
                .verify();
    }

    @Test
    void testValidarGuardarDescripcionLarga() {
        CapacidadRequestDto requestDto = CapacidadRequestDto
                .builder().nombre(this.nombre)
                .descripcion(String.format("%-91s", "Texto").replace(' ', 'a'))
                .listaTecnologia(List.of(new TecnologiaRequestDto())).build();

        Mono<CapacidadRequestDto> requestMono = Mono.just(requestDto);

        StepVerifier.create(capacidadService.guardar(requestMono))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals(MensajeError.LONGITUD_PERMITIDA.formato(ConstantesAplicacion.DESCRIPCION, ConstantesAplicacion.MAX_DESCRIPCION)))
                .verify();
    }
}
