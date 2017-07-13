package org.egzi.springconfigurator;

import com.google.common.base.Throwables;
import org.egzi.treebuilder.TreeNode;
import org.egzi.treebuilder.visitors.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;
import java.util.Map;

/**
 * Created by egorz on 5/1/2017.
 */
public class ContextVisitor implements Visitor<Class, ApplicationContext> {
    private static final Logger log = LoggerFactory.getLogger(ContextVisitor.class);

    ContextManager contextManager;

    public ContextVisitor(ContextManager manager) {
        this.contextManager = manager;
    }


    @Override
    public <R> R doVisit(TreeNode<Class, ApplicationContext> node) {
        ContextTreeNode contextTreeNode = (ContextTreeNode) node;

        try {

            ApplicationContext context = init(contextTreeNode);

            contextTreeNode.setStartDate(new Date());
            contextTreeNode.set(context);

            Map<String, ContextListener> internalListeners = context.getBeansOfType(ContextListener.class);
            log.debug("found listeners to append: {}", internalListeners.keySet());

        } catch (Throwable t) {
            contextTreeNode.setError(t);
            Throwables.propagate(t);
        } finally {
            contextTreeNode.setFinishDate(new Date());
        }
        return null;
    }

    private ApplicationContext init(ContextTreeNode contextTreeNode) throws Exception {
        final Class configuration = contextTreeNode.getId();

        final AnnotationConfigApplicationContext context = contextTreeNode.getContextClass().newInstance();

        log.info("start context {}", configuration.getSimpleName());

        context.setAllowBeanDefinitionOverriding(true);
        context.setAllowCircularReferences(true);
        context.register(configuration);
        log.trace("bean factory {}", context.getBeanFactory());
        if (contextTreeNode.getParentId() != null)
            context.setParent(contextTreeNode.getParent().get());
        context.registerShutdownHook();
        context.refresh();

        return context;
    }
}
