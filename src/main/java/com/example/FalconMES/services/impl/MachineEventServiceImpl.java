package com.example.FalconMES.services.impl;


import com.example.FalconMES.dao.entity.MachineOrderEntity;
import com.example.FalconMES.dao.entity.ProductionOrderEntity;
import com.example.FalconMES.dao.mongo.event.MachineEvent;
import com.example.FalconMES.dao.mongo.repository.MachineEventRepository;
import com.example.FalconMES.dto.MachineOrderDto;
import com.example.FalconMES.dto.ProductionOrderDto;
import com.example.FalconMES.services.MachineEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MachineEventServiceImpl implements MachineEventService {

    private final MachineEventRepository machineEventRepository;

    @Override
    public List<MachineEvent> createAndSaveEvent(ProductionOrderDto dto) {
        List<MachineEvent> machineEventList = new ArrayList<>();
        for (MachineOrderDto mo: dto.machineOrders()){
           var output = machineEventRepository.findByMachineOrderIdAndMachineOrderStatus(mo.id(), mo.status());
            if(output.isEmpty() || output == null){
                MachineEvent machineEvent = MachineEvent.builder()
                        .id(UUID.randomUUID().toString())
                        .productionOrderId(dto.id())
                        .orderNumber(dto.orderNumber())
                        .currentStage(dto.currentStage())
                        .totalStages(dto.totalStages())
                        .orderStatus(dto.status())
                        .machineOrderId(mo.id())
                        .machineId(mo.machine().id())
                        .machineOrderStatus(mo.status())
                        .description(dto.description())
                        .timestamp(Instant.now())
                        .build();
                machineEventList.add(machineEvent);
            }
        }

        return machineEventRepository.saveAll(machineEventList);
    }
}
