package org.egzi.springconfigurator.context.locator.context3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration_3 {

    @Bean
    public String abb() {
        return "abb";
    }
}
