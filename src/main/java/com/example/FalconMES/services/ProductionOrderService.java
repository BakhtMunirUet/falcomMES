package com.example.FalconMES.services;

import com.example.FalconMES.dao.entity.ProductionOrderEntity;
import com.example.FalconMES.dto.ProductionOrderDto;
import com.example.FalconMES.dto.UpdateMachineOrderStatusDto;

public interface ProductionOrderService {


    ProductionOrderDto createProductionOrder(ProductionOrderDto dto);

    ProductionOrderDto updateMachineOrderStatus(UpdateMachineOrderStatusDto dto);


}
