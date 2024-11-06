package com.microservicio.hxcapacidad.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CapacidadResponseDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private int cantidadTecnologia;
    private List<TecnologiaResponseDto> listaTecnologias;

    public CapacidadResponseDto(Long id, String nombre, String descripcion, int cantidadTecnologia) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidadTecnologia = cantidadTecnologia;
    }
}
