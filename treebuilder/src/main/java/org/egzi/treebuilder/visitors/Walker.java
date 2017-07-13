package org.egzi.treebuilder.visitors;

import org.egzi.treebuilder.Forest;
import org.egzi.treebuilder.TreeNode;

/**
 * Created by egorz on 6/6/2017.
 */
public interface Walker<K, V> {
    void setVisitor(final Visitor<K, V> visitor);

    void walk(TreeNode<K, V> treeNode);

    void walk(Forest<K, V> forest);
}
