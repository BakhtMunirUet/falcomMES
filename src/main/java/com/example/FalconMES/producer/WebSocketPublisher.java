package com.example.FalconMES.producer;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public void publishToWebSocket(Object event) {
        messagingTemplate.convertAndSend("/topic/events", event);
        System.out.println("Sent to WebSocket: " + event);
    }
}
