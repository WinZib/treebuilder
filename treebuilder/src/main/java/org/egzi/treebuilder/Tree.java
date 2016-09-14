package org.egzi.treebuilder;

/**
 * Created by Егор on 14.09.2016.
 */
public class Tree<K,V> {
    private TreeNode<K, V> rootNode;

    Tree(TreeNode<K, V> rootNode) {
        if (rootNode == null)
            throw new IllegalArgumentException("root node can't be a null");

        this.rootNode = rootNode;
    }
}
