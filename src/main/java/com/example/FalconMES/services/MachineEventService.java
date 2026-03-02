package com.example.FalconMES.services;

import com.example.FalconMES.dao.entity.ProductionOrderEntity;
import com.example.FalconMES.dao.mongo.event.MachineEvent;
import com.example.FalconMES.dto.ProductionOrderDto;

import java.util.List;

public interface MachineEventService {


    List<MachineEvent> createAndSaveEvent(ProductionOrderDto dto);
}
