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


import org.egzi.springconfigurator.context.locator.context1.MyConfiguration_1;
import org.egzi.springconfigurator.context.locator.context2.ParentConfiguration;
import org.egzi.springconfigurator.util.TestUtils;
import org.egzi.springconfigurator.visitor.ContextTreeNode;
import org.egzi.treebuilder.TreeNode;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.egzi.springconfigurator.locator.PropertiesContextDefinitionLocator.RELATIVE_ADDITIONAL_JAR_PATH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class YamContextDefinitionLocatorTest extends LocatorTestSkeleton<YamlContextDefinitionLocator> {
    @Override
    protected YamlContextDefinitionLocator getLocator(String fileName) {
        return new YamlContextDefinitionLocator(fileName);
    }

    @Override
    protected String getExtension() {
        return "yaml";
    }

    @Test
    public void testThatLocatorLoadCorrectContextDefinitionFromClasspath() throws Exception {
        final YamlContextDefinitionLocator locator = getLocator(getFileName("context"));
        assertTrue(locator.hasMore());
        TreeNode<Class, ApplicationContext> loaded = locator.getNextTreeNode();
        final ContextTreeNode expectedParentContext = ContextTreeNode.builder()
                .applicationConfigClass(ParentConfiguration.class)
                .parentApplicationConfigClass(null)
                .lazyInit(false).build();
        assertEquals(expectedParentContext, loaded);

        loaded = locator.getNextTreeNode();
        final ContextTreeNode expectedChildContext = ContextTreeNode.builder()
                .applicationConfigClass(MyConfiguration_1.class)
                .parentApplicationConfigClass(org.egzi.springconfigurator.context.locator.context2.ParentConfiguration.class)
                .lazyInit(false).build();

        assertEquals(expectedChildContext, loaded);
    }

    @Test
    public void testThatLocatorCanLoadContextDefinitionsFromMultipleJar() throws Exception {
        final String pathToJar = RELATIVE_ADDITIONAL_JAR_PATH + "testdata_yaml.jar";
        URL resource = this.getClass().getResource(pathToJar);
        Objects.requireNonNull(resource, pathToJar);
        TestUtils.addJarToCurrentClasspath(resource);
        final YamlContextDefinitionLocator locator = getLocator(getFileName("context"));
        List<TreeNode<Class, ApplicationContext>> nodes = new ArrayList<>();
        while (locator.hasMore()) {
            nodes.add(locator.getNextTreeNode());
        }
        assertEquals(3, nodes.size());
        TestUtils.removeJarFromClassPath(resource);


    }
}
