package com.kholodilin.reactor.kafka.consumer.sample.validate.sample.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kholodilin.reactor.kafka.consumer.sample.validate.sample.dto.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class KafkaMessageServiceImpl implements KafkaMessageService {
    private final ObjectMapper objectMapper;

    @Override
    public Mono<KafkaMessage> parse(String messageBody) {
        try {
            return Mono.just(objectMapper.readValue(messageBody, KafkaMessage.class));
        }
        catch(Throwable throwable) {
            return Mono.error(throwable);
        }
    }
}
