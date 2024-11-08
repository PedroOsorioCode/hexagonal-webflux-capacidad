package com.microservicio.hxcapacidad.infrastructure.input.rest;

import com.microservicio.hxcapacidad.application.dto.request.BootcampRequestDto;
import com.microservicio.hxcapacidad.application.dto.request.CapacidadFilterRequestDto;
import com.microservicio.hxcapacidad.application.dto.request.CapacidadRequestDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampCapacidadResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.BootcampPaginacionResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.CapacidadPaginacionResponseDto;
import com.microservicio.hxcapacidad.application.dto.response.CapacidadResponseDto;
import com.microservicio.hxcapacidad.application.service.IBootcampCapacidadService;
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
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CapacidadController {
    private final ICapacidadService capacidadService;
    private final IBootcampCapacidadService bootcampCapacidadService;

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
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/listar")
    public Mono<ResponseEntity<CapacidadPaginacionResponseDto<CapacidadResponseDto>>> consultarTodos(
            @RequestBody Mono<CapacidadFilterRequestDto> capacidadFilterRequestDTO) {

        return capacidadService.consultarTodosPaginado(capacidadFilterRequestDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @Operation(summary = "Registrar bootcamp con las capacidades")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bootcamp guardada correctamente", content = @Content),
            @ApiResponse(responseCode = "409", description = "Inconsistencia en la información", content = @Content)
    })
    @PostMapping("/registrar-bootcamp")
    public Mono<ResponseEntity<BootcampCapacidadResponseDto>> guardarBootcamp(@RequestBody Mono<BootcampRequestDto> bootcampRequestDto) {
        return bootcampCapacidadService.guardarBootcamp(bootcampRequestDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/listar-bootcamp")
    public Mono<ResponseEntity<BootcampPaginacionResponseDto<BootcampCapacidadResponseDto>>> consultarBootcampTodos(
            @RequestBody Mono<CapacidadFilterRequestDto> capacidadFilterRequestDTO) {

        return bootcampCapacidadService.consultarBootcampTodosPaginado(capacidadFilterRequestDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
}
