package com.example.FalconMES.services.impl;


import com.example.FalconMES.dao.entity.MachineOrderEntity;
import com.example.FalconMES.dao.entity.ProductionOrderEntity;
import com.example.FalconMES.dao.mongo.event.MachineEvent;
import com.example.FalconMES.dao.mongo.repository.MachineEventRepository;
import com.example.FalconMES.dto.ProductionOrderDto;
import com.example.FalconMES.services.MachineEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MachineEventServiceImpl implements MachineEventService {

    private final MachineEventRepository machineEventRepository;

    @Override
    public List<MachineEvent> createAndSaveEvent(ProductionOrderDto dto) {
        List<MachineEvent> events = dto.machineOrders().stream()
                .map(mo -> MachineEvent.builder()
                        .id(UUID.randomUUID().toString())
                        .productionOrderId(String.valueOf(dto.id()))
                        .orderNumber(dto.orderNumber())
                        .machineId(String.valueOf(mo.machine().id()))
                        .status(mo.status())
                        .description(dto.description())
                        .timestamp(Instant.now())
                        .build())
                .collect(Collectors.toList());

        return machineEventRepository.saveAll(events);
    }
}
