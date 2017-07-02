package org.egzi.treebuilder.visitors;

import org.egzi.treebuilder.TreeNode;

/**
 * Class described visiting of Tree node
 * @param <K> type of TreeNode id
 * @param <V> type of TreeNode value
 */
public interface Visitor<K, V> {
    /**
     * Do some action in node <b>node</b>
     * @param node current node
     * @param <R> type of Result
     * @return result of processing
     */
    <R> R doVisit(TreeNode<K,V> node);

}
