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


import com.google.common.io.Files;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.egzi.springconfigurator.ContextsConfiguration;
import org.egzi.treebuilder.TreeNodeLocator;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.egzi.springconfigurator.ContextsConfiguration.LocatorConfig;

/**
 * Create locators {@link TreeNodeLocator} for specific file extension
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocatorsFactory {

    public static final LocatorsFactory LOCATORS_FACTORY = new LocatorsFactory();

    public List<TreeNodeLocator<Class, ApplicationContext>> createLocators(ContextsConfiguration configuration) {
        Set<ContextsConfiguration.LocatorConfig> locatorConfigs = configuration.locatorsConfigs();
        return locatorConfigs.stream().map(config -> createLocator(config, configuration)).collect(toList());
    }


    /**
     * Create locator according to locator type
     *
     * @param config - locator configuration
     * @return locator for configuration files
     */
    public TreeNodeLocator<Class, ApplicationContext> createLocator(LocatorConfig config) {
        return createLocator(config, ContextsConfiguration.configuration());
    }


    /**
     * Create locator according to locator type
     *
     * @param config - locator configuration
     * @param contextConfiguration - configuration for {@link ContextTreeNodeConverter}
     * @return locator for configuration files
     */
    public TreeNodeLocator<Class, ApplicationContext> createLocator(LocatorConfig config,
                                                                    ContextsConfiguration contextConfiguration) {

        final ContextTreeNodeConverter converter = new ContextTreeNodeConverter(contextConfiguration);
        switch (config.type()) {
            case PROPERTIES:
                return new PropertiesContextDefinitionLocator(config.fileName(),
                        converter);
            case YAML:
                return new YamlContextDefinitionLocator(config.fileName(), converter);
            default:
                throw new IllegalArgumentException("Unsupported user type");
        }
    }


    public enum LocatorType {
        PROPERTIES("properties"), YAML("yaml");

        private final String extension;

        LocatorType(String extension) {
            this.extension = extension;
        }

        public static LocatorType getLocatorType(String fileName) {
            String extension = Files.getFileExtension(fileName);
            switch (extension) {
                case "properties":
                    return PROPERTIES;
                case "yaml":
                    return YAML;
                default:
                    throw new IllegalArgumentException("Unsupported configuration file extension: " + extension);
            }
        }
    }

}
