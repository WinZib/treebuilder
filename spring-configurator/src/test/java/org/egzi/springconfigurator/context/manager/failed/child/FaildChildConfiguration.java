package org.egzi.springconfigurator.context.manager.failed.child;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FaildChildConfiguration {
    @Bean
    public Object someBean() {
        return new Object();
    }
}
