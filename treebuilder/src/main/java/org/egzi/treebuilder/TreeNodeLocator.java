package org.egzi.treebuilder;

/**
 * Abstraction to find next TreeNode
 * @param <K> key type in a tree
 * @param <V> value type in a tree
 */
public interface TreeNodeLocator<K, V> {
    /**
     * Return next TreeNode instance from some source
     * @return {@see TreeNode} instance
     */
    TreeNode<K, V> getNextTreeNode();

    /**
     * Identifies presence of another TreeNode instances
     * @return <b>true</b> if there's another tree instances
     *         <b>false</b> otherwise
     */
    boolean hasMore();
}
