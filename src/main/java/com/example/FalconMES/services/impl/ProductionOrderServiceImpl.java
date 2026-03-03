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
import com.example.FalconMES.services.ProductionOrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProductionOrderServiceImpl implements ProductionOrderService {

    private final ProductionOrderRepository productionOrderRepository;
    private final MachineRepository machineRepository;
    private final ProductionOrderMapper productionOrderMapper;
    private final MessageProducer messageProducer;



    @Override
    @Transactional
    public ProductionOrderDto createProductionOrder(ProductionOrderDto dto) {

        ProductionOrderEntity order = productionOrderMapper.dtoToEntity(dto);
        order.setOrderNumber(String.valueOf(UUID.randomUUID()));
        order.setStatus(String.valueOf(OrderStatusType.PENDING));

        order.getMachineOrders().forEach(mo -> mo.setProductionOrder(order));


//        ProductionOrderEntity order;
//
//        if (dto.id() == null){
//            order = productionOrderMapper.dtoToEntity(dto);
//            order.setOrderNumber(String.valueOf(UUID.randomUUID()));
//            order.setStatus(String.valueOf(OrderStatusType.PENDING));
//        }else {
//           order = productionOrderRepository.findById(dto.id())
//                   .orElseThrow(() -> new RuntimeException("Could not found order"));
//        }
//
//
//
//        List<MachineEntity> machines = machineRepository.findByType(dto.machineType());
//
//        if (machines.isEmpty()) {
//            throw new RuntimeException("No active machines available for assignment!");
//        }
//
//        int totalQty = dto.quantity();
//        int perMachineQty = totalQty / machines.size();
//        int remaining = totalQty;
//
//        List<MachineOrderEntity> moList = new ArrayList<>();
//
//        for (int i = 0; i < machines.size(); i++) {
//            MachineEntity machine = machines.get(i);
//
//            int qty = (i == machines.size() - 1) ? remaining : perMachineQty;
//            remaining -= qty;
//
//            MachineOrderEntity mo = MachineOrderEntity.builder()
//                    .machine(machine)
//                    .productionOrder(order)
//                    .quantity(qty)
//                    .status(String.valueOf(OrderStatusType.PENDING))
//                    .build();
//
//            moList.add(mo);
//
//        }
//
//
//        if(order.getMachineOrders() == null){
//            order.setMachineOrders(moList);
//        }else {
//            order.getMachineOrders().addAll(moList);
//        }


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

        if(machineOrder.getStatus().equals(dto.status())){
            throw new RuntimeException("Machine order status already updated");
        }

        machineOrder.setStatus(dto.status());


        List<MachineOrderEntity> stageOrders =
                orderEntity.getMachineOrders().stream()
                        .filter(mo -> mo.getStage() == machineOrder.getStage())
                        .toList();

        boolean allCompleted = stageOrders.stream()
                .allMatch(mo -> mo.getStatus().equals(String.valueOf(OrderStatusType.COMPLETED)));

        if (allCompleted) {
            int nextStage = orderEntity.getCurrentStage() + 1;
            orderEntity.setCurrentStage(nextStage);

        }

        boolean allMachineOrdersCompleted =
                orderEntity.getMachineOrders().stream()
                        .allMatch(mo -> mo.getStatus().equals(String.valueOf(OrderStatusType.COMPLETED)));


        if(allMachineOrdersCompleted){
            orderEntity.setStatus(String.valueOf(OrderStatusType.COMPLETED));
        }
        else {
            orderEntity.setStatus(String.valueOf(OrderStatusType.RUNNING));
        }



        var responseDto = productionOrderMapper.entityToDto(
                productionOrderRepository.save(orderEntity));
        messageProducer.send(responseDto);
        return responseDto;
    }


}
