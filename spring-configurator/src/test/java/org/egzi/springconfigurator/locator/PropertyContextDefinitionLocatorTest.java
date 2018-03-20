package org.egzi.springconfigurator.locator;

import org.egzi.springconfigurator.context.locator.context1.MyConfiguration_1;
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

public class PropertyContextDefinitionLocatorTest extends LocatorTestSkeleton<PropertiesContextDefinitionLocator> {


    @Test
    public void testThatLocatorCanLoadContextDefinitionsFromMultipleJar() throws Exception {
        final String pathToJar = RELATIVE_ADDITIONAL_JAR_PATH + "testdata_properties.jar";

        URL resource = this.getClass().getResource(pathToJar);
        Objects.requireNonNull(resource, pathToJar);
        TestUtils.addJarToCurrentClasspath(resource);
        PropertiesContextDefinitionLocator locator =
                new PropertiesContextDefinitionLocator("META-INF/locator/properties/context.properties");
        List<TreeNode<Class, ApplicationContext>> nodes = new ArrayList<>();
        while (locator.hasMore()) {
            nodes.add(locator.getNextTreeNode());
        }
        assertEquals(2, nodes.size());
        TestUtils.removeJarFromClassPath(resource);

    }


    @Test
    public void testThatLocatorLoadCorrectContextDefinitionFromClasspath() throws Exception {
        final PropertiesContextDefinitionLocator locator = getLocator(getFileName("context"));
        assertTrue(locator.hasMore());
        TreeNode<Class, ApplicationContext> loaded = locator.getNextTreeNode();
        final ContextTreeNode expected = ContextTreeNode.builder()
                .applicationConfigClass(MyConfiguration_1.class)
                .parentApplicationConfigClass(null)
                .lazyInit(false).build();
        assertEquals(expected, loaded);
    }

    @Override
    protected PropertiesContextDefinitionLocator getLocator(String fileName) {
        return new PropertiesContextDefinitionLocator(fileName);
    }

    @Override
    protected String getExtension() {
        return "properties";
    }

}
