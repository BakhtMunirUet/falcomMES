package com.example.FalconMES.controller;


import com.example.FalconMES.dao.entity.ProductionOrderEntity;
import com.example.FalconMES.dto.ProductionOrderDto;
import com.example.FalconMES.dto.UpdateMachineOrderStatusDto;
import com.example.FalconMES.services.ProductionOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/production-orders")
@RequiredArgsConstructor
public class ProductionOrderController {

    private final ProductionOrderService productionOrderService;


    /**
     * Create a new production order and assign it to machines
     */
    @PostMapping
    public ResponseEntity<ProductionOrderDto> createProductionOrder(
            @Valid @RequestBody ProductionOrderDto dto) {
        return ResponseEntity.ok(productionOrderService.createProductionOrder(dto));
    }


    @PostMapping("/updateStatus")
    public ResponseEntity<ProductionOrderDto> updateMachineOrderStatus(
            @Valid @RequestBody UpdateMachineOrderStatusDto dto) {
        return ResponseEntity.ok(productionOrderService.updateMachineOrderStatus(dto));
    }
}
