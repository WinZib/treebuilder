package org.egzi.springconfigurator.context2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParentConfiguration {
    @Bean
    public String a() {
        return "a";
    }
}
