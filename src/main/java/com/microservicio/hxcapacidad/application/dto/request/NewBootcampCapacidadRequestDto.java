package com.microservicio.hxcapacidad.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewBootcampCapacidadRequestDto {
    private NewBootcampRequestDto nuevoBootcamp;
    private List<BootcampCapacidadRequestDto> listaCapacidad;
}
