package com.kholodilin.reactor.kafka.consumer.sample.validate.sample.service;


import com.kholodilin.reactor.kafka.consumer.sample.validate.sample.dto.KafkaMessage;
import reactor.core.publisher.Mono;

public interface KafkaMessageService {
    Mono<KafkaMessage> parse(String messageBody);
}
