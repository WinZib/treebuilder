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


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import org.egzi.springconfigurator.locator.ConfigDefinition.ConfigDefinitionYamlAggregator;

import java.net.URL;
import java.util.Iterator;

/**
 * Load data from yaml file in {@link ConfigDefinition}
 */
public class YamlContextDefinitionLocator extends ContextDefinitionLocatorSkeleton {


    public YamlContextDefinitionLocator(String contextsConfiguration) {
        super(contextsConfiguration);
    }

    public YamlContextDefinitionLocator(String contextsConfigurationFile, ContextTreeNodeConverter converter) {
        super(contextsConfigurationFile, converter);
    }

    @Override
    @SneakyThrows
    protected Iterator<ConfigDefinition> loadResources(URL resource) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        final ConfigDefinitionYamlAggregator aggregator
                = mapper.readValue(resource, ConfigDefinitionYamlAggregator.class);
        return  aggregator.contexts().iterator();
    }

}
