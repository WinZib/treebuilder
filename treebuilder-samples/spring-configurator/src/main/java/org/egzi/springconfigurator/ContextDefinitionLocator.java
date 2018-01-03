package org.egzi.springconfigurator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterators;
import org.egzi.treebuilder.TreeNode;
import org.egzi.treebuilder.TreeNodeLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Created by egorz on 5/1/2017.
 */
public class ContextDefinitionLocator implements TreeNodeLocator<Class, ApplicationContext> {
    private static final Logger log = LoggerFactory.getLogger(ContextDefinitionLocator.class);

    private static final String RESOURCE_NAME = "META-INF/spring-definition.json";

    Iterator<ConfigDefinition> contextIterator;
    Enumeration<URL> resourceEnumeration;

    public ContextDefinitionLocator() {
        try {
            resourceEnumeration = getClass().getClassLoader().getResources(RESOURCE_NAME);
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }

    @Override
    public TreeNode<Class, ApplicationContext> getNextTreeNode() {

        if (!(contextIterator != null && contextIterator.hasNext())) {
            parseResource();
        }
        return convert(contextIterator.next());
    }

    private ContextTreeNode convert(ConfigDefinition configDefinition) {
        return new ContextTreeNode(configDefinition.getConfigurationClass(), configDefinition.getParentConfigurationClass(), configDefinition.getApplicationContextClass(), configDefinition.isLazyInit());
    }

    private void parseResource() {
        URL resource = resourceEnumeration.nextElement();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            contextIterator = Iterators.forArray(objectMapper.readValue(new File(resource.toURI()), ConfigDefinition[].class));
        } catch (Exception e) {
            throw new RuntimeException("fail to parse resource " + resource, e);
        }
    }

    @Override
    public boolean hasMore() {
        return (contextIterator != null && contextIterator.hasNext()) || resourceEnumeration.hasMoreElements();
    }
}
