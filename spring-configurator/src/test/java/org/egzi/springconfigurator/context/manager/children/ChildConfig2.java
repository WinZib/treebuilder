package org.egzi.springconfigurator.context.manager.children;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChildConfig2 {

    @Bean
    @Qualifier("child2")
    public String bean1(@Qualifier("someBean") String value) {
        return "ChildContext2" + value;
    }

}
