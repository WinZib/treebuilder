package org.egzi.treebuilder.visitors;

import org.egzi.treebuilder.Forest;
import org.egzi.treebuilder.TreeNode;

import java.util.Map;
import java.util.concurrent.ForkJoinTask;

/**
 * Created by egorz on 6/6/2017.
 */
public interface Walker<K,V> {
    void setVisitor(final Visitor<K, V> visitor);

    ForkJoinTask<Void> walk(TreeNode<K,V> treeNode);

    Map<K, ForkJoinTask<Void>> walk(Forest<K, V> forest);

}
