package com.example.FalconMES.dao.entity;

import com.example.FalconMES.dao.enums.ProductionOrderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "TBL_PRODUCTION_ORDER")
public class ProductionOrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ORDER_NUMBER", nullable = false)
    private String orderNumber;

    @Column(name = "ORDER_NAME", length = 255)
    private String orderName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @Column(name = "TOTAL_STAGES", nullable = false)
    private Integer totalStages;

    @Column(name = "CURRENT_STAGE", nullable = false)
    private Integer currentStage;

    @Enumerated(EnumType.STRING)
    @Column(name = "ORDER_TYPE", nullable = false)
    private ProductionOrderType orderType;

    @Column(name = "STATUS")
    private String status = "PENDING";

    // One ProductionOrder has many MachineOrders
    @OneToMany(mappedBy = "productionOrder", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MachineOrderEntity> machineOrders = new ArrayList<>();
}