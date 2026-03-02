package com.example.FalconMES.dto;


import lombok.Builder;

@Builder
public record MachineDto(

        Long id,

        String name,

        String type,

        String status
) {
}
