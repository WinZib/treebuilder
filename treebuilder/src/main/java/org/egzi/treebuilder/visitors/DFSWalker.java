package org.egzi.treebuilder.visitors;

import org.egzi.treebuilder.TreeNode;

import java.util.concurrent.ForkJoinTask;

public class DFSWalker<K, V> extends AbstractWalker<K,V> {

    public ForkJoinTask<Void> walk(TreeNode<K,V> treeNode) {
        getVisitor().doVisit(treeNode);
        for (TreeNode<K, V> child : treeNode.getChildren()) {
            walk(child);
        }
        return null;
    }




}
