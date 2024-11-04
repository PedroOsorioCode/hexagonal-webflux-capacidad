package com.microservicio.hxcapacidad.application.mapper;

import com.microservicio.hxcapacidad.application.dto.request.CapacidadRequestDto;
import com.microservicio.hxcapacidad.application.dto.response.CapacidadResponseDto;
import com.microservicio.hxcapacidad.domain.model.CapacidadModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICapacidadModelMapper {
    CapacidadModel toModelFromRequest(CapacidadRequestDto capacidadRequestDto);
    CapacidadResponseDto toResponseFromModel(CapacidadModel capacidadModel);
}
