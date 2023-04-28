package com.kholodilin.reactor.kafka.consumer.sample.validate.sample;

import com.kholodilin.reactor.kafka.consumer.sample.validate.sample.service.KafkaConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RunAfterStartup {

    private final KafkaConsumerService kafkaConsumerService;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        kafkaConsumerService.receive();
    }
}
