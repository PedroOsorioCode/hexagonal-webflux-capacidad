package com.microservicio.hxcapacidad.infrastructure.out.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "capacidad_bootcamp")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapacidadBootcampEntity {
    private Long id;
    private Long idCapacidad;
    private Long idBootcamp;
}
