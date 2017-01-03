package org.egzi.treebuilder.visitors;

import org.egzi.treebuilder.TreeNode;

import java.util.concurrent.Callable;

/**
 * Created by Егор on 28.09.2016.
 */
public class TreeNodeTask<K,V> implements Callable {
    private TreeNode<K,V> treeNode;
    private Visitor<K,V> visitor;

    public TreeNodeTask(final TreeNode<K,V> treeNode, final Visitor<K,V> visitor) {
        this.treeNode = treeNode;
        this.visitor = visitor;
    }

    public Object call() throws Exception {
        return visitor.doVisit(treeNode);
    }
}