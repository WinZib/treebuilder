package org.egzi.springconfigurator.locator;

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
