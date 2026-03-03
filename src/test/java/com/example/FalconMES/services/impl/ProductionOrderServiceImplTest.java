package com.example.FalconMES.services.impl;


import com.example.FalconMES.dao.entity.MachineOrderEntity;
import com.example.FalconMES.dao.entity.ProductionOrderEntity;
import com.example.FalconMES.dao.enums.OrderStatusType;
import com.example.FalconMES.dao.enums.ProductionOrderType;
import com.example.FalconMES.dao.repository.ProductionOrderRepository;
import com.example.FalconMES.dto.MachineOrderDto;
import com.example.FalconMES.dto.ProductionOrderDto;
import com.example.FalconMES.dto.UpdateMachineOrderStatusDto;
import com.example.FalconMES.mapper.ProductionOrderMapper;
import com.example.FalconMES.producer.MessageProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductionOrderServiceImplTest {

    @InjectMocks
    private ProductionOrderServiceImpl service;

    @Mock
    private ProductionOrderRepository productionOrderRepository;

    @Mock
    private ProductionOrderMapper productionOrderMapper;

    @Mock
    private MessageProducer messageProducer;

    private ProductionOrderEntity entity;
    private ProductionOrderDto dto;
    private MachineOrderEntity machineOrderEntity;
    private MachineOrderDto machineOrderDto;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Machine order entity
        machineOrderEntity = new MachineOrderEntity();
        machineOrderEntity.setId(1L);
        machineOrderEntity.setStage(1);
        machineOrderEntity.setStatus(OrderStatusType.PENDING.toString());
        machineOrderEntity.setQuantity(5);

        // Machine order DTO
        machineOrderDto = MachineOrderDto.builder()
                .id(1L)
                .stage(1)
                .status(OrderStatusType.PENDING.toString())
                .quantity(5)
                .build();

        // Production order entity
        entity = new ProductionOrderEntity();
        entity.setId(100L);
        entity.setOrderName("Order-A");
        entity.setDescription("Test order");
        entity.setQuantity(5);
        entity.setCurrentStage(1);
        entity.setTotalStages(3);
        entity.setOrderType(ProductionOrderType.MASS_PRODUCTION);
        entity.setMachineOrders(List.of(machineOrderEntity));
        entity.setStatus(OrderStatusType.PENDING.toString());

        // Production order DTO
        dto = ProductionOrderDto.builder()
                .id(100L)
                .orderName("Order-A")
                .description("Test order")
                .quantity(5)
                .currentStage(1)
                .totalStages(3)
                .orderType(ProductionOrderType.MASS_PRODUCTION)
                .status(OrderStatusType.PENDING.toString())
                .machineOrders(List.of(machineOrderDto))
                .build();

        // Mapper mocks
        when(productionOrderMapper.dtoToEntity(any(ProductionOrderDto.class))).thenReturn(entity);
        when(productionOrderMapper.entityToDto(any(ProductionOrderEntity.class))).thenReturn(dto);

        // Repository save mock returns the entity
        when(productionOrderRepository.save(any(ProductionOrderEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }


    @Test
    void testCreateProductionOrder() {
        ProductionOrderDto result = service.createProductionOrder(dto);

        assertNotNull(result);
        assertEquals(dto, result);

        // Machine order entity should now reference production order
        assertEquals(entity, entity.getMachineOrders().get(0).getProductionOrder());

        // Check status and orderNumber
        assertEquals(OrderStatusType.PENDING.toString(), entity.getStatus());
        assertNotNull(entity.getOrderNumber());

        verify(productionOrderRepository).save(entity);
        verify(messageProducer).send(result);
    }

    @Test
    void testUpdateMachineOrderStatus_AllCompleted() {
        // setup machine order as RUNNING
        machineOrderEntity.setStatus(OrderStatusType.RUNNING.toString());

        UpdateMachineOrderStatusDto updateDto = new UpdateMachineOrderStatusDto(
                entity.getId(),
                machineOrderEntity.getId(),
                OrderStatusType.COMPLETED.toString()
        );

        when(productionOrderRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        ProductionOrderDto result = service.updateMachineOrderStatus(updateDto);

        assertEquals(dto, result);

        // All machine orders completed → production order status COMPLETED
        assertEquals(OrderStatusType.COMPLETED.toString(), entity.getStatus());

        verify(messageProducer).send(result);
        verify(productionOrderRepository).save(entity);
    }

    @Test
    void testUpdateMachineOrderStatus_AlreadyUpdated() {
        // Machine order already COMPLETED
        machineOrderEntity.setStatus(OrderStatusType.COMPLETED.toString());

        UpdateMachineOrderStatusDto updateDto = new UpdateMachineOrderStatusDto(
                entity.getId(),
                machineOrderEntity.getId(),
                OrderStatusType.COMPLETED.toString()
        );

        when(productionOrderRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateMachineOrderStatus(updateDto));

        assertEquals("Machine order status already updated", ex.getMessage());
    }

    @Test
    void testUpdateMachineOrderStatus_MachineOrderNotFound() {
        UpdateMachineOrderStatusDto updateDto = new UpdateMachineOrderStatusDto(
                entity.getId(),
                999L, // non-existent machine order
                OrderStatusType.COMPLETED.toString()
        );

        when(productionOrderRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateMachineOrderStatus(updateDto));

        assertEquals("Machine order not found in this production order", ex.getMessage());
    }

    @Test
    void testUpdateMachineOrderStatus_OrderNotFound() {
        UpdateMachineOrderStatusDto updateDto = new UpdateMachineOrderStatusDto(
                999L,// non-existent production order
                1L,
                OrderStatusType.COMPLETED.toString()
        );

        when(productionOrderRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateMachineOrderStatus(updateDto));

        assertEquals("Could not found order", ex.getMessage());
    }


}
