package com.example.FalconMES.dto;

import com.example.FalconMES.dao.enums.ProductionOrderType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record ProductionOrderDto(

        Long id,
        String orderNumber,

        String orderName,

        String machineType,

        String description,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity,

        @NotNull(message = "Order type is required")
        ProductionOrderType orderType,

        String status,

        List<MachineOrderDto> machineOrders
) {
}
