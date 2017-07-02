package org.egzi.springconfigurator;

import org.egzi.treebuilder.Forest;
import org.egzi.treebuilder.TreeBuilderFactory;
import org.egzi.treebuilder.visitors.AsyncWalker;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ForkJoinPool;

public class ContextManager {
    private static final String CONTEXT_PARALLELISM = "spring.configurator.parallelism";
    private static volatile ContextManager instance;

    private Forest<Class, ApplicationContext> forest;

    private ContextManager() {
        forest = TreeBuilderFactory.<Class, ApplicationContext>newBuilder().useTreeNodeLocator(new ContextDefinitionLocator()).buildTrees();
    }


    public static ContextManager getInstance() {
        if (instance != null)
            return instance;
        synchronized (ContextManager.class) {
            if (instance != null)
                return instance;

            ContextManager candidate = new ContextManager();
            candidate.init();

            instance = candidate;
        }

        return instance;
    }

    private void init() {
        ForkJoinPool fjp = new ForkJoinPool(Integer.valueOf(System.getProperty(CONTEXT_PARALLELISM, "5")));
        AsyncWalker<Class, ApplicationContext> asyncWalker = new AsyncWalker<>();
        asyncWalker.setForkJoinPool(fjp);
        asyncWalker.setVisitor(new ContextVisitor(this));
    }

    public Forest<Class, ApplicationContext> getForest() {
        return forest;
    }
}
