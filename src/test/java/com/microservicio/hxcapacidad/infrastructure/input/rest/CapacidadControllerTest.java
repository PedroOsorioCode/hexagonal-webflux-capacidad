package com.microservicio.hxcapacidad.infrastructure.input.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = CapacidadController.class)
public class CapacidadControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testHealth() {
        webTestClient.get()
                .uri("/api/capacidad/health")  // Cambia la URI si es diferente
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("ok");
    }
}
