package com.spring.training.config;

import brave.context.log4j2.ThreadContextScopeDecorator;
import brave.propagation.CurrentTraceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    CurrentTraceContext.ScopeDecorator decorator() {
        return ThreadContextScopeDecorator.get();
    }

}
