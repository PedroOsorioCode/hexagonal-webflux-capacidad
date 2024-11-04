package com.microservicio.hxcapacidad.application.dto.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapacidadTecnologiaRequestDto {
    private Long idCapacidad;
    private List<Long> idTecnologia;
}
