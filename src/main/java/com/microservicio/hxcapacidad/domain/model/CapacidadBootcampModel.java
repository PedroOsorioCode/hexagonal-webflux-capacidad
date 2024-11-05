package com.microservicio.hxcapacidad.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapacidadBootcampModel {
    private Long id;
    private Long idCapacidad;
    private Long idBootcamp;

    public CapacidadBootcampModel(Long idCapacidad, Long idBootcamp) {
        this.idCapacidad = idCapacidad;
        this.idBootcamp = idBootcamp;
    }
}
