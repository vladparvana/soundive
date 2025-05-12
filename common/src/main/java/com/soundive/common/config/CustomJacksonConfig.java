package com.soundive.common.config;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.soundive.common.annotation.ExcludeFromDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Configures Jackson to:
 * - exclude fields annotated with @ExcludeFromDto
 * - support LocalDate and LocalDateTime
 * - use ISO format for date/time
 */
@Configuration
public class CustomJacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // 1. Support for java.time.*
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // use ISO-8601

        // 2. Custom module to exclude @ExcludeFromDto
        SimpleModule excludeModule = new SimpleModule();
        excludeModule.setSerializerModifier(new CustomBeanSerializerModifier());
        mapper.registerModule(excludeModule);

        return mapper;
    }

    static class CustomBeanSerializerModifier extends BeanSerializerModifier {
        @Override
        public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
            return beanProperties.stream()
                    .filter(beanProperty -> beanProperty.getAnnotation(ExcludeFromDto.class) == null)
                    .collect(Collectors.toList());
        }
    }
}
