package org.egzi.treebuilder;

import java.util.Collection;

/**
 * Created by Егор on 14.09.2016.
 */
public interface TreeNode<K, V> {
    K getParentId();

    K getId();

    V getObject();

    Collection<TreeNode<K,V>> getChilds();

    void addChildNode(TreeNode<K, V> node);
}
