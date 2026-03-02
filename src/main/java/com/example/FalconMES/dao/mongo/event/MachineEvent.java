package com.example.FalconMES.dao.mongo.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;


@Setter
@Getter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "MachineEvent")
public class MachineEvent {

    @Id
    @JsonProperty
    private String id;
    @JsonProperty
    private String productionOrderId;
    @JsonProperty
    private String orderNumber;
    @JsonProperty
    private String machineId;
    @JsonProperty
    private String status;
    @JsonProperty
    private String description;
    @JsonProperty
    private Instant timestamp;
}
