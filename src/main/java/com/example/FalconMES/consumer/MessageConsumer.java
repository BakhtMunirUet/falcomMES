package com.example.FalconMES.consumer;


import com.example.FalconMES.dao.entity.ProductionOrderEntity;
import com.example.FalconMES.dao.mongo.event.MachineEvent;
import com.example.FalconMES.dto.ProductionOrderDto;
import com.example.FalconMES.producer.WebSocketPublisher;
import com.example.FalconMES.services.MachineEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageConsumer {

    private final MachineEventService machineEventService;
    private final WebSocketPublisher webSocketPublisher;

    @RabbitListener(queues = "demoQueue")
    public void receive(ProductionOrderDto message) {

        try {

            List<MachineEvent> machineEventList = machineEventService.createAndSaveEvent(message);
            webSocketPublisher.publishToWebSocket(machineEventList);
            System.out.println("Received: " + message);

        }catch (Exception e) {
            e.printStackTrace(); // logs full exception
        }

    }
}
