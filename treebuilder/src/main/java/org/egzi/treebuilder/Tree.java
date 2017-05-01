package org.egzi.treebuilder;


public class Tree<K,V> {
    private TreeNode<K, V> rootNode;

    Tree(TreeNode<K, V> rootNode) {
        if (rootNode == null)
            throw new IllegalArgumentException("root node can't be a null");

        this.rootNode = rootNode;
    }

    public TreeNode<K,V> getRoot() {
        return rootNode;
    }
}
