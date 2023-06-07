package com.kholodilin.reactor.kafka.consumer.sample.validate.sample.service;

import com.kholodilin.reactor.kafka.consumer.sample.validate.sample.dto.Package;
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
                .<Package>handle((record, synchronousSink) -> {
                    synchronousSink.next(Package
                            .builder()
                            .record(record)
                            .build());
                })
                .concatMap(pkg -> kafkaMessageService
                        .parse(pkg.getRecord().value())
                        .<Package>handle((kafkaMessage, synchronousSink) -> {
                            pkg.setMessage(kafkaMessage);
                            synchronousSink.next(pkg);
                        })
                        .onErrorResume(throwable -> {
                            log.error("Parsing error", throwable);
                            return pkg.getRecord().receiverOffset()
                                    .commit()
                                    .thenReturn(pkg);
                        })
                )
                .filter(pkg -> pkg.getMessage() == null)
                .concatMap(pkg -> pkg.getRecord()
                        .receiverOffset()
                        .commit())
                .subscribe();
    }
}
