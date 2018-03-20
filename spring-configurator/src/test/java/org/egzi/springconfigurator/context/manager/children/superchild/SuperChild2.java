package org.egzi.springconfigurator.context.manager.children.superchild;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

public class SuperChild2 {

    @Bean
    public String superChild2(@Qualifier("child2") String value ) {
        return "SuperChildContext2" + value;
    }
}
