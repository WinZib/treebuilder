package org.egzi.treebuilder.visitors;

import org.egzi.treebuilder.TreeNode;

/**
 * Created by Егор on 28.09.2016.
 */
public interface OnErrorListener {
    <K,V> void report(TreeNode<K,V> treeNode);
}
