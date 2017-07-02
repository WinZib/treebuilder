package org.egzi.treebuilder.visitors;

import org.egzi.treebuilder.TreeNode;

public class DFSWalker<K, V> extends AbstractWalker<K,V> {
    public void walk(TreeNode<K, V> treeNode) {
        getVisitor().doVisit(treeNode);
        for (TreeNode<K, V> child : treeNode.getChilds()) {
            walk(child);
        }
    }
}
