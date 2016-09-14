package org.egzi.treebuilder;

/**
 * Created by Егор on 14.09.2016.
 */
public interface TreeNodeLocator<K, V> {
    TreeNode<K, V> getNextTreeNode();
    boolean hasMore();
}
