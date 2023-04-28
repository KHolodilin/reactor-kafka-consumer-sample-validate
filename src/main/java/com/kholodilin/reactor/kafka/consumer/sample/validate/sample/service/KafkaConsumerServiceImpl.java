package com.kholodilin.reactor.kafka.consumer.sample.validate.sample.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.kafka.receiver.KafkaReceiver;
import javax.annotation.PostConstruct;


@RequiredArgsConstructor
@Slf4j
@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService {
    private final KafkaReceiver<String, String> kafkaReceiver;
    private final KafkaMessageService kafkaMessageService;
    @PostConstruct
    private void checkProperties() {
        Assert.notNull(kafkaReceiver, "A kafkaReceiver must be provided");
    }

    public void receive() {
        kafkaReceiver.receive()
                .concatMap(record -> kafkaMessageService
                        .parse(record.value())
                        .thenReturn(record)
                )
                .concatMap(record -> record.receiverOffset().commit())
                .subscribe();
    }
}
