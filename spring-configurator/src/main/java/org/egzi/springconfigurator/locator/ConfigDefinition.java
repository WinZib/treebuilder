package org.egzi.springconfigurator.locator;

/*
 * #%L
 * spring-configurator
 * %%
 * Copyright (C) 2018 WinZib (winzib@yandex.ru)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

/**
 * Pojo used as template for configuration lookup. It's just value object which is used to configure ContextTreeNode
 * fro further processing
 */
@Getter
public class ConfigDefinition {
    Class configurationClass;
    Class parentConfigurationClass;
    boolean lazyInit = false;
    Class<? extends AnnotationConfigApplicationContext> applicationContextClass;

    @JsonCreator
    public ConfigDefinition(@JsonProperty(value = "config-class", required = true) Class configurationClass,
                            @JsonProperty(value = "parent-config-class") Class parentConfigurationClass,
                            @JsonProperty("lazy-init") boolean lazyInit,
                            @JsonProperty(value = "application-context-class", defaultValue = "org.springframework.context.annotation.AnnotationConfigApplicationContext")
                                    Class<? extends AnnotationConfigApplicationContext> applicationContextClass) {
        this.configurationClass = configurationClass;
        this.parentConfigurationClass = parentConfigurationClass;
        this.lazyInit = lazyInit;
        this.applicationContextClass = applicationContextClass;
    }


    /**
     * Jackson can't load array of object explicitly and this class used for aggregation in
     * {@link YamlContextDefinitionLocator}
     */
    @Setter
    @Getter
    static class ConfigDefinitionYamlAggregator {
        @JsonProperty
        @Accessors(fluent = true)
        private List<ConfigDefinition> contexts;
    }

}
