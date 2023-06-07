package com.kholodilin.reactor.kafka.consumer.sample.validate.sample.dto;

import lombok.Builder;
import lombok.Data;
import reactor.kafka.receiver.ReceiverRecord;

@Data
@Builder
public class Package {
    private KafkaMessage message;
    private ReceiverRecord<String, String> record;
}
