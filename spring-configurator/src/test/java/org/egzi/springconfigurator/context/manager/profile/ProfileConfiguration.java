package org.egzi.springconfigurator.context.manager.profile;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ProfileConfiguration {

    @Bean
    @Profile("correct_profile")
    public Object correctBean() {
        return "correct_profile_bean";
    }


    @Bean
    @Profile("incorrect_profile_bean")
    public Object incorrectBean() {
        return "incorrectProfileBean";
    }

}
