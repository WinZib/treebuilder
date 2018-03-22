package org.egzi.springconfigurator;

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
