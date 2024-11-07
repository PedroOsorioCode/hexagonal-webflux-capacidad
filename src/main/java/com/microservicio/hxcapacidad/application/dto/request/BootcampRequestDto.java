package com.microservicio.hxcapacidad.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BootcampRequestDto {
    private String nombre;
    private String descripcion;
    private List<BootcampCapacidadRequestDto> listaCapacidad;
}
