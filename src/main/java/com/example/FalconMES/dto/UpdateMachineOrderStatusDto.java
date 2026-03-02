package com.example.FalconMES.dto;


import lombok.Builder;

@Builder
public record UpdateMachineOrderStatusDto(

        Long productionOrderId,
        Long machineId,

        Long machineOrderId,

        String status,

        String orderStatus



) {
}
