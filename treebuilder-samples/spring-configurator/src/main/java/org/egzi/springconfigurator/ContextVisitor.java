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
        ContextTreeNode contextInfo = (ContextTreeNode) node;

        try {

            final Class configuration = contextInfo.getId();

            final AnnotationConfigApplicationContext context = contextInfo.getContextClass().newInstance();

            contextInfo.setStartDate(new Date());

            //final ClassLoader loader = Mode.getMode() == Mode.RTF ? ClassUtils
            //        .overrideThreadContextClassLoader(ContextFactory.class.getClassLoader()) : null;
            log.info("start context {}", configuration.getSimpleName());
            //  context.getEnvironment().setActiveProfiles(Mode.getProfileNames());
            //  log.info("activate profiles: {}", Arrays.toString(context.getEnvironment().getActiveProfiles()));
            context.setAllowBeanDefinitionOverriding(true);
            context.setAllowCircularReferences(true);
            context.register(configuration);
            log.trace("bean factory {}", context.getBeanFactory());
            if (node.getParentId() != null)
                context.setParent(node.getParent().get());
            context.registerShutdownHook();
            context.refresh();


            //internal bean that implements MediationInternalListener will be executed at the end automatically
            Map<String, ContextListener> internalListeners = context.getBeansOfType(ContextListener.class);
            log.debug("found listeners to append: {}", internalListeners.keySet());

            log.info("finish context {} : {}", configuration.getSimpleName(), context);
        } catch (Throwable t) {
            contextInfo.setError(t);
            Throwables.propagate(t);
        } finally {
            contextInfo.setFinishDate(new Date());
            //ClassUtils.overrideThreadContextClassLoader(loader);
        }

        return null;
    }
}
