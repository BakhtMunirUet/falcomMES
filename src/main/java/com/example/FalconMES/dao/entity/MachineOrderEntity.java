package com.example.FalconMES.dao.entity;

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
@Table(name = "TBL_MACHINE_ORDER")
public class MachineOrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "STATUS")
    private String status = "PENDING";

    // Many MachineOrders belong to one ProductionOrder
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCTION_ORDER_ID")
    private ProductionOrderEntity productionOrder;

    // Many MachineOrders belong to one Machine
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MACHINE_ID")
    private MachineEntity machine;

}
