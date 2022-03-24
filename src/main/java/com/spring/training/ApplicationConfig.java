package com.spring.training;

import brave.context.log4j2.ThreadContextScopeDecorator;
import brave.propagation.CurrentTraceContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ConditionalOnProperty(prefix = "spring", name = "cache.type", havingValue = "redis")
@Configuration
@Import({ RedisAutoConfiguration.class })
public class ApplicationConfig {

    @Bean
    CurrentTraceContext.ScopeDecorator decorator() {
        return ThreadContextScopeDecorator.get();
    }

}
