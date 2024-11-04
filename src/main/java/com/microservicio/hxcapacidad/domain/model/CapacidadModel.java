package com.microservicio.hxcapacidad.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapacidadModel {
    private Long id;
    private String nombre;
    private String descripcion;
    private int nrotecnologia;
}
