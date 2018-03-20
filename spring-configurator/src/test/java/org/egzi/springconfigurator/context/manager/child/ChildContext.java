package org.egzi.springconfigurator.context.manager.child;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChildContext {

    @Bean
    public String bean(@Qualifier("someBean") String value ) {
        return "ChildContext" + value;
    }

}
