package org.egzi.treebuilder.visitors;

import org.egzi.treebuilder.TreeNode;

/**
 * Listener react on error when {@see Visitor} doVisit fails on some treenode
 */
public interface OnErrorListener {
    /**
     * Execute logic on <b>treeNode</b> during iteration
     * @param treeNode current {@see TreeNode}
     * @param <K> type of tree id
     * @param <V> type of tree value
     */
    <K,V> void report(TreeNode<K,V> treeNode);
}
