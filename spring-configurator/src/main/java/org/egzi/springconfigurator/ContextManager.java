package org.egzi.springconfigurator;

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


import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.egzi.springconfigurator.exceptions.InvalidContextStatusException;
import org.egzi.springconfigurator.info.ContextStatus;
import org.egzi.springconfigurator.info.ContextTreeInfo;
import org.egzi.springconfigurator.locator.ConfigDefinition;
import org.egzi.springconfigurator.locator.ContextTreeNodeConverter;
import org.egzi.springconfigurator.visitor.ContextTreeNode;
import org.egzi.springconfigurator.visitor.ContextVisitor;
import org.egzi.treebuilder.Forest;
import org.egzi.treebuilder.Tree;
import org.egzi.treebuilder.TreeBuilderFactory;
import org.egzi.treebuilder.TreeNode;
import org.egzi.treebuilder.visitors.AsyncWalker;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.requireNonNull;
import static org.egzi.springconfigurator.locator.LocatorsFactory.LOCATORS_FACTORY;

/**
 * Represents entry point for spring contexts
 */
@Slf4j
public class ContextManager {

    private static final String CONTEXT_PARALLELISM = "spring.configurator.parallelism";

    private static volatile ContextManager instance;

    private static boolean created = false;

    private static AtomicBoolean started = new AtomicBoolean(false);

    private static volatile ContextsConfiguration contextsConfiguration
            = ContextsConfiguration.configuration();

    private ContextVisitor contextVisitor = new ContextVisitor();

    private Map<Class<?>, ContextTreeNode> processedExternalNode = new ConcurrentHashMap<>();

    @Getter
    @Accessors(fluent = true)
    private final Forest<Class, ApplicationContext> forest;

    private final Object mutex = new Object();

    private ContextManager() {
        log.debug("Building context tree.....");
        forest = TreeBuilderFactory.<Class, ApplicationContext>newBuilder()
                .useTreeNodeLocators(LOCATORS_FACTORY.createLocators(contextsConfiguration))
                .buildTrees();
        log.info("Mediation context tree was build");
    }

    /**
     * Apply configuration to context manager. Should be called before method getInstance() was called.
     * In other cases throws exception
     *
     * @param contextsConfiguration - configuration for context manager.
     * @throws IllegalStateException -  if method getInstance() have been already called
     */
    public static void configure(ContextsConfiguration contextsConfiguration) {
        if (created) {
            throw new IllegalStateException("Context tree is already created");
        }
        synchronized (ContextManager.class) {
            if (created) {
                throw new IllegalStateException("Context tree is already created");
            }
            ContextManager.contextsConfiguration = contextsConfiguration;
        }
    }

    /**
     * Return the instance of context manager.
     *
     * @return instance of singleton
     */
    public static ContextManager getInstance() {
        if (instance != null)
            return instance;
        synchronized (ContextManager.class) {
            if (instance != null)
                return instance;
            ContextManager candidate = new ContextManager();
            created = true;
            instance = candidate;
            return candidate;
        }
    }


    /**
     * Start contexts tree
     *
     * @return {@link Map} of context tree root and  future task on context start result.
     * @throws IllegalStateException if tree have been already started
     */
    public Map<Class, ForkJoinTask<Void>> start() {
        if (!started.compareAndSet(false, true)) {
            throw new IllegalStateException("Context is already stared");
        }
        AsyncWalker<Class, ApplicationContext> asyncWalker = new AsyncWalker<>();
        asyncWalker.setVisitor(contextVisitor);
        synchronized (mutex) {
            return asyncWalker.walk(forest);
        }
    }


    /**
     * Start contexts tree and wait specified timeout for evert context tree in forest
     *
     * @param timeout - waiting timeout
     * @param unit    - dimension {@link TimeUnit}
     * @throws IllegalStateException if tree have been already started
     */
    @SneakyThrows
    public void start(long timeout, TimeUnit unit) {
        for (ForkJoinTask<Void> voidForkJoinTask : start().values()) {
            voidForkJoinTask.get(timeout, unit);
        }
    }


