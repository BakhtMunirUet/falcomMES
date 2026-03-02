package com.example.FalconMES.dao.repository;


import com.example.FalconMES.dao.entity.MachineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineRepository extends JpaRepository<MachineEntity, Long> {

    List<MachineEntity> findByType(String machineType);
}
