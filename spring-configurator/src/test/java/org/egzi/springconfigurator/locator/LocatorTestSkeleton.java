package org.egzi.springconfigurator.locator;

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
