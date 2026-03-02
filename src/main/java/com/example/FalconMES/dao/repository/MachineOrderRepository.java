package com.example.FalconMES.dao.repository;

import com.example.FalconMES.dao.entity.MachineOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineOrderRepository extends JpaRepository<MachineOrderEntity, Long> {
}
