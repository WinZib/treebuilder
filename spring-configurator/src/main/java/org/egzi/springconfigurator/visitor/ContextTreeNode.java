package org.egzi.springconfigurator.visitor;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.egzi.springconfigurator.ContextListener;
import org.egzi.springconfigurator.exceptions.InvalidContextStatusException;
import org.egzi.springconfigurator.info.ContextStatus;
import org.egzi.treebuilder.AbstractTreeNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.util.Objects.requireNonNull;
import static org.egzi.springconfigurator.info.ContextStatus.*;

/**
 * Represents a node for {@link org.egzi.treebuilder.Tree}, used as holder for data which is required for context
 * starting and useful information about context starting parameters.
 */
@Getter
@ToString
@Accessors(fluent = true)
@Slf4j
public class ContextTreeNode extends AbstractTreeNode<Class, ApplicationContext> {

    private volatile Date startDate;

    private volatile Date finishDate;

    private volatile Date stopDate;

    private volatile Throwable error;

    private volatile ContextStatus status = NOT_STARTED;

    private final boolean lazyInit;

    private final Class applicationConfigClass;

    private final Class parentApplicationConfigClass;

    private final Class<? extends AnnotationConfigApplicationContext> contextClass;

    private final List<String> profiles;

    private final Queue<ContextListener> contextListeners = new ConcurrentLinkedQueue<>();


    @Builder
    private ContextTreeNode(Class applicationConfigClass,
                            Class parentApplicationConfigClass,
                            Class<? extends AnnotationConfigApplicationContext> contextClass,
                            boolean lazyInit, List<String> profiles) {
        super(applicationConfigClass, parentApplicationConfigClass);
        requireNonNull(applicationConfigClass, "Config class is required for context start");
        this.applicationConfigClass = applicationConfigClass;
        this.parentApplicationConfigClass = parentApplicationConfigClass;
        this.contextClass = contextClass;
        this.lazyInit = lazyInit;
        this.profiles = profiles;
    }


    /**
     * Calculates context status at current moment of time
     *
     * @return {@link ContextStatus }
     */
    public ContextStatus contextStatus() {
        return status;
    }

    /**
     * Apply the listener to context tree node.
     *
     * @param listener - {@link ContextListener} listener to specific context events.
     */
    public void addListener(ContextListener listener) {
        if (finishDate != null) {
            final ContextStatus contextStatus = contextStatus();
            switch (contextStatus) {
                case STARTED:
                    listener.onRefresh();
                    break;
                case FAILED:
                    listener.onError();
                    break;
                default:
                    throw new InvalidContextStatusException("Context tree node is in invalid state: " + contextStatus);
            }
        } else {
            contextListeners.add(listener);
        }
    }

    /**
     * Set starting date and status. Invoke on start action on listeners
     *
     * @throws InvalidContextStatusException if context have been already started
     */
    public void onStarting() {
        if (status != NOT_STARTED)
            throw new InvalidContextStatusException("You can't start context tree node with status: " + status + "" +
                    " It should be in state NOT_STARTED");
        startDate = new Date();
        status = STARTING;
        contextListeners.forEach(ContextListener::onStart);
    }

    /**
     * Set status.Invoke on refresh action on listeners
     *
     * @throws InvalidContextStatusException if context have been already refreshed
     */
    public void onRefresh() {
        if (status != STARTING)
            throw new InvalidContextStatusException("You can't refresh context tree node with status: " + status +
                    " It should be in state STARTING");
        contextListeners.forEach(ContextListener::onRefresh);
        status = STARTED;
    }

    /**
     * Set failed status  invoke on error action on listeners
     *
     * @throws InvalidContextStatusException if context have been already failed or started
     */
    public void onError(Throwable error) {
        if (status != STARTING)
            throw new InvalidContextStatusException("You can't refresh context tree node with status: " + status +
                    " It should be in state STARTING");
        this.error = error;
        status = FAILED;
        contextListeners.forEach(ContextListener::onError);
    }

    /**
     * Set finish date
     *
     * @throws InvalidContextStatusException if context have been already finished or not started
     */
    public void onFinish() {
        if (status != FAILED && status != STARTED) {
            throw new InvalidContextStatusException("You can't finish context tree node with status: " + status +
                    " It should be in state FAILED or STARTED");

        }
        finishDate = new Date();
    }

    /**
     * Set stop date
     *
     * @throws InvalidContextStatusException if context have been already finished or not started
     */

    public void onStop() {
        stopDate = new Date();
        status = STOPPED;
    }


    public ContextTreeNode copy() {
        ContextTreeNode treeNode =
                new ContextTreeNode(applicationConfigClass,
                        parentApplicationConfigClass,
                        contextClass,
                        lazyInit,
                        profiles);
        treeNode.status = status;
        treeNode.startDate = startDate;
        treeNode.stopDate = stopDate;
        treeNode.finishDate = finishDate;
        treeNode.error = error;
        treeNode.contextListeners.addAll(contextListeners);
        return treeNode;
    }


}


