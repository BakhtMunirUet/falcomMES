package com.example.FalconMES.mapper;


import com.example.FalconMES.dao.entity.ProductionOrderEntity;
import com.example.FalconMES.dto.ProductionOrderDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductionOrderMapper {

    ProductionOrderEntity dtoToEntity(ProductionOrderDto dto);

    ProductionOrderDto entityToDto(ProductionOrderEntity entity);
}