    /**
     * Destroy application context for specified class configuration
     *
     * @param klass - class configuration
     */
    public void shutdownContext(Class<?> klass) {
        final ContextTreeNode contextTreeNode = (ContextTreeNode) getContextTreeNodeFromForest(klass);
        if (contextTreeNode.status() != ContextStatus.STARTED) {
            log.debug("Configuration for {}  was't started, current status is {} ", klass, contextTreeNode.status());
            return;
        }
        closeContext(contextTreeNode);
        contextTreeNode
                .getChildren()
                .stream()
                .map(node -> ((ContextTreeNode) node))
                .filter(node -> node.status() == ContextStatus.STARTED)
                .forEach(node -> shutdownContext(node.getId()));

    }

    private void closeContext(ContextTreeNode node) {
        try {
            final ApplicationContext context = node.get();
            if (!(context instanceof DisposableBean)) {
                throw new IllegalArgumentException("Context for class: " + node.getId() + "should be Disposable Bean");
            }
            final DisposableBean disposableBean = (DisposableBean) context;
            disposableBean.destroy();
            node.onStop();
        } catch (Exception e) {
            log.error("Can't close context for class {}", node.getId(), e);
        }
    }


    /**
     * Destroy all contexts in forest.
     */
    public void shutDownTree() {
        forest
                .getTrees()
                .stream()
                .map(Tree::getRoot)
                .map(TreeNode::getId)
                .forEach(this::shutdownContext);
    }


    /**
     * Check that context is started for specific config
     *
     * @param config -  spring configuration class
     * @return true if started, false - in other cases
     * @throws IllegalArgumentException if there are no required context in tree
     */
    public boolean isStarted(Class<?> config) {
        return getContextTreeNodeFromForest(config).isVisited();
    }

    /**
     * Wait for some timeout for context starting.
     *
     * @param config - spring configuration class
     * @param time   - waiting timeout
     * @param unit   - dimension {@link TimeUnit}
     * @return instance itself due to fluid API processing
     * @throws InterruptedException     if waiting thread was interrupted
     * @throws IllegalArgumentException if there are no required context in tree
     */
    public ContextManager awaitStarting(Class<?> config, long time, TimeUnit unit) throws InterruptedException {
        final TreeNode<Class, ApplicationContext> contextNode = getContextTreeNodeFromForest(config);
        await(contextNode, time, unit);
        return this;
    }


    /**
     * Get application context for configuration class. If configuration class is absent or context was't started exception
     * would thrown
     *
     * @param config - config for application class
     * @return {@link ApplicationContext} for specific class
     * @throws IllegalArgumentException      if there are no context node in tree for specified config
     * @throws InvalidContextStatusException if context was't started
     */
    public ApplicationContext provideApplicationContext(Class<?> config) {
        final ContextTreeNode node = (ContextTreeNode) getContextTreeNodeFromForest(config);
        final ContextStatus contextStatus = node.contextStatus();
        if (contextStatus != ContextStatus.STARTED) {
            throw new InvalidContextStatusException("Context " + config + "was't started. Current state is: " + contextStatus);
        }
        return node.get();
    }

    /**
     * Get application context for configuration class. If context was't started wait specified timeout.
     * If configuration class is absent or context was't started during timeout exception would thrown
     *
     * @param config - config for application class
     * @param time   - waiting timeout
     * @param unit   - dimension {@link TimeUnit}
     * @return {@link ApplicationContext} for specific class
     * @throws InterruptedException                  if thread was interrupted during context up awaiting
     * @throws IllegalArgumentException              if there are no context node in tree for specified config
     * @throws TimeoutException if context was't started during specified timeout
     */
    public ApplicationContext provideApplicationContextOrWait(Class<?> config, long time, TimeUnit unit) throws Exception {
        awaitStarting(config, time, unit);
        return getContextTreeNodeFromForest(config).get();
    }

    private void await(TreeNode<Class, ApplicationContext> contextNode, long time, TimeUnit unit) {
        if (!contextNode.isVisited()) {
            contextNode.awaitVisited(time, unit);
        }
    }

    /**
     * Add external context to tree, can be added after/before context start.
     * If context is't started added context to tree
     * If context has been already started, it waits parent context staring for specified timeout after that it start.
     *
     * @param context - definition  {@link ConfigDefinition} of context
     * @param timeout  parent context starting
     * @param timeUnit - {@link TimeUnit} for timeout
     * @return instance itself due to fluid API processing
     * @throws IllegalArgumentException      if there are no context node in tree for specified config
     * @throws TimeoutException if parent context was't started for specified timeout
     * @throws InterruptedException if current thread was interrupted

     */
    public ContextManager add(ConfigDefinition context, long timeout, TimeUnit timeUnit) {
        synchronized (mutex) {
            if (started.get()) {
                addNodeToStartingTree(context, timeout, timeUnit);
            } else {
                addNodeToInitialTree(context);
            }
        }
        return this;
    }

