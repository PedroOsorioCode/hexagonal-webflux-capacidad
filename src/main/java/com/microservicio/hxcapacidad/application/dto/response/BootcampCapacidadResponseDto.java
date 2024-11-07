package com.microservicio.hxcapacidad.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BootcampCapacidadResponseDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private int cantidadCapacidad;
    private List<CapacidadResponseDto> listaCapacidades;

    public BootcampCapacidadResponseDto(Long id, String nombre, String descripcion, int cantidadCapacidad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidadCapacidad = cantidadCapacidad;
    }
}
