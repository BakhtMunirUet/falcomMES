package com.example.FalconMES.dao.mongo.repository;


import com.example.FalconMES.dao.mongo.event.MachineEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MachineEventRepository extends MongoRepository<MachineEvent, String> {

    Optional<MachineEvent> findByMachineOrderIdAndMachineOrderStatus(Long machineOrderId, String machineOrderStatus);
}
