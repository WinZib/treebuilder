package org.egzi.springconfigurator.context.manager.parent;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParentContext {

    @Bean
    @Qualifier(value = "someBean")
    public String someBean(){
        return "someBean";
    }
}
