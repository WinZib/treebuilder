package org.egzi.treebuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class Tree<K, V> {

    private final TreeNode<K, V> rootNode;


    Tree(TreeNode<K, V> rootNode) {
        if (rootNode == null)
            throw new IllegalArgumentException("root node can't be a null");
        this.rootNode = rootNode;
    }

    public TreeNode<K, V> getRoot() {
        return rootNode;
    }

    public Stream<TreeNode<K, V>>  treeNodeStream() {
        final ArrayList<TreeNode<K, V>> nodes = new ArrayList<>();
        return getNodesList(rootNode, nodes).stream();
    }

    /*it's awful algorithm but is's pertty and clear */
    private Collection<TreeNode<K, V>> getNodesList(TreeNode<K, V> currentNode, List<TreeNode<K,V>> nodes ) {
        if (currentNode == null) {
            return nodes;
        }
        nodes.add(currentNode);
        for (TreeNode<K, V> node : currentNode.getChildren()) {
            getNodesList(node, nodes);
        }
        return nodes;
    }
}
