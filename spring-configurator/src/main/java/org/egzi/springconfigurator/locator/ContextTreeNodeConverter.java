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



import org.egzi.springconfigurator.ContextsConfiguration;
import org.egzi.springconfigurator.visitor.ContextTreeNode;
import org.egzi.treebuilder.TreeNode;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * Convert that converts to ContextTreeNode
 */
public class ContextTreeNodeConverter implements ContextConverter<TreeNode<Class, ApplicationContext>> {

    private final List<String> profiles;

    public ContextTreeNodeConverter(ContextsConfiguration contextsConfiguration) {
        this.profiles = contextsConfiguration.contextProfiles();
    }

    @Override
    public TreeNode<Class, ApplicationContext> convert(ConfigDefinition configDefinition) {
        return ContextTreeNode.builder()
                .parentApplicationConfigClass(configDefinition.parentConfigurationClass)
                .applicationConfigClass(configDefinition.configurationClass)
                .contextClass(configDefinition.applicationContextClass)
                .lazyInit(configDefinition.lazyInit)
                .profiles(profiles)
                .build();
    }
}
