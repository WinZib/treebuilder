package org.egzi.springconfigurator.locator;


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
