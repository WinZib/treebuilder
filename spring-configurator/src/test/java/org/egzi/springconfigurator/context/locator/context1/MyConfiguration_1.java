package org.egzi.springconfigurator.context.locator.context1;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfiguration_1 {
    @Bean
    public String b(@Qualifier(value = "a") String a) {
        return a + "b";
    }
}
