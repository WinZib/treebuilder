package org.egzi.treebuilder.visitors;

import org.egzi.treebuilder.TreeNode;

/**
 * Created by Егор on 27.09.2016.
 */
public interface Visitor<K, V> {
    <R> R doVisit(TreeNode<K,V> node);

}
