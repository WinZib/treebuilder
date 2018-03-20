package org.egzi.springconfigurator.locator;

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
