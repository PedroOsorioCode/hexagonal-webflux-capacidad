package com.microservicio.hxcapacidad.infrastructure.out.r2dbc.mapper;

import com.microservicio.hxcapacidad.domain.model.CapacidadModel;
import com.microservicio.hxcapacidad.infrastructure.out.r2dbc.entity.CapacidadEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface ICapacidadEntityMapper {
    CapacidadEntity toEntityFromModel(CapacidadModel capacidadModel);
    CapacidadModel toModelFromEntity(CapacidadEntity objectEntity);
}
