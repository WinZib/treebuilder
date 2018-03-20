package org.egzi.springconfigurator.context.manager.children;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChildConfig3 {
    @Bean
    @Qualifier("child3")
    public String bean1(@Qualifier("someBean") String value ) {
        return "ChildContext3" + value;
    }
}
