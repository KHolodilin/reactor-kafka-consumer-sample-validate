package com.kholodilin.reactor.kafka.consumer.sample.validate.sample.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.io.File;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapAddress;
    @Value("${kafka.ssl.enabled}")
    private Boolean sslEnabled;
    @Value("${kafka.ssl.keystore.type}")
    private String keyStoreType;
    @Value("${kafka.ssl.keystore.password}")
    private String keyStorePassword;
    @Value("${kafka.ssl.key.password}")
    private String keyPassword;
    @Value("${kafka.ssl.keystore.location}")
    private String keyStoreLocation;
    @Value("${kafka.ssl.truststore.type}")
    private String trustStoreType;
    @Value("${kafka.ssl.truststore.password}")
    private String trustStorePassword;
    @Value("${kafka.ssl.truststore.location}")
    private String trustStoreLocation;
    @Value("${kafka.consumer.enable-auto-commit}")
    private Boolean enableAutoCommit;
    @Value("${kafka.consumer.group-id}")
    private String consumerGroupId;

    @Value("${kafka.topics}")
    private String topic;

    @Value("${kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Bean
    public ReceiverOptions<String, String> receiverOptions() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);

        if (sslEnabled) {
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
            props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, new File(trustStoreLocation).getAbsolutePath());
            props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, trustStorePassword);
            props.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, trustStoreType);
            props.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, keyStoreType);
            props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, new File(keyStoreLocation).getAbsolutePath());
            props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, keyStorePassword);
            props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, keyPassword);
            props.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");
        }

        return ReceiverOptions.<String, String>create(props)
                .subscription(Collections.singletonList(topic))
                .commitInterval(Duration.ZERO) // Disable periodic commits
                .commitBatchSize(0); // Disable commits by batch size
    }
    @Bean
    public KafkaReceiver<String, String> kafkaReceiver(
            ReceiverOptions<String, String> kafkaReceiverOptions) {

        return KafkaReceiver.create(kafkaReceiverOptions);
    }
}


