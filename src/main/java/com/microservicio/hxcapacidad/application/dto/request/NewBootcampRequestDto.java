package com.microservicio.hxcapacidad.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewBootcampRequestDto {
    private String nombre;
    private String descripcion;
    private int cantidadCapacidad;
}
