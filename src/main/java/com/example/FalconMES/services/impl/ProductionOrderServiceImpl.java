package com.example.FalconMES.services.impl;


import com.example.FalconMES.dao.entity.MachineEntity;
import com.example.FalconMES.dao.entity.MachineOrderEntity;
import com.example.FalconMES.dao.entity.ProductionOrderEntity;
import com.example.FalconMES.dao.enums.OrderStatusType;
import com.example.FalconMES.dao.enums.ProductionOrderType;
import com.example.FalconMES.dao.mongo.event.MachineEvent;
import com.example.FalconMES.dao.mongo.repository.MachineEventRepository;
import com.example.FalconMES.dao.repository.MachineOrderRepository;
import com.example.FalconMES.dao.repository.MachineRepository;
import com.example.FalconMES.dao.repository.ProductionOrderRepository;
import com.example.FalconMES.dto.ProductionOrderDto;
import com.example.FalconMES.dto.UpdateMachineOrderStatusDto;
import com.example.FalconMES.mapper.ProductionOrderMapper;
import com.example.FalconMES.producer.MessageProducer;
import com.example.FalconMES.services.MachineEventService;
import com.example.FalconMES.services.ProductionOrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.mongodb.core.aggregation.MergeOperation.UniqueMergeId.id;

@Service
@RequiredArgsConstructor
public class ProductionOrderServiceImpl implements ProductionOrderService {

    private final ProductionOrderRepository productionOrderRepository;
    private final MachineRepository machineRepository;
    private final ProductionOrderMapper productionOrderMapper;
    private final MachineEventService machineEventService;
    private final MessageProducer messageProducer;



    @Override
    @Transactional
    public ProductionOrderDto createProductionOrder(ProductionOrderDto dto) {

        ProductionOrderEntity order;

        if (dto.id() == null){
            order = productionOrderMapper.dtoToEntity(dto);
            order.setOrderNumber(String.valueOf(UUID.randomUUID()));
            order.setStatus(String.valueOf(OrderStatusType.PENDING));
        }else {
           order = productionOrderRepository.findById(dto.id())
                   .orElseThrow(() -> new RuntimeException("Could not found order"));
        }



        List<MachineEntity> machines = machineRepository.findByType(dto.machineType());

        if (machines.isEmpty()) {
            throw new RuntimeException("No active machines available for assignment!");
        }

        int totalQty = dto.quantity();
        int perMachineQty = totalQty / machines.size();
        int remaining = totalQty;

        List<MachineOrderEntity> moList = new ArrayList<>();

        for (int i = 0; i < machines.size(); i++) {
            MachineEntity machine = machines.get(i);

            int qty = (i == machines.size() - 1) ? remaining : perMachineQty;
            remaining -= qty;

            MachineOrderEntity mo = MachineOrderEntity.builder()
                    .machine(machine)
                    .productionOrder(order)
                    .quantity(qty)
                    .status(String.valueOf(OrderStatusType.PENDING))
                    .build();

            moList.add(mo);

        }


        if(order.getMachineOrders() == null){
            order.setMachineOrders(moList);
        }else {
            order.getMachineOrders().addAll(moList);
        }


        var responseDto = productionOrderMapper.entityToDto(
                productionOrderRepository.save(order));
        messageProducer.send(responseDto);
        return responseDto;
    }

    @Override
    public ProductionOrderDto updateMachineOrderStatus(UpdateMachineOrderStatusDto dto) {

        ProductionOrderEntity orderEntity =
                productionOrderRepository.findById(dto.productionOrderId()).
                        orElseThrow(() -> new RuntimeException("Could not found order"));


        MachineOrderEntity machineOrder = orderEntity.getMachineOrders().stream()
                .filter(mo -> mo.getId().equals(dto.machineOrderId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Machine order not found in this production order"));

        machineOrder.setStatus(dto.status());
        orderEntity.setStatus(dto.orderStatus());

        var responseDto = productionOrderMapper.entityToDto(
                productionOrderRepository.save(orderEntity));
        messageProducer.send(responseDto);
        return responseDto;
    }


}