    /**
     * Add external context to tree, can be added after/before context start.
     * If context is't started added context to tree
     * If context has been already started, it endlessly waiting parent context staring after that it start
     * Method can hang because of infinite waiting.
     *
     * @param context - definition  {@link ConfigDefinition} of context
     * @return instance itself due to fluid API processing
     * @throws IllegalArgumentException      if there are no context node in tree for specified config

     */
    public ContextManager add(ConfigDefinition context) {
        return add(context, 0, TimeUnit.MINUTES);
    }


    /**
     * Return snapshot of context at the current moment.
     *
     * @return ContextTreeInfo - current state of context tree
     */
    public ContextTreeInfo contextTreeInfo() {
        return new ContextTreeInfo(forest);
    }


    /**
     * Add {@link ContextListener} which was fired on context events like start/refresh/error
     *
     * @param configuration - context class configuration
     * @param listener      - context listener
     * @throws IllegalArgumentException if there are no context node in tree for specified configuration
     */
    public void addListener(Class<?> configuration, ContextListener listener) {
        if (isContextPresentInCurrentTree(configuration)) {
            final ContextTreeNode node = ((ContextTreeNode) getContextTreeNodeFromForest(configuration));
            node.addListener(listener);
        } else {
            if (processedExternalNode.containsKey(configuration)) {
                processedExternalNode.get(configuration).addListener(listener);
            } else {
                throw new IllegalArgumentException("Context" + configuration +
                        " is not available in current context tree");
            }
        }
    }


    private void addNodeToInitialTree(ConfigDefinition context) {
        ContextTreeNodeConverter converter = new ContextTreeNodeConverter(contextsConfiguration);
        final TreeNode<Class, ApplicationContext> node = converter.convert(context);
        final Class parentClass = node.getParentId();
        if (parentClass != null && !isContextPresentInCurrentTree(parentClass)) {
            throw new IllegalArgumentException("There are no parent context for " + parentClass +
                    " in current context tree");
        }
        forest.addTreeNode(node);
    }


    private void addNodeToStartingTree(ConfigDefinition context, long timeoout, TimeUnit timeUnit) {
        final Class parentConfigurationClass = context.getParentConfigurationClass();
        ContextTreeNodeConverter converter = new ContextTreeNodeConverter(contextsConfiguration);
        final TreeNode<Class, ApplicationContext> childTreeNode = converter.convert(context);
        processedExternalNode.put(childTreeNode.getId(), (ContextTreeNode) childTreeNode);
        try {
            if (parentConfigurationClass != null) {
                TreeNode<Class, ApplicationContext> parentTreeNode
                        = validateAndGetParentContext(parentConfigurationClass, childTreeNode);
                await(parentTreeNode, timeoout, timeUnit);
                childTreeNode.setParent(parentTreeNode);
            }
            startContext(childTreeNode);
            forest.addTreeNode(childTreeNode);
        } finally {
            processedExternalNode.remove(childTreeNode.getId());
        }
    }

    private void startContext(TreeNode<Class, ApplicationContext> node) {
        try {
            contextVisitor.doVisit(node);
        } catch (Exception e) {
            log.error("Error was appear while context have been starting", e);
        }
    }

    @SneakyThrows
    private TreeNode<Class, ApplicationContext> validateAndGetParentContext(Class parentConfigurationClass, TreeNode<Class, ApplicationContext> childContextNode) {
        if (!isContextPresentInCurrentTree(parentConfigurationClass)) {
            throw new IllegalArgumentException("There are no parent context is available " + parentConfigurationClass +
                    " in current context tree");
        }
        return getContextTreeNodeFromForest(parentConfigurationClass);

    }

    private boolean isContextPresentInCurrentTree(Class<?> nodeId) {
        return forest.nodeStream()
                .map(TreeNode::getId).anyMatch(nodeId::equals);
    }

    private TreeNode<Class, ApplicationContext> getContextTreeNodeFromForest(Class<?> configurationClass) {
        requireNonNull(configurationClass, "Configuration class can't be null");
        return forest.nodeStream()
                .filter(node -> node.getId().equals(configurationClass)).findAny().orElseThrow(() ->
                        new IllegalArgumentException("Context" + configurationClass +
                                " is not available in current context tree"));
    }


}
