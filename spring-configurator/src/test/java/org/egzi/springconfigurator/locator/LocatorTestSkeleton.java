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


import org.egzi.treebuilder.TreeNode;
import org.egzi.treebuilder.TreeNodeLocator;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertFalse;

public abstract class LocatorTestSkeleton<T extends TreeNodeLocator<Class, ApplicationContext>> {

    @Test(expected = RuntimeException.class)
    public void testThatLocatorThrowExceptionIfWeHaveEmptyConfigurationInClasspath() throws Exception {
        T locator = getLocator(getFileName("emptyContext"));
        TreeNode<Class, ApplicationContext> nextTreeNode = locator.getNextTreeNode();
    }

    @Test(expected = NoSuchElementException.class)
    public void testThatLocatorThrowExceptionIfWeHaveNoConfigurationFilesInClasspath() throws Exception {
        T locator = getLocator(getFileName("absentFile"));
        assertFalse(locator.hasMore());
        TreeNode<Class, ApplicationContext> nextTreeNode = locator.getNextTreeNode();
    }


    protected String getFileName(String fileNameWithoutExstension) {
        return "META-INF/locator/" + getExtension() + "/" + fileNameWithoutExstension + "." + getExtension();
    }

    protected abstract T getLocator(String fileName);

    protected abstract String getExtension();
}
