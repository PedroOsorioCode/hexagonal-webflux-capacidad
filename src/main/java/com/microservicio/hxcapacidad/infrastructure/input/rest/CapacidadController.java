package com.microservicio.hxcapacidad.infrastructure.input.rest;

import com.microservicio.hxcapacidad.application.dto.request.CapacidadRequestDto;
import com.microservicio.hxcapacidad.application.dto.response.CapacidadResponseDto;
import com.microservicio.hxcapacidad.application.service.ICapacidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/capacidad")
@RequiredArgsConstructor
public class CapacidadController {
    private final ICapacidadService capacidadService;

    @Operation(summary = "Validar la salud de la aplicación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Salud okey", content = @Content)
    })
    @GetMapping("/health")
    public Mono<String> health(){
        return Mono.just("ok");
    }

    @Operation(summary = "Registrar capacidad con las tecnologías")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Capacidad guardada correctamente", content = @Content),
            @ApiResponse(responseCode = "409", description = "Inconsistencia en la información", content = @Content)
    })
    @PostMapping
    public Mono<ResponseEntity<CapacidadResponseDto>> guardar(@RequestBody Mono<CapacidadRequestDto> capacidadRequestDto) {
        return capacidadService.guardar(capacidadRequestDto)
                .map(capacidadModel -> ResponseEntity.ok(capacidadModel))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
