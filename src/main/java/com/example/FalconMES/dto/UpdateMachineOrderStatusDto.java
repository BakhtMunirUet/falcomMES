package com.example.FalconMES.dto;


import lombok.Builder;

@Builder
public record UpdateMachineOrderStatusDto(

        Long productionOrderId,
        Long machineOrderId,
        String status

) {
}
