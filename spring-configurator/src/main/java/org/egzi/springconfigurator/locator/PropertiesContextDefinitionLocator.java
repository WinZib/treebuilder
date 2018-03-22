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


import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.google.common.collect.Iterators;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.Iterator;

/**
 * Load data from properties file in {@link ConfigDefinition}
 */
public class PropertiesContextDefinitionLocator extends ContextDefinitionLocatorSkeleton {

    protected static final String RELATIVE_ADDITIONAL_JAR_PATH = "/org/egzi/springconfigurator/";

    public PropertiesContextDefinitionLocator(String contextsConfigurationFile) {
        super(contextsConfigurationFile);
    }


    public PropertiesContextDefinitionLocator(String contextsConfigurationFile, ContextTreeNodeConverter converter) {
        super(contextsConfigurationFile, converter);
    }

    @Override
    @SneakyThrows
    public Iterator<ConfigDefinition> loadResources(URL resource) {
        JavaPropsMapper propertiesMapper = new JavaPropsMapper();
        return Iterators.singletonIterator(propertiesMapper.readValue(resource, ConfigDefinition.class));
    }

}
