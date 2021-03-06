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


import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.egzi.springconfigurator.ContextsConfiguration;
import org.egzi.treebuilder.TreeNode;
import org.egzi.treebuilder.TreeNodeLocator;
import org.springframework.context.ApplicationContext;

import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;


/**
 * Class represents basic locator logic for context configurations.
 * Not Thread Safe.
 *
 * @author egzi, mash0916
 */
@Slf4j
public abstract class ContextDefinitionLocatorSkeleton implements TreeNodeLocator<Class, ApplicationContext> {

    @Getter
    @Accessors(fluent = true)
    private final String contextsConfigurationFile;

    private final ContextTreeNodeConverter converter;

    private Supplier<Enumeration<URL>> resourceEnumeration = () -> {
        Enumeration<URL> enumeration = loadFileEnumeration();
        resourceEnumeration = () -> enumeration;
        return enumeration;
    };

    private Iterator<ConfigDefinition> contextIterator;

    ContextDefinitionLocatorSkeleton(String contextsConfigurationFile) {
        this.contextsConfigurationFile = contextsConfigurationFile;
        this.converter = new ContextTreeNodeConverter(ContextsConfiguration.configuration());
    }


    ContextDefinitionLocatorSkeleton(String contextsConfigurationFile, ContextTreeNodeConverter converter) {
        this.contextsConfigurationFile = contextsConfigurationFile;
        this.converter = converter;
    }



    @Override
    public final TreeNode<Class, ApplicationContext> getNextTreeNode() {

        checkBoundaries();

        if (!(contextIterator != null && contextIterator.hasNext())) {
            parseResource();
        }
        return converter.convert(contextIterator.next());
    }

    @SneakyThrows
    private Enumeration<URL> loadFileEnumeration() {
        return  Thread.currentThread().getContextClassLoader().getResources(contextsConfigurationFile);
    }


    private void checkBoundaries() {
        if (!hasMore()) {
            throw new NoSuchElementException("There are no context configuration is available for files: " +
                    contextsConfigurationFile);
        }
    }

    private void parseResource() {
        URL resource = resourceEnumeration.get().nextElement();
        try {
            contextIterator = loadResources(resource);
        } catch (Exception e) {
            throw new RuntimeException("Fail to parse resource " + resource, e);
        }
    }

    protected abstract Iterator loadResources(URL resource);

    @Override
    public final boolean hasMore() {
        return (contextIterator != null && contextIterator.hasNext()) || resourceEnumeration.get().hasMoreElements();
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContextDefinitionLocatorSkeleton)) return false;

        ContextDefinitionLocatorSkeleton that = (ContextDefinitionLocatorSkeleton) o;

        return contextsConfigurationFile.equals(that.contextsConfigurationFile);
    }

    @Override
    public int hashCode() {
        return contextsConfigurationFile.hashCode();
    }
}
