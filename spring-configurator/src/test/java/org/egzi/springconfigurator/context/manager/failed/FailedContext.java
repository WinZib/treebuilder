package org.egzi.springconfigurator.context.manager.failed;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FailedContext {

    @Bean
    public String failedBean(@Qualifier("oh it's absent") String absentBean) {
        return " " + absentBean;
    }
}
