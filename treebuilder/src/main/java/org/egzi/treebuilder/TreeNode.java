package org.egzi.treebuilder;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Interface representing a node in a tree
 *
 * @param <K> type for key entity in node
 * @param <V> type for value entity in node
 */
public interface TreeNode<K, V> {
    /**
     * Return Parent Node key
     *
     * @return <b>K</b> or null if it's the root
     */
    K getParentId();

    /**
     * Return Parent Node
     *
     * @return <b>parent</b> node instance or null if it's the root
     */
    TreeNode<K, V> getParent();

    /**
     * set parent node for specified
     *
     * @param parent new parent node
     */
    void setParent(TreeNode<K, V> parent);

    /**
     * Return entity key
     *
     * @return <b>K</b>
     */
    K getId();

    /**
     * Return entity value
     *
     * @return <b>V</b>
     */
    V get();

    /**
     * Set entity value
     *
     * @param value new entity value
     */
    void set(V value);

    /**
     * Return children nodes
     *
     * @return children
     */
    Collection<TreeNode<K, V>> getChildren();

    /**
     * Add child node to the node
     *
     * @param node child node
     */
    void addChildNode(TreeNode<K, V> node);

    /**
     * Check what current node is visit by visitor
     *
     * @return true if node was processed, false in other case
     */
    boolean isVisited();



    /**
     * Awaiting node processing for specif timeout
     * @param timeout awaiting timeout, 0 is infinite]
     * @param unit timeunit for timeout
     *
     * @throws Exception on interrupting and end of time
     */
    void awaitVisited(long timeout, TimeUnit unit) ;

    /**
     * Marking nod as processed
     *
     * @throws IllegalStateException if node have been already processed
     */
    void markVisited();
}
