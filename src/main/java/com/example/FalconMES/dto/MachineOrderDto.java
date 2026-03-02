package com.example.FalconMES.dto;

import lombok.Builder;

@Builder
public record MachineOrderDto(

        Long id,
        int quantity,

        String status,

        MachineDto machine


) {
}
