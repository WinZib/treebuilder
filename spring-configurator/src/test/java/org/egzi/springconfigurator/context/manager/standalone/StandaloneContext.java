package org.egzi.springconfigurator.context.manager.standalone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StandaloneContext {

    @Bean
    public Object  aloneBean() {
        return new Object();
    }
}
