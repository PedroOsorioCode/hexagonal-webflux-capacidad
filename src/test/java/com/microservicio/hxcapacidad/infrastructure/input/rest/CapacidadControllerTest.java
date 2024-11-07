package com.microservicio.hxcapacidad.infrastructure.input.rest;

import com.microservicio.hxcapacidad.application.dto.request.CapacidadFilterRequestDto;
import com.microservicio.hxcapacidad.application.dto.request.CapacidadRequestDto;
import com.microservicio.hxcapacidad.application.dto.response.CapacidadPaginacionResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.CapacidadResponseDto;
import com.microservicio.hxcapacidad.application.service.impl.BootcampCapacidadService;
import com.microservicio.hxcapacidad.application.service.impl.CapacidadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebFluxTest(controllers = CapacidadController.class)
class CapacidadControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private CapacidadService capacidadService;
    @MockBean
    private BootcampCapacidadService bootcampCapacidadService;

    private String nombre = "test";
    private String descripcion = "testd";

    @Test
    void testHealth() {
        webTestClient.get()
                .uri("/api/capacidad/health")  // Cambia la URI si es diferente
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("ok");
    }

    @Test
    void testGuardarExitoso() {
        // Preparar datos de prueba
        CapacidadRequestDto requestDto = CapacidadRequestDto
                .builder().nombre(this.nombre).descripcion(this.descripcion).build();

        CapacidadResponseDto responseDto = CapacidadResponseDto
                .builder().nombre(this.nombre).descripcion(this.descripcion).build();

        // Mockear el comportamiento del servicio para que devuelva una respuesta exitosa
        when(capacidadService.guardar(any(Mono.class))).thenReturn(Mono.just(responseDto));

        // Ejecutar la prueba
        webTestClient.post()
                .uri("/api/capacidad") // Cambia el URI si es diferente
                .contentType(APPLICATION_JSON)
                .body(Mono.just(requestDto), CapacidadRequestDto.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testListarExitoso() {
        CapacidadFilterRequestDto filterDto = new CapacidadFilterRequestDto(); // Configura el filtro según sea necesario
        CapacidadResponseDto responseDto = new CapacidadResponseDto(); // Configura la respuesta según sea necesario
        CapacidadPaginacionResponseDto<CapacidadResponseDto> paginacionResponseDto = new CapacidadPaginacionResponseDto<>();
        paginacionResponseDto.setContent(List.of(responseDto)); // Agrega elementos a la paginación

        when(capacidadService.consultarTodosPaginado(any())).thenReturn(Mono.just(paginacionResponseDto));

        webTestClient.post()
                .uri("/api/capacidad/listar") // Cambia el URI si es diferente
                .contentType(APPLICATION_JSON)
                .body(Mono.just(filterDto), CapacidadFilterRequestDto.class)
                .exchange()
                .expectStatus().isOk();
    }
}
