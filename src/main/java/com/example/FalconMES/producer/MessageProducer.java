package com.example.FalconMES.producer;


import com.example.FalconMES.dao.entity.ProductionOrderEntity;
import com.example.FalconMES.dto.ProductionOrderDto;
import com.example.FalconMES.services.MachineEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.exchange.name}")
    private String exchange;

    @Value("${app.routing.key}")
    private String routingKey;

    public void send(ProductionOrderDto message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);

        System.out.println("Sent: " + message);
    }
}
