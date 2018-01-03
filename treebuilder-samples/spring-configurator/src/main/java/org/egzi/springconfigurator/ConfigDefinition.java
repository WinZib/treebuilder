package org.egzi.springconfigurator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Getter
public class ConfigDefinition {
    @JsonProperty("config-class")
    private Class configurationClass;
    @JsonProperty("parent-config-class")
    private Class parentConfigurationClass;
    @JsonProperty("lazy-init")
    private boolean lazyInit = false;
    @JsonProperty("application-context-class")
    private Class<? extends AnnotationConfigApplicationContext> applicationContextClass = AnnotationConfigApplicationContext.class;

    public ConfigDefinition() {
    }
}
