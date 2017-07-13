package org.egzi.springconfigurator.context1;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration {
    @Bean
    public String b(@Qualifier(value = "a") String a) {
        return a + "b";
    }
}
