package org.egzi.springconfigurator.context.manager.children.superchild;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SuperChild1 {

    @Bean
    public String superChild1(@Qualifier("child1") String value ) {
        return "SuperChildContext1" + value;
    }
}
