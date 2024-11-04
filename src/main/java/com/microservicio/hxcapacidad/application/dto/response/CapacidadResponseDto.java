package com.microservicio.hxcapacidad.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CapacidadResponseDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private int cantidadTecnologia;
    private List<TecnologiaResponseDto> listaTecnologias;
}
