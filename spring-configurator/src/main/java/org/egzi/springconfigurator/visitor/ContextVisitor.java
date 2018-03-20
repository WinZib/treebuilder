package org.egzi.springconfigurator.visitor;

import lombok.extern.slf4j.Slf4j;
import org.egzi.springconfigurator.exceptions.ParentContextFailedException;
import org.egzi.springconfigurator.info.ContextStatus;
import org.egzi.treebuilder.TreeNode;
import org.egzi.treebuilder.visitors.Visitor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Optional;

/**
 * Created by egorz on 5/1/2017.
 */
@Slf4j
public class ContextVisitor implements Visitor<Class, ApplicationContext> {


    @Override
    public <R> R doVisit(TreeNode<Class, ApplicationContext> node) {
        ContextTreeNode contextTreeNode = (ContextTreeNode) node;
        visit(contextTreeNode);
        return null;
    }

    private void visit(ContextTreeNode contextTreeNode) {
        try {
            contextTreeNode.onStarting();
            checkParentContextStatus(contextTreeNode);
            ApplicationContext context = init(contextTreeNode);
            contextTreeNode.set(context);
        } catch (Throwable error) {
            log.error("Error while starting the context: " + contextTreeNode.applicationConfigClass(), error);
            processError(contextTreeNode, error);
        } finally {
            contextTreeNode.markVisited();
            contextTreeNode.onFinish();
        }
    }

    private void checkParentContextStatus(TreeNode<Class, ApplicationContext> node) {
        final ContextTreeNode contextNode = (ContextTreeNode) node;
        //noinspection ResultOfMethodCallIgnored
        Optional.ofNullable(contextNode.getParent())
                .map(parentNode -> ((ContextTreeNode) parentNode).status())
                .filter(contextStatus -> contextStatus == ContextStatus.FAILED)
                .ifPresent(contextStatus -> {
                    throw new ParentContextFailedException("Parent context: " + node.getParentId()
                            + " for configuration " + node.getId() +
                            " is failed", ((ContextTreeNode) node.getParent()).error());
                });
    }


    private void processError(ContextTreeNode contextTreeNode, Throwable error) {
        try {
            contextTreeNode.onError(error);
        } catch (Exception e) {
            log.error("Error in processing error event of application  context start", e);
        }
    }


    private ApplicationContext init(ContextTreeNode contextTreeNode) throws Exception {
        final Class configuration = contextTreeNode.getId();

        final AnnotationConfigApplicationContext context = contextTreeNode.contextClass().newInstance();

        log.info("start context {}", configuration.getSimpleName());

        context.setAllowBeanDefinitionOverriding(true);
        context.setAllowCircularReferences(true);
        ConfigurableEnvironment environment = context.getEnvironment();
        environment.setActiveProfiles(contextTreeNode.profiles().toArray(new String[0]));
        context.register(configuration);
        log.trace("bean factory {}", context.getBeanFactory());
        if (contextTreeNode.getParentId() != null)
            context.setParent(contextTreeNode.getParent().get());
        context.registerShutdownHook();
        context.refresh();
        contextTreeNode.onRefresh();
        return context;
    }
}
