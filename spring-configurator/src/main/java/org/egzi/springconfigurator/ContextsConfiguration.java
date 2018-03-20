package org.egzi.springconfigurator;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.egzi.springconfigurator.locator.LocatorsFactory.LocatorType;

import java.util.*;

@Getter()
@Accessors(fluent = true)
public class ContextsConfiguration {


    private static final ContextsConfiguration DEFAULT_CONFIGURATION = new ContextsConfiguration();

    private final List<String> contextProfiles = new ArrayList<>();

    private final Set<LocatorConfig> locatorsConfigs = new HashSet<>();

    private ContextsConfiguration() {
        locatorsConfigs.add(new LocatorConfig("META-INF/context.properties"));
    }

    public ContextsConfiguration addProfie(String profile) {
        contextProfiles.add(profile);
        return this;
    }

    public ContextsConfiguration addProfiels(Collection<String> profiles) {
        contextProfiles.addAll(profiles);
        return this;
    }

    public ContextsConfiguration enableContextFileLookup(String fileName) {
        locatorsConfigs.add(new LocatorConfig(fileName));
        return this;
    }

    public ContextsConfiguration disableContextFileLookup(String fileName) {
        locatorsConfigs.remove(new LocatorConfig(fileName));
        return this;
    }

    public static ContextsConfiguration configuration() {
         return new ContextsConfiguration();
    }


    @Data
    @Accessors(fluent = true)
    public static class LocatorConfig {
        private final String fileName;
        private final LocatorType type;

        LocatorConfig(String fileName) {
            this.fileName = fileName;
            this.type = LocatorType.getLocatorType(fileName);
        }
    }
}
