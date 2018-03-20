package org.egzi.springconfigurator.context.locator.context2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParentConfiguration {
    @Bean
    @Qualifier(value = "a")
    public String a() {
        return "a";
    }
}
