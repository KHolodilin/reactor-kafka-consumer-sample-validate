package com.kholodilin.reactor.kafka.consumer.sample.validate.sample.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kholodilin.reactor.kafka.consumer.sample.validate.sample.encoding.StringEmptyToNullDeserializer;
import com.kholodilin.reactor.kafka.consumer.sample.validate.sample.encoding.ZonedDateTimeDeserializer;
import com.kholodilin.reactor.kafka.consumer.sample.validate.sample.encoding.ZonedDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;

@Configuration
public class ObjectMapperConfig {
    @Bean
    public ObjectMapper objectMapper() {
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer());
        module.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());
        module.addDeserializer(String.class, new StringEmptyToNullDeserializer());

        return JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                //.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true) Не работает :(
                .build().registerModule(module)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }
}
