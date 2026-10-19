# reactor-kafka consumer, manual offset commit

![Java](https://img.shields.io/badge/Java-11-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.11-brightgreen)

Small Spring Boot app that reads Kafka with reactor-kafka. `KafkaReceiver` takes messages from the topic and commits the offset manually. SSL is in the config, off by default.

Java 11, Spring Boot 2.7.

## Run

Start Kafka:

```bash
docker compose -f docker/docker-compose.yml up -d
```

The compose file creates the topic `sample-validate`. Then:

```bash
./mvnw spring-boot:run
```

The app comes up on port 8888. Broker is `localhost:9092`, consumer group is `sample-validate`.

Send a message:

```bash
docker exec -it sample-validate-broker kafka-console-producer --bootstrap-server broker:29092 --topic sample-validate
```

```json
{"id":"1"}
```

## Config

All of it is under `kafka` in `src/main/resources/application.yml`.

- `bootstrap-servers` is the broker
- `topics` is the topic name
- `consumer.group-id` is the consumer group
- `consumer.enable-auto-commit` is `false`, so the app commits the offset itself
- `ssl.enabled` is `false` for a local broker. Turn it on and fill in the keystore and truststore when the broker asks for SSL

The receiver starts once the app is up, in `RunAfterStartup`.
