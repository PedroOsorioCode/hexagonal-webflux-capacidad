package com.microservicio.hxcapacidad.application.mapper;

import com.microservicio.hxcapacidad.application.dto.response.CapacidadResponseDto;
import com.microservicio.hxcapacidad.domain.model.CapacidadBootcampModel;
import com.microservicio.hxcapacidad.domain.model.CapacidadModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ICapacidadBootcampModelMapper {
    List<CapacidadResponseDto> toResponseFromModelList(List<CapacidadBootcampModel> capacidadBootcampModel);
}
