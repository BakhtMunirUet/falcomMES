package com.example.FalconMES.dao.mongo.repository;


import com.example.FalconMES.dao.mongo.event.MachineEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MachineEventRepository extends MongoRepository<MachineEvent, String> {
}
