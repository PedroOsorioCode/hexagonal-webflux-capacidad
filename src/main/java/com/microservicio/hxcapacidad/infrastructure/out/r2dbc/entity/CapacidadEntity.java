package com.microservicio.hxcapacidad.infrastructure.out.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "capacidades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapacidadEntity {
    @Id
    private Long id;
    private String nombre;
    private String descripcion;
    private int nrotecnologia;
}
