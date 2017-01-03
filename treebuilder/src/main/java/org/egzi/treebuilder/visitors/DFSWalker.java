package org.egzi.treebuilder.visitors;

import org.egzi.treebuilder.TreeNode;

/**
 * Created by Егор on 27.09.2016.
 */
public class DFSWalker<K,V> {
    private TreeNode<K,V> currentNode;

    public DFSWalker(final TreeNode<K,V> startNode) {
        this.currentNode = startNode;
    }

    public TreeNode<K, V> nextNode() {
        return null;
    }

    public <R> R doVisit(TreeNode<K, V> node) {
        return null;
    }
}
