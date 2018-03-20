package org.egzi.springconfigurator.context.manager.children.superchild;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SuperChild3{

        @Bean
        public String superChild2(@Qualifier("child3") String value ) {
            return "SuperChildContext3" + value;
        }
}
