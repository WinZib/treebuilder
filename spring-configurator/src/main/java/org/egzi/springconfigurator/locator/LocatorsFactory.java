package org.egzi.springconfigurator.locator;

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
