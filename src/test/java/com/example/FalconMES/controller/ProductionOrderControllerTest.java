package com.example.FalconMES.controller;


import com.example.FalconMES.dao.enums.ProductionOrderType;
import com.example.FalconMES.dto.ProductionOrderDto;
import com.example.FalconMES.dto.UpdateMachineOrderStatusDto;
import com.example.FalconMES.services.ProductionOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductionOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired(required = false)
    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private ProductionOrderService productionOrderService;

    private ProductionOrderDto productionOrderDto;
    private UpdateMachineOrderStatusDto updateStatusDto;


    @BeforeEach
    void setup() {

        productionOrderDto = ProductionOrderDto.builder()
                .id(100L)
                .orderNumber("PO-100")
                .orderName("Order-A")
                .machineType("CNC")
                .description("Test production order")
                .quantity(5)
                .totalStages(3)
                .orderType(ProductionOrderType.MASS_PRODUCTION)
                .currentStage(1)
                .status("PENDING")
                .build();

        updateStatusDto = UpdateMachineOrderStatusDto.builder()
                .machineOrderId(10L)
                .productionOrderId(100L)
                .status("COMPLETED")
                .build();
    }

    @Test
    void givenValidRequest_whenCreateProductionOrder_thenReturnDto() throws Exception {
        when(productionOrderService.createProductionOrder(any()))
                .thenReturn(productionOrderDto);

        mockMvc.perform(post("/api/v1/production-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productionOrderDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.orderNumber").value("PO-100"))
                .andExpect(jsonPath("$.orderName").value("Order-A"))
                .andExpect(jsonPath("$.quantity").value(5))
                .andExpect(jsonPath("$.orderType").value("MASS_PRODUCTION"))
                .andExpect(jsonPath("$.status").value("PENDING"));


    }

    @Test
    void givenValidStatusUpdate_whenUpdateMachineOrderStatus_thenReturnUpdatedDto() throws Exception {
        when(productionOrderService.updateMachineOrderStatus(any(UpdateMachineOrderStatusDto.class)))
                .thenReturn(productionOrderDto);

        mockMvc.perform(post("/api/v1/production-orders/updateStatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateStatusDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.orderNumber").value("PO-100"))
                .andExpect(jsonPath("$.orderName").value("Order-A"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

}
