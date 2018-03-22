package org.egzi.springconfigurator.info;

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


import lombok.ToString;
import org.egzi.treebuilder.Forest;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.Map;

import static java.util.stream.Collectors.toMap;


/**
 * Snapshot of context tree statuses in some moment of time.
 */
@ToString
public class ContextTreeInfo {

    private Map<Class, ContextInfo> contextsInfo;

    public ContextTreeInfo(Forest<Class, ApplicationContext> forest) {
        contextsInfo = forest
                .nodeStream()
                .map(ContextInfo::new)
                .collect(toMap(ContextInfo::applicationConfigClass, o -> o));
    }

    /**
     * Return context status for specific configuration
     * @param config - context configuration
     * @return {@link ContextStatus}
     * @throws IllegalArgumentException if there is no available context for specified configuration
     */
    public ContextStatus contextStatus(Class config) {
        return contextInfo(config).status();
    }

    /**
     * Return {@link Collection} of available context info
     * @return - context info collection
     */
    public Collection<ContextInfo> contextInfoNodes() {
        return contextsInfo.values();
    }

    /**
     * Return context info of spring configuration
     * @param config - context configuration
     * @return {@link ContextInfo}
     * @throws IllegalArgumentException if there is no available context for specified configuration
     */
    public ContextInfo contextInfo(Class config) {
        if (!contextsInfo.containsKey(config)) {
            throw new IllegalArgumentException("There are no available context for " + config);
        }
        return contextsInfo.get(config);
    }


}
