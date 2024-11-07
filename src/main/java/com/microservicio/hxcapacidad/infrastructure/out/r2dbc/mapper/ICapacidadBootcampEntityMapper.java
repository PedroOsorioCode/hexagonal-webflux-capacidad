package com.microservicio.hxcapacidad.infrastructure.out.r2dbc.mapper;

import com.microservicio.hxcapacidad.domain.model.CapacidadBootcampModel;
import com.microservicio.hxcapacidad.infrastructure.out.r2dbc.entity.CapacidadBootcampEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface ICapacidadBootcampEntityMapper {
    List<CapacidadBootcampEntity> toEntityFromModelList(List<CapacidadBootcampModel> listaCapacidadTecnologia);
    CapacidadBootcampModel toModelFromEntity(CapacidadBootcampEntity capacidadBootcamp);
}
